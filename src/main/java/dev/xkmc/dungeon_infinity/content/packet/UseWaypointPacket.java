package dev.xkmc.dungeon_infinity.content.packet;

import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.dungeon_infinity.util.RespawnAnchorHelper;
import dev.xkmc.l2core.util.TeleportTool;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record UseWaypointPacket(MazePos pos) implements SerialPacketBase<UseWaypointPacket> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
		for (var e : visit.getAllWaypoints()) {
			int y = e / 160000;
			int x = e / 400 % 400;
			int z = e % 400;
			if (pos.px() / 16 == x / 16 && pos.pz() / 16 == z / 16) {
				var p = pos.toPos(y + 1).getBottomCenter();
				TeleportTool.performTeleport(sp, sp.level(), p.x, p.y, p.z, sp.getYRot(), sp.getXRot());
				RespawnAnchorHelper.setSpawn(sp, pos.toPos(y));
				return;
			}
		}
	}

}
