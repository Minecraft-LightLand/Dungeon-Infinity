package dev.xkmc.dungeon_infinity.content.cap.packet;

import dev.xkmc.dungeon_infinity.content.cap.MazeBuffData;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record SyncBuffToClient(MazeBuffData data) implements SerialPacketBase<SyncBuffToClient> {

	@Override
	public void handle(Player player) {
		DIMeta.HISTORY.type().getOrCreate(player).buff = data;
	}

}
