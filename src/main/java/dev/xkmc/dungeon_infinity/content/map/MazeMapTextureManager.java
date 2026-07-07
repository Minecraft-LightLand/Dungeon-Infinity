package dev.xkmc.dungeon_infinity.content.map;

import com.mojang.blaze3d.platform.NativeImage;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeDimHolder;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;

public class MazeMapTextureManager implements AutoCloseable {

	public static MazeMapTextureManager get() {
		return ((MapTextureManagerProvider) (Minecraft.getInstance().getMapTextureManager())).dungeon_infinity$getMazeData();
	}

	private final Long2ObjectMap<MazeLevelMapSet> dims = new Long2ObjectOpenHashMap<>();

	public MapTextureData getDetail(long seed, MazePos pos) {
		var dim = dims.computeIfAbsent(seed, MazeLevelMapSet::new);
		return dim.getDetail(pos);
	}

	public PathTextureData getPath(long seed, MazePos pos) {
		var dim = dims.computeIfAbsent(seed, MazeLevelMapSet::new);
		return dim.getPath(pos);
	}

	public FogTextureData getFog(long seed, MazePos pos) {
		var dim = dims.computeIfAbsent(seed, MazeLevelMapSet::new);
		return dim.getFog(pos);
	}

	@Override
	public void close() {
		for (var e : dims.values())
			e.close();
		dims.clear();
	}

	public static class MazeLevelMapSet implements AutoCloseable {

		private final MazeDimHolder dim;
		private final Long2ObjectMap<MapTextureData> detail = new Long2ObjectOpenHashMap<>();
		private final Long2ObjectMap<PathTextureData> path = new Long2ObjectOpenHashMap<>();
		private final Long2ObjectMap<FogTextureData> fog = new Long2ObjectOpenHashMap<>();

		public MazeLevelMapSet(long seed) {
			this.dim = new MazeDimHolder(seed);
		}

		public MapTextureData getDetail(MazePos pos) {
			return detail.computeIfAbsent(pos.key(), _ -> new MapTextureData(dim, pos));
		}

		public PathTextureData getPath(MazePos pos) {
			return path.computeIfAbsent(pos.key(), _ -> new PathTextureData(pos));
		}

		public FogTextureData getFog(MazePos pos) {
			return fog.computeIfAbsent(pos.key(), _ -> new FogTextureData(pos));
		}

		@Override
		public void close() {
			for (var e : detail.values())
				e.close();
			detail.clear();
		}

	}

	public static class MapTextureData implements AutoCloseable {

		private final MazeDimHolder dim;
		private final DynamicTexture texture;
		private final Vec3i pos;

		public final Identifier id;

		public int w, h;
		public int[][] data;

		private int defeat = -1;

		public MapTextureData(MazeDimHolder dim, MazePos pos) {
			this.dim = dim;
			this.pos = pos.toVec3i();
			w = 25;
			h = 25;
			data = new int[128][128];
			this.texture = new DynamicTexture(() -> "Maze Map " + pos, 128, 128, true);
			this.id = DungeonInfinity.loc("maze_map/" + Long.toUnsignedString(pos.key(), 16));
			Minecraft.getInstance().getTextureManager().register(id, texture);
		}

		public void update(MazeHistory.Visit visit) {
			if (visit.getDefeat() == defeat) return;
			defeat = visit.getDefeat();
			fill(visit);
		}

		public void fill(MazeHistory.Visit visit) {
			NativeImage pixels = this.texture.getPixels();
			int[][] maze = dim.getRegion(pos.getX(), pos.getY(), pos.getZ());
			for (int x = 0; x < 25; x++) {
				for (int z = 0; z < 25; z++) {
					int cell = maze[x][z];
					int[][] px = MazeMapPixelMapper.getPixels(cell, visit.isDefeated(x, z));
					for (int ix = 0; ix < 5; ix++) {
						System.arraycopy(px[ix], 0, data[x * 5 + ix], z * 5, 5);
					}
				}
			}

			data[127][127] = 0xffffffff;

			for (int y = 0; y < 128; y++) {
				for (int x = 0; x < 128; x++) {
					pixels.setPixel(x, y, data[x][y]);
				}
			}

			this.texture.upload();
		}

		@Override
		public void close() {
			texture.close();
		}
	}

	public static class PathTextureData implements AutoCloseable {

		private final DynamicTexture texture;
		private final Vec3i pos;

		public final Identifier id;

		public int w, h;
		public int[][] data;

		private int[] path = new int[0];

		public PathTextureData(MazePos pos) {
			this.pos = pos.toVec3i();
			w = 25;
			h = 25;
			data = new int[128][128];
			this.texture = new DynamicTexture(() -> "Path Map " + pos, 128, 128, true);
			this.id = DungeonInfinity.loc("path_map/" + Long.toUnsignedString(pos.key(), 16));
			Minecraft.getInstance().getTextureManager().register(id, texture);
		}

		public void update(MazeHistory.Visit visit) {
			if (visit.getPath() == path) return;
			path = visit.getPath();
			fill();
		}

		public void fill() {
			data = new int[128][128];
			NativeImage pixels = this.texture.getPixels();
			for (int i = 0; i < path.length - 1; i++) {
				int p0 = path[i];
				int p1 = path[i + 1];
				int x0 = p0 >> 5, z0 = p0 & 31, x1 = p1 >> 5, z1 = p1 & 31;
				x0 = x0 * 5 + 2;
				z0 = z0 * 5 + 2;
				x1 = x1 * 5 + 2;
				z1 = z1 * 5 + 2;
				int px0 = Math.min(x0, x1), px1 = Math.max(x0, x1);
				int pz0 = Math.min(z0, z1), pz1 = Math.max(z0, z1);
				for (int ix = px0; ix <= px1; ix++) {
					for (int iz = pz0; iz <= pz1; iz++) {
						data[ix][iz] = 0xffffaa00;
					}
				}
			}

			for (int y = 0; y < 128; y++) {
				for (int x = 0; x < 128; x++) {
					pixels.setPixel(x, y, data[x][y]);
				}
			}

			this.texture.upload();
		}

		@Override
		public void close() {
			texture.close();
		}
	}

	public static class FogTextureData implements AutoCloseable {

		private final DynamicTexture texture;
		private final Vec3i pos;

		public final Identifier id;

		public int w, h;
		public int[][] data;

		private int revision = -1;

		public FogTextureData(MazePos pos) {
			this.pos = pos.toVec3i();
			w = 25;
			h = 25;
			data = new int[32][32];
			this.texture = new DynamicTexture(() -> "Maze Fog " + pos, 32, 32, true);
			this.id = DungeonInfinity.loc("maze_fog/" + Long.toUnsignedString(pos.key(), 16));
			Minecraft.getInstance().getTextureManager().register(id, texture);
		}

		public void update(MazeHistory.Visit visit) {
			if (visit.getVer() == revision) return;
			revision = visit.getVer();
			NativeImage pixels = this.texture.getPixels();
			for (int y = 0; y < 25; y++) {
				for (int x = 0; x < 25; x++) {
					pixels.setPixel(x, y, visit.isVisible(x, y) ? 0 : 0xff7f7f7f);
				}
			}
			this.texture.upload();
		}

		@Override
		public void close() {
			texture.close();
		}
	}

}
