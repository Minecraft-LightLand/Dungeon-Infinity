package dev.xkmc.dungeon_infinity.content.chunkgen;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.content.map.MapLogger;
import dev.xkmc.dungeon_infinity.content.maze.generator.IRandom;
import dev.xkmc.dungeon_infinity.content.maze.generator.MazeConfig;
import dev.xkmc.dungeon_infinity.content.maze.generator.MazeGen;
import dev.xkmc.dungeon_infinity.content.maze.objective.BranchMarker;
import dev.xkmc.dungeon_infinity.content.maze.objective.MazeRegistry;
import net.minecraft.util.RandomSource;

import java.util.*;

public class RoomProcessorStrategy {

	private static final String[] STYLES = {"sculk", "deepslate", "copper", "mineshaft", "stone"};

	private static final Map<String, Integer> SPECIAL_ROOMS = new TreeMap<>(Map.of(
			"warehouse", 6,
			"workshop", 6,
			"shop", 6
	));

	private final int r1;

	public RoomProcessorStrategy(int r1) {
		this.r1 = r1;
	}

	public float getRoomChance(int cell) {
		return switch (CellInterpreter.getTemplateType(cell)) {
			case 1 -> 0.9f;
			case 5 -> 1;
			case 4 -> 0.5f;
			case 2, 3 -> 0.3f;
			default -> 0;
		};
	}

	public float getEndLargeRoomChance() {
		return 0.8f;
	}

	public int getMaxQuadRoom(int max) {
		return Math.clamp((int) (max * 0.8f), Math.min(max, 5), 10);
	}

	public float getHallLargeRoomChance() {
		return 0.8f;
	}

	public int getBossRoomCount() {
		return 4;
	}

	public MazeGen genMaze(int rad, long regionSeed) {
		MazeConfig config = new MazeConfig();
		config.invariant = 2;
		config.survive = 4;
		config.invarianceRim = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7}, {0, 4, 8, 12, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15}};
		var ans = new MazeGen(rad / 2, IRandom.parse(new Random(regionSeed)), config, new MazeGen.Debugger());
		ans.gen();
		enhanceConnections(ans.ans);
		return ans;
	}

	public int getStyleForLayer(int layer) {
		return TemplateConfig.get().styleIndex(STYLES[layer]);
	}

	public void enhanceConnections(int[][] maze) {
		new ConnectionGen(maze).enhanceConnections();
	}

	private class ConnectionGen {

		private final int r = r1 / 2;
		private final int[][] maze;
		private final BranchMarker[][] branch;
		private final Map<Integer, Pair<Integer, Integer>> map = new LinkedHashMap<>();

		private ConnectionGen(int[][] maze) {
			this.maze = maze;
			branch = MazeRegistry.BRANCH.generate(maze, r, r).value;
			branch[r - 1][r].color = 1;
			branch[r + 1][r].color = 2;
			branch[r][r - 1].color = 3;
			branch[r][r + 1].color = 4;
			branch[r - 1][r].dist = 0;
			branch[r + 1][r].dist = 0;
			branch[r][r - 1].dist = 0;
			branch[r][r + 1].dist = 0;
		}

		private boolean isCenter(int x, int z) {
			return x >= r - 2 && x <= r + 2 && z >= r - 2 && z <= r + 2;
		}

		private boolean isEnd(int cell) {
			return cell == 1 || cell == 2 || cell == 4 || cell == 8;
		}

		private void enhanceConnections() {
			for (int x = 0; x < r1; x++) {
				for (int z = 0; z < r1; z++) {
					if (isCenter(x, z)) continue;
					var cell = maze[x][z];
					if (!isEnd(cell)) continue;
					if ((cell & 1) == 0 && x > 0) find(x, z, x - 1, z, 1);
					if ((cell & 2) == 0 && x < r1 - 1) find(x, z, x + 1, z, 2);
					if ((cell & 4) == 0 && z > 0) find(x, z, x, z - 1, 4);
					if ((cell & 8) == 0 && z < r1 - 1) find(x, z, x, z + 1, 8);
				}
			}
			for (var pair : map.values()) {
				int flag = pair.getSecond();
				int dir = flag >> 16;
				int p0 = flag & 0xFFFF;
				int x = p0 / r1;
				int z = p0 % r1;
				maze[x][z] |= dir;
				if (dir == 1) maze[x - 1][z] |= 2;
				if (dir == 2) maze[x + 1][z] |= 1;
				if (dir == 4) maze[x][z - 1] |= 8;
				if (dir == 8) maze[x][z + 1] |= 4;
			}
		}

		private void find(int x0, int z0, int x1, int z1, int dir) {
			if (isCenter(x1, z1)) return;
			int b0 = branch[x0][z0].getColor();
			int b1 = branch[x1][z1].getColor();
			if (b0 == b1) return;
			int b = (1 << b0) | (1 << b1);
			int p0 = x0 * r1 + z0;
			int dis = Math.min(branch[x0][z0].dist(), branch[x1][z1].dist());
			map.compute(b, (_, old) -> old == null || old.getFirst() < dis ? Pair.of(dis, dir << 16 | p0) : old);
		}

	}

	public class StairGen {

		private final int y1;

		public StairGen(int y1) {
			this.y1 = y1;
		}

		private void prefill(int[][] marker, int[][] maze) {
			for (int dx = 0; dx < r1; dx++) {
				for (int dz = 0; dz < r1; dz++) {
					int cell = maze[dx][dz];
					if (cell == 1 && dx > 0) marker[dx - 1][dz] = 1;
					if (cell == 2 && dx < r1 - 1) marker[dx + 1][dz] = 1;
					if (cell == 4 && dz > 0) marker[dx][dz - 1] = 1;
					if (cell == 8 && dz < r1 - 1) marker[dx][dz + 1] = 1;
				}
			}
		}

		private void fillLayer(int[][] prevMarker, int[][] marker, int[][] low, int[][] maze, int m, int y, Random rand) {
			for (int dx = m; dx < r1 - m; dx++) {
				for (int dz = m; dz < r1 - m; dz++) {
					if ((marker[dx][dz] & 1) != 0 || (prevMarker[dx][dz] & 1) != 0)
						continue;
					boolean stairs = low[dx][dz] == 1 && maze[dx][dz] == 2 ||
							low[dx][dz] == 2 && maze[dx][dz] == 1 ||
							low[dx][dz] == 4 && maze[dx][dz] == 8 ||
							low[dx][dz] == 8 && maze[dx][dz] == 4;
					boolean cross =
							low[dx][dz] == 3 && maze[dx][dz] == 12 ||
									low[dx][dz] == 12 && maze[dx][dz] == 3;
					float chance = 0;
					if (stairs) chance = 0.3f + 0.7f * (y + 1) / y1;
					else if (cross) chance = 0.2f + 0.3f * (y + 1) / y1;
					if (chance <= 0) continue;
					float val = rand.nextFloat();
					if (val < chance) {
						low[dx][dz] |= 16;
						maze[dx][dz] |= 32;
						for (int ddx = -m; ddx <= m; ddx++) {
							for (int ddz = -m; ddz <= m; ddz++) {
								marker[dx + ddx][dz + ddz] |= 1;
							}
						}
					}
				}
			}
		}

		public void fillStairs(MazeDimHolder.RegionStack.TopRegion.SubRegion[] regions, long stairSeed, int[] bossRoom) {
			var rand = new Random(stairSeed);
			int[][] prevMarker = new int[r1][r1];
			prefill(prevMarker, regions[0].maze);
			int m = 2;
			for (int y = 1; y < y1; y++) {
				var low = regions[y - 1].maze;
				var maze = regions[y].maze;
				int[][] marker = new int[r1][r1];
				prefill(marker, maze);
				if (bossRoom[y] != 2)
					fillLayer(prevMarker, marker, low, maze, m, y, rand);
				prevMarker = marker;
			}
		}

	}

	public class Grid {

		private final MapLogger logger;
		private final RandomSource rand;
		private final int style;
		private final int[][] maze;
		private final int[][] marker = new int[r1][r1];
		private final int[][] variants = new int[r1][r1];

		public Grid(MapLogger logger, RandomSource rand, int style, int[][] maze) {
			this.logger = logger;
			this.rand = rand;
			this.style = style;
			this.maze = maze;
		}

		public void set(int x, int z, int mark) {
			marker[x][z] = mark;
			maze[x][z] |= CellInterpreter.setRoomTypeMask(maze[x][z], mark);
			variants[x][z] = TemplateConfig.of(maze[x][z]).getRandom(style, rand);
			maze[x][z] |= CellInterpreter.setStyleAndVariant(style, variants[x][z]);
		}

		public void set(int x, int z, int mark, int variant) {
			marker[x][z] = mark;
			variants[x][z] = variant;
			maze[x][z] |= CellInterpreter.setRoomTypeMask(maze[x][z], mark);
			maze[x][z] |= CellInterpreter.setStyleAndVariant(style, variant);
		}

		public void set(int x, int z, int mark, String variant) {
			marker[x][z] = mark;
			maze[x][z] |= CellInterpreter.setRoomTypeMask(maze[x][z], mark);
			variants[x][z] = TemplateConfig.of(maze[x][z]).variantIndex(style, variant);
			maze[x][z] |= CellInterpreter.setStyleAndVariant(style, variants[x][z]);
		}

		public void setEndRoom(int x, int z) {
			marker[x][z] = -1;
		}


		private final int[][] margin = new int[r1][r1];
		private final ArrayDeque<Integer> endRooms = new ArrayDeque<>();
		private final ArrayDeque<Integer> fallback = new ArrayDeque<>();

		private int getEndRoom() {
			while (true) {
				if (endRooms.isEmpty()) {
					assert !fallback.isEmpty();
					return fallback.poll();
				}
				int pos = endRooms.poll();
				int x = pos / r1;
				int z = pos % r1;
				if (margin[x][z] == 0) {
					spread(x, z, 4);
					return pos;
				}
				fallback.add(pos);
			}
		}

		private void spread(int x, int z, int step) {
			if (x < 0 || z < 0 || x >= r1 || z >= r1) return;
			if (step <= 0) return;
			if (margin[x][z] >= step) return;
			margin[x][z] = step;
			int cell = maze[x][z];
			if ((cell & 1) != 0) spread(x - 1, z, step - 1);
			if ((cell & 2) != 0) spread(x + 1, z, step - 1);
			if ((cell & 4) != 0) spread(x, z - 1, step - 1);
			if ((cell & 8) != 0) spread(x, z + 1, step - 1);
		}

		public void resolveEndRoom() {
			List<Integer> ends = new ArrayList<>();
			for (int x = 0; x < r1; x++) {
				for (int z = 0; z < r1; z++) {
					if (marker[x][z] == -1) {
						ends.add(x * r1 + z);
					}
				}
			}
			logger.print("Unsorted End Room Candidates: %s", ends);
			int n = ends.size();
			for (int i = 0; i < n * 2; i++) {
				int a = rand.nextInt(n);
				int b = rand.nextInt(n);
				var temp = ends.get(a);
				ends.set(a, ends.get(b));
				ends.set(b, temp);
			}
			logger.print("Sorted Room Candidates: %s", ends);
			this.endRooms.addAll(ends);

			int total = 0;
			Map<String, Integer> rooms = new LinkedHashMap<>();
			for (var e : SPECIAL_ROOMS.entrySet()) {
				total += e.getValue();
				rooms.put(e.getKey(), e.getValue());
			}
			if (total > ends.size()) {
				int average = ends.size() / SPECIAL_ROOMS.size();
				for (var e : SPECIAL_ROOMS.entrySet()) {
					if (e.getValue() > average) {
						total -= e.getValue() - average;
						rooms.put(e.getKey(), average);
					}
				}
			}
			logger.print("Room Types: %s", rooms);
			for (var e : rooms.entrySet()) {
				String room = e.getKey();
				int count = e.getValue();
				for (int i = 0; i < count; i++) {
					int pos = getEndRoom();
					int x = pos / r1;
					int z = pos % r1;
					logger.print("Set Room: %s at %d", room, pos);
					set(x, z, CellInterpreter.HALLWAY, room);
				}
			}
			var remain = new ArrayList<>(endRooms);
			remain.addAll(fallback);
			for (var pos : remain) {
				int x = pos / r1;
				int z = pos % r1;
				set(x, z, CellInterpreter.ROOM + 1);
			}

		}

	}

	public class Scanner {

		private final int[][] maze;
		private final Grid marker;

		public Scanner(int[][] maze, Grid marker) {
			this.maze = maze;
			this.marker = marker;
		}

		public void scan(RandomSource rand) {
			List<int[]> candidates = new ArrayList<>();
			for (int dx = 0; dx < r1 - 1; dx++) {
				for (int dz = 0; dz < r1 - 1; dz++) {
					if (maze[dx][dz] >= 16) continue;
					if (maze[dx][dz + 1] >= 16) continue;
					if (maze[dx + 1][dz] >= 16) continue;
					if (maze[dx + 1][dz + 1] >= 16) continue;
					int count = 0;
					if ((maze[dx][dz] & 1) != 0) count++;
					if ((maze[dx][dz + 1] & 1) != 0) count++;
					if ((maze[dx + 1][dz] & 2) != 0) count++;
					if ((maze[dx + 1][dz + 1] & 2) != 0) count++;
					if ((maze[dx][dz] & 4) != 0) count++;
					if ((maze[dx + 1][dz] & 4) != 0) count++;
					if ((maze[dx][dz + 1] & 8) != 0) count++;
					if ((maze[dx + 1][dz + 1] & 8) != 0) count++;
					if (count == 1) candidates.add(new int[]{dx, dz});
				}
			}
			int max = getMaxQuadRoom(candidates.size());
			for (int i = 0; i < max; i++) {
				var e = candidates.remove(rand.nextInt(candidates.size()));
				int dx = e[0];
				int dz = e[1];
				maze[dx][dz] |= 10 | CellInterpreter.setQuadRoom(0, 0);
				maze[dx + 1][dz] |= 9 | CellInterpreter.setQuadRoom(1, 0);
				maze[dx][dz + 1] |= 6 | CellInterpreter.setQuadRoom(0, 1);
				maze[dx + 1][dz + 1] |= 5 | CellInterpreter.setQuadRoom(1, 1);
			}
			for (var e : candidates) {
				int dx = e[0];
				int dz = e[1];
				for (int ddx = 0; ddx <= 1; ddx++) {
					for (int ddz = 0; ddz <= 1; ddz++) {
						int cell = maze[dx + ddx][dz + ddz];
						if (CellInterpreter.getTemplateType(cell) != 1)
							marker.set(dx + ddx, dz + ddz, CellInterpreter.HALLWAY);
					}
				}

			}
		}

	}

	public class Marker {

		private final RandomSource rand;
		private final Grid grid;
		private final int[][] maze;
		Queue<int[]> rooms = new ArrayDeque<>();
		Queue<int[]> largeRooms = new ArrayDeque<>();
		Queue<int[]> hallways = new ArrayDeque<>();

		public Marker(RandomSource rand, Grid grid, int[][] maze) {
			this.rand = rand;
			this.grid = grid;
			this.maze = maze;
		}

		private void prefill(int x, int z) {
			if (x < 0 || z < 0 || x >= r1 || z >= r1) return;
			if (grid.marker[x][z] != 0) return;
			grid.set(x, z, CellInterpreter.HALLWAY);
		}

		public void mark() {
			for (int x = 0; x < r1; x++) {
				for (int z = 0; z < r1; z++) {
					if (grid.marker[x][z] != 0) continue;
					int cell = maze[x][z];
					if (cell >= 64) {
						grid.set(x, z, CellInterpreter.SPECIAL);
						continue;
					}
					int flag = CellInterpreter.getCellFlags(cell);
					if (CellInterpreter.getTemplateType(cell) == 1) {
						grid.setEndRoom(x, z);
					} else if (flag != 3) {
						grid.set(x, z, CellInterpreter.getRoomMarker(cell, flag));
					} else if ((cell & 1) != 0 && x == 0 || (cell & 2) != 0 && x == r1 - 1 ||
							(cell & 4) != 0 && z == 0 || (cell & 8) != 0 && z == r1 - 1) {
						grid.set(x, z, CellInterpreter.HALLWAY);
					}
				}
			}
			grid.resolveEndRoom();
			for (int x = 0; x < r1; x++) {
				for (int z = 0; z < r1; z++) {
					int cell = maze[x][z];
					int mark = grid.marker[x][z];
					if (mark == CellInterpreter.SPECIAL || mark == CellInterpreter.HALLWAY &&
							CellInterpreter.getTemplateType(cell) == 1) {
						if ((cell & 1) != 0) prefill(x - 1, z);
						if ((cell & 2) != 0) prefill(x + 1, z);
						if ((cell & 4) != 0) prefill(x, z - 1);
						if ((cell & 8) != 0) prefill(x, z + 1);
					}
				}
			}
			for (int x = 0; x < r1; x++) {
				for (int z = 0; z < r1; z++) {
					int ans = grid.marker[x][z];
					if (ans >= CellInterpreter.HALLWAY) {
						if (ans > CellInterpreter.ROOM) largeRooms.add(new int[]{x, z});
						if (ans == CellInterpreter.ROOM) rooms.add(new int[]{x, z});
						if (ans == CellInterpreter.HALLWAY) hallways.add(new int[]{x, z});
					}
				}
			}
			while (!rooms.isEmpty() || !largeRooms.isEmpty() || !hallways.isEmpty()) {
				while (!rooms.isEmpty()) {
					expand(rooms.poll());
				}
				if (!largeRooms.isEmpty()) {
					expand(largeRooms.poll());
				} else if (!hallways.isEmpty()) {
					expand(hallways.poll());
				}
			}
		}

		private void expand(int[] r) {
			int x = r[0];
			int z = r[1];
			int cell = maze[x][z];
			int room = grid.marker[x][z];
			int variant = grid.variants[x][z];
			if ((cell & 1) != 0) fromRoom(x - 1, z, room, variant);
			if ((cell & 2) != 0) fromRoom(x + 1, z, room, variant);
			if ((cell & 4) != 0) fromRoom(x, z - 1, room, variant);
			if ((cell & 8) != 0) fromRoom(x, z + 1, room, variant);
		}

		private void fromRoom(int x, int z, int src, int variant) {
			if (x < 0 || x >= r1 || z < 0 || z >= r1) return;
			if (grid.marker[x][z] != 0) return;
			if (src > CellInterpreter.ROOM && rand.nextFloat() < getEndLargeRoomChance()) {
				grid.set(x, z, src - 1, variant);
				rooms.add(new int[]{x, z});
			} else if (src >= CellInterpreter.ROOM) {
				grid.set(x, z, CellInterpreter.HALLWAY);
				hallways.add(new int[]{x, z});
			} else if (rand.nextFloat() < getRoomChance(maze[x][z])) {
				if (rand.nextFloat() < getHallLargeRoomChance()) {
					grid.set(x, z, CellInterpreter.ROOM + 1);
					largeRooms.add(new int[]{x, z});
				} else {
					grid.set(x, z, CellInterpreter.ROOM);
					rooms.add(new int[]{x, z});
				}
			} else {
				grid.set(x, z, CellInterpreter.HALLWAY);
				hallways.add(new int[]{x, z});
			}
		}

	}

	public static class Itr {

		private final int r1 = 25;
		private final Queue<int[]> queue = new ArrayDeque<>();
		private final int[][] maze;
		private final int[][] marker = new int[r1][r1];

		public Itr(int[][] maze) {
			this.maze = maze;
		}

		private void tryAdd(int x, int z) {
			if (marker[x][z] > 0) return;
			marker[x][z] = 1;
			int cell = maze[x][z];
			if (CellInterpreter.isHallway(cell)) return;
			queue.add(new int[]{x, z});
		}

		public List<int[]> findRooms(int x, int z) {
			queue.add(new int[]{x, z});
			marker[x][z] = 1;
			List<int[]> list = new ArrayList<>();
			while (!queue.isEmpty()) {
				var p = queue.poll();
				list.add(p);
				int px = p[0];
				int pz = p[1];
				int c = maze[px][pz];
				if ((c & 1) != 0 && px > 0) tryAdd(px - 1, pz);
				if ((c & 2) != 0 && px < r1 - 1) tryAdd(px + 1, pz);
				if ((c & 4) != 0 && pz > 0) tryAdd(px, pz - 1);
				if ((c & 8) != 0 && pz < r1 - 1) tryAdd(px, pz + 1);
			}
			return list;
		}

	}

	public static List<int[]> findRooms(int[][] maze, int x, int z) {
		return new Itr(maze).findRooms(x, z);
	}

}

