package dev.xkmc.dungeon_infinity.content.packet;

import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record RerollBuffToServer(int reroll) implements SerialPacketBase<RerollBuffToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		for (int i = 0; i < reroll; i++) {
			data.reroll();
		}
	}

}
