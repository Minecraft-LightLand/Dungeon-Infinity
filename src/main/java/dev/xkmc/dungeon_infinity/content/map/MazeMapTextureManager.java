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
		private final Long2ObjectMap<FogTextureData> fog = new Long2ObjectOpenHashMap<>();

		public MazeLevelMapSet(long seed) {
			this.dim = new MazeDimHolder(seed);
		}

		public MapTextureData getDetail(MazePos pos) {
			return detail.computeIfAbsent(pos.key(), _ -> new MapTextureData(dim, pos));
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
