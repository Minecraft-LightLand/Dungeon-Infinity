package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public record AddWaypointPacket(
		BlockPos pos
) implements SerialPacketBase<AddWaypointPacket> {

	@Override
	public void handle(Player player) {
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		var mp = MazePos.map(pos);
		data.getOrCreate(mp).addWaypoint(mp.px(), pos.getY() % 16, mp.pz());
	}

}
