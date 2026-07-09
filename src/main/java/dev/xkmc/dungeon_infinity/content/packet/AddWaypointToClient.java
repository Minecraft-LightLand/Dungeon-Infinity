package dev.xkmc.dungeon_infinity.content.packet;

import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public record AddWaypointToClient(
		BlockPos pos
) implements SerialPacketBase<AddWaypointToClient> {

	@Override
	public void handle(Player player) {
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		var mp = MazePos.map(pos);
		data.getOrCreate(mp).addWaypoint(mp.px(), pos.getY() % 16, mp.pz());
	}

}
