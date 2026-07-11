package dev.xkmc.dungeon_infinity.util;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.packet.AddWaypointToClient;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.storage.LevelData;

public class RespawnAnchorHelper {

	public static void setSpawn(ServerPlayer sp, BlockPos pos) {
		var state = sp.level().getBlockState(pos);
		if (!state.is(Blocks.RESPAWN_ANCHOR) || !RespawnAnchorBlock.canSetSpawn(sp.level(), pos))
			return;
		ServerPlayer.RespawnConfig prev = sp.getRespawnConfig();
		ServerPlayer.RespawnConfig config = new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(sp.level().dimension(), pos, 0, 0), false);
		if (prev == null || !prev.isSamePosition(config)) {
			sp.setRespawnPosition(config, true);
			sp.level().playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	public static void recharge(ServerPlayer sp) {
		var config = sp.getRespawnConfig();
		if (config == null) return;
		var data = config.respawnData();
		if (!data.dimension().identifier().equals(sp.level().dimension().identifier())) return;
		if (data.pos().distManhattan(sp.blockPosition()) > 8) return;
		var state = sp.level().getBlockState(data.pos());
		if (!state.is(Blocks.RESPAWN_ANCHOR)) return;
		if (state.getValue(RespawnAnchorBlock.CHARGE) >= 4) return;
		RespawnAnchorBlock.charge(sp, sp.level(), data.pos(), state);
	}

	public static void whileInMaze(MazeHistory data, ServerPlayer sp) {
		var config = sp.getRespawnConfig();
		if (config == null) return;
		if (!config.respawnData().dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			sp.setRespawnPosition(null, false);
			data.prevHome = MazeHistory.RespawnData.of(config);
		} else {
			var pos = config.respawnData().pos();
			var mp = MazePos.map(pos);
			var pmp = MazePos.map(sp.blockPosition());
			if (mp.key() == pmp.key()) {
				if (data.getOrCreate(mp).addWaypoint(mp.px(), pos.getY() % 16, mp.pz())) {
					DungeonInfinity.HANDLER.toClientPlayer(new AddWaypointToClient(pos), sp);
				}
			}
		}
	}

	public static void whileOutOfMaze(MazeHistory data, ServerPlayer sp) {
		var respawn = sp.getRespawnConfig();
		if (respawn != null) {
			if (respawn.respawnData().dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
				sp.setRespawnPosition(data.prevHome == null ? null : data.prevHome.config(), false);
				data.prevHome = null;
			}
		}
	}

}
