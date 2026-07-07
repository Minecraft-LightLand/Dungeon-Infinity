package dev.xkmc.dungeon_infinity.content.cap.packet;

import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record SetRadiusToClient(int rad) implements SerialPacketBase<SetRadiusToClient> {

	@Override
	public void handle(Player player) {
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		data.setRadius(rad);
	}

}
