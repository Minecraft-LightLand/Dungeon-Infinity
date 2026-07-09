package dev.xkmc.dungeon_infinity.content.cap;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.dungeon_infinity.content.cap.packet.RevealPathToClient;
import dev.xkmc.dungeon_infinity.content.cap.packet.SyncFinderToClient;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
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
		List<Pair<Integer, Integer>> candidates = new ArrayList<>();
		List<Pair<Integer, Integer>> found = new ArrayList<>();
		int cmin = 1000000, fmin = 1000000;
		for (int x = 0; x < R; x++) {
			for (int z = 0; z < R; z++) {
				int cell = maze[x][z];
				if (visit.isDefeated(x, z)) continue;
				if (type.pred.test(cell)) {
					if (bfs.unlock[x][z] > 0) {
						int cost = bfs.ans[x][z];
						if (cost > cmin) continue;
						if (cost < cmin) {
							candidates.clear();
							cmin = cost;
						}
						candidates.add(Pair.of(bfs.ans[x][z], x << 5 | z));
					} else {
						int cost = bfs.ans[x][z];
						if (cost > fmin) continue;
						if (cost < fmin) {
							found.clear();
							fmin = cost;
						}
						found.add(Pair.of(bfs.ans[x][z], x << 5 | z));
					}
				}
			}
		}
		boolean findNew = cmin < fmin || prevType == type;
		prevType = type;
		var target = findNew && !candidates.isEmpty() ? candidates : !found.isEmpty() ? found : null;
		if (target == null) return false;
		var pair = target.get(rand.nextInt(target.size()));
		int pos = pair.getSecond();
		path = bfs.getPath(pos);
		visit.markPath(path);
		DungeonInfinity.HANDLER.toClientPlayer(new RevealPathToClient(mp, path), sp);
		return target == candidates;
	}

	private static class BFS {

		private final int[][] ans = new int[R][R], unlock = new int[R][R], prev = new int[R][R], maze;
		private final MazeHistory.Visit visit;
		private final Queue<Integer> queue = new ArrayDeque<>();

		public BFS(int[][] maze, MazeHistory.Visit visit, int x0, int z0) {
			this.maze = maze;
			this.visit = visit;
			ans[x0][z0] = 1;
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
			int dist = ans[x0][z0];
			if (ans[px][pz] > 0 && ans[px][pz] <= dist + 1) return;
			if (!CellInterpreter.isHallway(maze[px][pz]) && !visit.isDefeated(px, pz)) dist++;
			ans[px][pz] = dist;
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
