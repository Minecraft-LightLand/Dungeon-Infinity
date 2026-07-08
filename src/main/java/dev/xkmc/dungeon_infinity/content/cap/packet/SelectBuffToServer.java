package dev.xkmc.dungeon_infinity.content.cap.packet;

import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record SelectBuffToServer(boolean large, int index) implements SerialPacketBase<SelectBuffToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		if (large) data.selLargeBuff(sp, index);
		else data.selSmallBuff(sp, index);
	}

}
