package dev.xkmc.dungeon_infinity.content.chunkgen;

import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.config.ColumnLayoutConfig;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class MazeDimHolder {

	public static final Map<Long, MazeDimHolder> cache = new Long2ObjectOpenHashMap<>();

	public static MazeDimHolder get(long seed) {
		return cache.computeIfAbsent(seed, MazeDimHolder::new);
	}

	private final int r1 = 25;
	private final int r2 = 25;
	private final int y1 = 16;
	private final long seed;
	private final Long2ObjectMap<RegionStack> stacks = new Long2ObjectOpenHashMap<>();
	private final RoomProcessorStrategy strategy = new RoomProcessorStrategy(r1);

	public MazeDimHolder(long seed) {
		this.seed = seed;
	}

	public synchronized RegionStack get(int x, int z) {
		return stacks.computeIfAbsent(ChunkPos.pack(x, z), k0 -> new RegionStack(x, z));
	}

	public int getCell(MazePos pos) {
		return getRegion(pos.x(), pos.y(), pos.z())[pos.px() >> 4][pos.pz() >> 4];
	}

	public synchronized int[][] getRegion(int x, int y, int z) {
		int x2 = Mth.floorDiv(x, r2);
		int z2 = Mth.floorDiv(z, r2);
		var stack = get(x2, z2);
		int cx = x - x2 * r2;
		int cz = z - z2 * r2;
		stack.getColumn(cx, cz).check();
		return stack.getRegion(y).getRegion(cx, cz).getMaze();
	}

	public synchronized int[] getColumn(int x, int z) {
		int x2 = Mth.floorDiv(x, r2 * r1);
		int z2 = Mth.floorDiv(z, r2 * r1);
		var stack = get(x2, z2);
		int x1 = x - x2 * r2 * r1;
		int z1 = z - z2 * r2 * r1;
		int cx = x1 / r1;
		int cz = z1 / r1;
		int rx = x1 - cx * r1;
		int rz = z1 - cz * r1;
		stack.getColumn(cx, cz).check();
		int[] ans = new int[y1];
		for (int i = 0; i < y1; i++) {
			ans[i] = stack.getRegion(i).getRegion(cx, cz).getCellType(rx, rz);
		}
		return ans;
	}

	public synchronized int getVisibility(MazePos pos) {
		int x2 = Mth.floorDiv(pos.x(), r2);
		int z2 = Mth.floorDiv(pos.z(), r2);
		var stack = get(x2, z2);
		var col = stack.getColumn(pos.x() - x2 * r2, pos.z() - z2 * r1);
		col.check();
		return col.visibility[pos.y()];
	}

	public class RegionStack {

		public final int x, z;
		public final @Nullable TopRegion[] regions = new TopRegion[y1];
		public final @Nullable MazeColumn[][] columns = new MazeColumn[r2][r2];
		public final @Nullable TopWall[] wallX = new TopWall[y1];
		public final @Nullable TopWall[] wallZ = new TopWall[y1];

		public RegionStack(int x, int z) {
			this.x = x;
			this.z = z;
		}

		public TopWall getWall(int y, Direction.Axis dir) {
			var side = dir == Direction.Axis.X ? wallX : dir == Direction.Axis.Z ? wallZ : null;
			if (side == null) throw new IllegalArgumentException("Wrong axis");
			if (side[y] != null) return side[y];
			var pos = new Vec3i(x, y, z);
			var ans = new TopWall(MazeRandHelper.getRootWallSeed(seed, pos, dir));
			side[y] = ans;
			return ans;
		}

		public TopRegion getRegion(int y) {
			if (regions[y] != null) return regions[y];
			var pos = new Vec3i(x, y, z);
			var regionSeed = MazeRandHelper.getRootCellSeed(seed, pos);
			var region = new TopRegion(regionSeed, pos);
			regions[y] = region;
			return region;
		}

		public MazeColumn getColumn(int cx, int cz) {
			if (columns[cx][cz] != null) return columns[cx][cz];
			var pos = new Vec3i(x * r2 + cx, 0, z * r2 + cz);
			var colSeed = MazeRandHelper.getColumnSeed(seed, pos.getX(), pos.getZ());
			var ans = new MazeColumn(colSeed, cx, cz);
			columns[cx][cz] = ans;
			return ans;
		}

		public class TopRegion {

			private final int y;
			private final int[][] wallX = new int[r2 + 1][r2];
			private final int[][] wallZ = new int[r2][r2 + 1];
			private final long[] subSeed = new long[r2 * r2];
			private final @Nullable SubRegion[][] sub = new SubRegion[r2][r2];

			public TopRegion(long seed, Vec3i pos) {
				this.y = pos.getY();
				long[] seeds = new long[3];
				MazeRandHelper.getChildrenSeeds(seed, seeds);
				int[][] maze = strategy.genMaze(r2, seeds[0]).ans;
				TopWall x0 = getWall(pos.getY(), Direction.Axis.X);
				TopWall x1 = get(pos.getX() + 1, pos.getZ()).getWall(pos.getY(), Direction.Axis.X);
				TopWall z0 = getWall(pos.getY(), Direction.Axis.Z);
				TopWall z1 = get(pos.getX(), pos.getZ() + 1).getWall(pos.getY(), Direction.Axis.Z);
				MazeRandHelper.getChildrenSeeds(seeds[1], subSeed);
				var rand = new Random(seeds[2]);
				for (int dx = 0; dx < r2 - 1; dx++) {
					for (int dz = 0; dz < r2 - 1; dz++) {
						if ((maze[dx][dz] & 2) != 0)
							wallX[dx + 1][dz] = rand.nextInt(r1);
						if ((maze[dx][dz] & 8) != 0)
							wallX[dx][dz + 1] = rand.nextInt(r1);
					}
				}
				for (int i = 0; i < r2; i++) {
					wallZ[i][0] = -1;
					wallZ[i][r2] = -1;
					wallX[0][i] = -1;
					wallX[r2][i] = -1;
				}
				wallX[0][x0.main] = x0.sub;
				wallX[r2][x1.main] = x1.sub;
				wallZ[z0.main][0] = z0.sub;
				wallZ[z1.main][r2] = z1.sub;
			}

			public SubRegion getRegion(int cx, int cz) {
				if (sub[cx][cz] != null) return sub[cx][cz];
				sub[cx][cz] = new SubRegion(subSeed[cx * r2 + cz], cx, cz);
				return sub[cx][cz];
			}

			public int getSubWall(int cx, int cz, Direction.Axis dir) {
				return (dir == Direction.Axis.X ? wallX : wallZ)[cx][cz];
			}

			public class SubRegion {

				private final int cx, cz;
				final int[][] maze;
				private final MazeColumn col;
				public final long[] roomSeed;

				private boolean checked = false;

				public SubRegion(long seed, int cx, int cz) {
					this.cx = cx;
					this.cz = cz;
					roomSeed = new long[4];
					MazeRandHelper.getChildrenSeeds(seed, roomSeed);
					this.maze = strategy.genMaze(r1, roomSeed[0]).ans;
					this.col = getColumn(cx, cz);
					int x0 = getSubWall(cx, cz, Direction.Axis.X);
					int x1 = getSubWall(cx + 1, cz, Direction.Axis.X);
					int z0 = getSubWall(cx, cz, Direction.Axis.Z);
					int z1 = getSubWall(cx, cz + 1, Direction.Axis.Z);
					if (x0 >= 0) maze[0][x0] |= 1;
					if (x1 >= 0) maze[r1 - 1][x1] |= 2;
					if (z0 >= 0) maze[z0][0] |= 4;
					if (z1 >= 0) maze[z1][r1 - 1] |= 8;
					if (col.bossRoom[y] > 0) {
						int offset = col.bossRoom[y] - 1;
						for (int dx = 0; dx <= 2; dx++) {
							for (int dz = 0; dz <= 2; dz++) {
								maze[r1 / 2 - 1 + dx][r1 / 2 - 1 + dz] |= CellInterpreter.setBossRoom(offset, dx, dz);
							}
						}
					}
				}

				public int[][] getMaze() {
					check();
					return maze;
				}

				public int getCellType(int x, int z) {
					check();
					return maze[x][z];
				}

				private void check() {
					if (checked) return;
					checked = true;
					col.check();
					var grid = strategy.new Grid(col.layout, RandomSource.create(roomSeed[1]), col.styles[y], maze);
					strategy.new Scanner(col.layout, maze, grid).scan(RandomSource.create(roomSeed[2]));
					strategy.new Marker(col.layout, RandomSource.create(roomSeed[3]), grid, maze).mark();
				}

			}

		}

		public class TopWall {
			private final int main, sub;

			public TopWall(long seed) {
				var rand = new Random(seed);
				main = rand.nextInt(r2);
				sub = rand.nextInt(r1);
			}

		}

		public class MazeColumn {

			private final int cx, cz;
			private final int[] bossRoom = new int[y1];
			private final int[] styles = new int[y1];
			private final int[] visibility = new int[y1];
			private final long stairSeed;
			private final ColumnLayoutConfig.Layout layout;

			private boolean checked = false;

			public MazeColumn(long seed, int cx, int cz) {
				this.cx = cx;
				this.cz = cz;
				var rand = RandomSource.create(seed);
				stairSeed = rand.nextLong();
				layout = ColumnLayoutConfig.getRandom(rand);
				var entries = layout.styles();
				int n = entries.size();
				int[] spaces = new int[n];
				int total = 0;
				for (int i = 0; i < n; i++) {
					total += entries.get(i).layer();
					spaces[i] = entries.get(i).layer();
				}
				if (total > y1) {
					total = 0;
					Arrays.fill(spaces, 0);
				}
				int rem = y1 - total;
				for (int i = 0; i < rem; i++) {
					int layer = rand.nextInt(n);
					spaces[layer]++;
				}
				int layer = 0;
				for (int i = 0; i < n - 1; i++) {
					int e = spaces[i];
					layer += e;
					bossRoom[layer - 1] = 1;
					bossRoom[layer] = 2;
				}
				layer = 0;
				for (int i = 0; i < y1; i++) {
					if (bossRoom[i] == 2) layer++;
					styles[i] = strategy.getStyleForLayer(entries.get(layer).style());
					visibility[i] = layer + 1;
				}
			}

			public void check() {
				if (checked) return;
				checked = true;
				var regions = new TopRegion.SubRegion[y1];
				for (int i = 0; i < y1; i++) {
					regions[i] = getRegion(i).getRegion(cx, cz);
				}
				strategy.new StairGen(y1).fillStairs(regions, stairSeed, bossRoom);
			}

		}
	}

}
