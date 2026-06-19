package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.content.item.KeyOfAccess;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2core.util.TeleportTool;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

@SerialClass
public class MazeHistory extends PlayerCapabilityTemplate<MazeHistory> {

	public record RespawnData(Identifier dim, BlockPos pos, float yaw, float pitch, boolean forced) {

		public static @Nullable RespawnData of(ServerPlayer.@Nullable RespawnConfig config) {
			if (config == null) return null;
			var data = config.respawnData();
			return new RespawnData(data.dimension().identifier(), data.pos(), data.yaw(), data.pitch(), config.forced());
		}

		public ServerPlayer.RespawnConfig config() {
			return new ServerPlayer.RespawnConfig(new LevelData.RespawnData(new GlobalPos(
					ResourceKey.create(Registries.DIMENSION, dim), pos
			), yaw, pitch), forced);
		}

	}

	@SerialField
	public final Map<Long, Visit> data = new Long2ObjectOpenHashMap<>();

	@SerialField
	public @Nullable BlockPos activeMobRoom = null;

	@SerialField
	public int radius = 1;

	@SerialField
	@Nullable
	public Identifier entryDim;

	@SerialField
	@Nullable
	public Vec3 enterPos;

	@SerialField
	@Nullable
	public RespawnData prevHome;

	public static boolean inMazeDim(Player player) {
		return player.level().dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier());
	}

	public static void markEntry(ServerPlayer sp) {
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		data.entryDim = sp.level().dimension().identifier();
		data.enterPos = sp.position();
		data.intoDim(sp);
	}

	public static void playerReturn(ServerPlayer sp) {
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		var level = data.entryDim == null ? null :
				sp.level().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, data.entryDim));
		data.outOfDim(sp);
		if (level == null || data.enterPos == null) {
			TeleportTool.teleportHome(sp.level(), sp);
		} else {
			var vec = data.enterPos;
			KeyOfAccess.performTeleport(sp, level, vec.x, vec.y, vec.z);
		}
	}

	private void intoDim(ServerPlayer sp) {
		var config = sp.getRespawnConfig();
		if (config == null) return;
		if (!config.respawnData().dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			prevHome = RespawnData.of(config);
			sp.setRespawnPosition(null, false);
		}
	}

	private void outOfDim(ServerPlayer sp) {
		var respawn = sp.getRespawnConfig();
		if (respawn != null) {
			if (respawn.respawnData().dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
				sp.setRespawnPosition(prevHome == null ? null : prevHome.config(), false);
				prevHome = null;
			}
		}
	}

	public void teleportToRoom(ServerPlayer sp, BlockPos fallback) {
		if (activeMobRoom == null) activeMobRoom = fallback;
		var vec = activeMobRoom.getBottomCenter();
		sp.teleportTo(vec.x, vec.y, vec.z);
	}

	@Override
	public void tick(Player player) {
		if (player instanceof ServerPlayer sp) {
			if (inMazeDim(sp)) intoDim(sp);
			else outOfDim(sp);
		}
		if (!inMazeDim(player)) {
			activeMobRoom = null;
			return;
		}
		if (player instanceof ServerPlayer sp && activeMobRoom != null) {
			var sec = MazeRoomData.get(sp.level(), SectionPos.of(activeMobRoom));
			if (sec != null && sec.isActive()) {
				if (sec.getOrCreateActiveMobRoomInstance().contains(sp))
					activeMobRoom = sp.blockPosition();
				else if (!sp.isCreative())
					teleportToRoom(sp, activeMobRoom);
				else activeMobRoom = null;
			} else activeMobRoom = null;
		}
		var pos = MazePos.map(player.blockPosition());
		var ent = data.computeIfAbsent(pos.key(), k -> new Visit());
		if (player instanceof ServerPlayer sp) {
			if (sp.level().getChunkSource().getGenerator() instanceof MazeChunkGenerator maze) {
				var dim = maze.getMaze(sp.level().getChunkSource().randomState());
				int rad = dim.getVisibility(pos) / 2 + 1;
				if (rad != radius) {
					DungeonInfinity.HANDLER.toClientPlayer(new SetRadiusPacket(rad), sp);
				}
				radius = rad;
			}
		}
		ent.visit(pos, radius);
		if (player instanceof ServerPlayer sp) {
			var sec = MazeRoomData.get(sp.level(), SectionPos.of(sp.blockPosition()));
			if (sec != null) {
				sec.tick(ent, pos, sp);
				if (sec.isActive()) {
					activeMobRoom = sp.blockPosition();
				}
			}
		}
	}

	public void defeat(ServerPlayer sp, ArrayList<MazePos> points, SectionRoom holder) {
		for (var mp : points)
			getOrCreate(mp).defeat(mp);
		int total = 0;
		for (var e : data.values())
			total += e.defeat;

	}

	public Visit getOrCreate(MazePos pos) {
		return data.computeIfAbsent(pos.key(), k -> new Visit());
	}

	public void setRadius(int rad) {
		this.radius = rad;
	}

	@SerialClass
	public static class Visit {

		private static final int R = 25, MAX = 80;

		@SerialField
		private final byte[] visibleGrid = new byte[MAX];
		@SerialField
		private final byte[] visitedGrid = new byte[MAX];
		@SerialField
		private final byte[] defeatGrid = new byte[MAX];
		@SerialField
		private int visited = 0, visible = 0, defeat = 0;
		@SerialField
		private int revision = 0;

		public int getVer() {
			return revision;
		}

		public int getDefeat() {
			return defeat;
		}

		public boolean isVisited(int x, int z) {
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			return (visitedGrid[i] & j) != 0;
		}

		public boolean isVisible(int x, int z) {
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			return (visibleGrid[i] & j) != 0;
		}

		public void visit(MazePos pos, int m) {
			int x = pos.px() >> 4;
			int z = pos.pz() >> 4;
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			if ((visitedGrid[i] & j) != 0) return;
			visitedGrid[i] |= j;
			visited++;
			int old = revision;
			markVisible(x - m, z - m, m * 2 + 1, m * 2 + 1);
			revision = old + 1;
		}

		public void markVisible(int x, int z, int w, int h) {
			int old = visible;
			for (int dx = 0; dx < w; dx++) {
				for (int dz = 0; dz < h; dz++) {
					int ix = x + dx;
					int iz = z + dz;
					if (ix < 0 || iz < 0 || ix >= R || iz >= R) continue;
					markVisible(ix, iz);
				}
			}
			if (visible != old)
				revision++;
		}

		private void markVisible(int x, int z) {
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			if ((visibleGrid[i] & j) != 0) return;
			visibleGrid[i] |= j;
			visible++;
		}

		public void defeat(MazePos pos) {
			int x = pos.px() >> 4;
			int z = pos.pz() >> 4;
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			if ((defeatGrid[i] & j) != 0) return;
			defeatGrid[i] |= j;
			defeat++;
			revision++;
		}

		public boolean isDefeated(MazePos pos) {
			int x = pos.px() >> 4;
			int z = pos.pz() >> 4;
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			return (defeatGrid[i] & j) != 0;
		}

		public boolean isDefeated(int x, int z) {
			int index = x * R + z;
			int i = index >> 3;
			int j = 1 << (index & 7);
			return (defeatGrid[i] & j) != 0;
		}
	}

}
