package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.content.packet.RevealPathToClient;
import dev.xkmc.dungeon_infinity.content.packet.SyncFinderToClient;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.IntPredicate;

@SerialClass
public class RoomFinder {

	private static final int R = 25;

	private static IntPredicate isShop(String str) {
		return cell -> CellInterpreter.isSpecial(cell) && CellInterpreter.getVariant(cell) ==
				TemplateConfig.of(cell).variantIndex(CellInterpreter.getStyle(cell), str);
	}

	private static boolean isStair(int cell) {
		return (cell & 32) != 0 || CellInterpreter.isBossRoom(cell) && CellInterpreter.getBossRoom(cell) >= 9;
	}

	private static boolean isQuadEntry(int cell) {
		return CellInterpreter.isQuadRoom(cell) && CellInterpreter.getTemplateType(cell) == 4;
	}

	public enum Type {
		WAREHOUSE(RoomFinder.isShop("warehouse")),
		WORKSHOP(RoomFinder.isShop("workshop")),
		SHOP(RoomFinder.isShop("shop")),
		QUAD(RoomFinder::isQuadEntry),
		STAIR(RoomFinder::isStair);

		final IntPredicate pred;

		Type(IntPredicate pred) {
			this.pred = pred;
		}
	}

	@SerialField
	public int finder;
	@SerialField
	public int defeated;

	@SerialField
	public int[] path = new int[0];
	@SerialField
	public @Nullable Type prevType = null;

	public void accumulate(ServerPlayer sp, int size) {
		int points = 10;
		int max = 3;
		if (finder >= max) {
			return;
		}
		defeated += size;
		if (defeated >= points) {
			defeated -= points;
			finder++;
			DungeonInfinity.HANDLER.toClientPlayer(new SyncFinderToClient(this), sp);
		}
	}

	public void find(ServerPlayer sp, MazeHistory data, Type type) {
		var room = MazeRoomData.get(sp.level(), SectionPos.of(sp.blockPosition()));
		if (room == null) return;
		var pos = MazePos.map(sp.blockPosition());
		MazeHistory.Visit visit = data.getOrCreate(pos);
		if (findPathTo(sp, pos, room.getAccess(), visit, type, sp.getRandom())) {
			if (!sp.isCreative()) finder--;
			DungeonInfinity.HANDLER.toClientPlayer(new SyncFinderToClient(this), sp);
		}
	}

	private boolean findPathTo(ServerPlayer sp, MazePos mp, MazeAccess access, MazeHistory.Visit visit, Type type, RandomSource rand) {
		int[][] maze = access.getMaze();
		var bfs = new BFS(maze, visit, access.getX(), access.getZ()).run();
		var next = getNext(maze, bfs, visit, type, rand);
		prevType = type;
		if (next == null) {
			path = new int[0];
			return false;
		}
		path = bfs.getPath(next.pos());
		visit.markPath(path);
		DungeonInfinity.HANDLER.toClientPlayer(new RevealPathToClient(mp, path), sp);
		return next.takeFinder();
	}

	private @Nullable TargetRoom getNext(int[][] maze, BFS bfs, MazeHistory.Visit visit, Type type, RandomSource rand) {
		List<TargetRoom> candidates = new ArrayList<>();
		List<TargetRoom> found = new ArrayList<>();
		int cmin = 1000000, fmin = 1000000;
		for (int x = 0; x < R; x++) {
			for (int z = 0; z < R; z++) {
				int cell = maze[x][z];
				if (visit.isDefeated(x, z)) continue;
				if (type.pred.test(cell)) {
					int cost = bfs.ans[x][z];
					boolean takeFinder = bfs.unlock[x][z] > 0;
					var tri = new TargetRoom(cost, bfs.dist[x][z], x << 5 | z, takeFinder);
					if (takeFinder) {
						if (cost > cmin) continue;
						if (cost < cmin) {
							candidates.clear();
							cmin = cost;
						}
						candidates.add(tri);
					} else {
						fmin = Math.min(fmin, cost);
						found.add(tri);
					}
				}
			}
		}
		found.sort(TargetRoom.COMPARATOR);

		boolean repeat = prevType == type;
		boolean findNew = cmin < fmin || repeat;
		if (findNew && !candidates.isEmpty()) {
			return candidates.get(rand.nextInt(candidates.size()));
		} else if (!found.isEmpty()) {
			if (repeat && path.length > 0) {
				for (int i = 0; i < found.size(); i++) {
					if (found.get(i).pos == path[0]) {
						return found.get((i + 1) % found.size());
					}
				}
			}
			return found.getFirst();
		} else return null;
	}

	private record TargetRoom(int cost, int dist, int pos, boolean takeFinder) {

		public final static Comparator<TargetRoom> COMPARATOR = Comparator.comparingInt(TargetRoom::cost)
				.thenComparingInt(TargetRoom::dist)
				.thenComparing(TargetRoom::pos);
	}

	private static class BFS {

		private final int[][] ans = new int[R][R], dist = new int[R][R], unlock = new int[R][R], prev = new int[R][R], maze;
		private final MazeHistory.Visit visit;
		private final Queue<Integer> queue = new ArrayDeque<>();

		public BFS(int[][] maze, MazeHistory.Visit visit, int x0, int z0) {
			this.maze = maze;
			this.visit = visit;
			ans[x0][z0] = 1;
			dist[x0][z0] = 1;
			for (var arr : prev)
				Arrays.fill(arr, -1);
			queue.add(x0 << 5 | z0);
		}

		public BFS run() {
			while (!queue.isEmpty()) {
				int pos = queue.poll();
				int px = pos >> 5, pz = pos & 31;

				int c = CellInterpreter.getOpenings(maze[px][pz]);
				if ((c & 1) != 0 && px > 0) tryAdd(px, pz, px - 1, pz);
				if ((c & 2) != 0 && px < R - 1) tryAdd(px, pz, px + 1, pz);
				if ((c & 4) != 0 && pz > 0) tryAdd(px, pz, px, pz - 1);
				if ((c & 8) != 0 && pz < R - 1) tryAdd(px, pz, px, pz + 1);
			}
			return this;
		}

		private void tryAdd(int x0, int z0, int px, int pz) {
			int roomCost = ans[x0][z0];
			if (ans[px][pz] > 0 && ans[px][pz] <= roomCost + 1) return;
			if (!CellInterpreter.isHallway(maze[px][pz]) && !visit.isDefeated(px, pz)) roomCost++;
			ans[px][pz] = roomCost;
			dist[px][pz] = dist[x0][z0] + 1;
			unlock[px][pz] = unlock[x0][z0];
			if (!visit.isVisible(px, pz))
				unlock[px][pz]++;
			prev[px][pz] = x0 << 5 | z0;
			queue.add(px << 5 | pz);
		}

		public int[] getPath(int pos) {
			int px = pos >> 5, pz = pos & 31;
			List<Integer> list = new ArrayList<>();
			list.add(pos);
			while (true) {
				int pre = prev[px][pz];
				if (pre < 0) {
					int[] arr = new int[list.size()];
					for (int i = 0; i < list.size(); i++)
						arr[i] = list.get(i);
					return arr;
				}
				list.add(pre);
				px = pre >> 5;
				pz = pre & 31;
			}
		}
	}

}
