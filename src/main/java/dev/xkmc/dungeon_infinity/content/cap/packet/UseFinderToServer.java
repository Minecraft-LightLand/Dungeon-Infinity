package dev.xkmc.dungeon_infinity.content.cap.packet;

import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record UseFinderToServer(boolean shop, boolean stair) implements SerialPacketBase<UseFinderToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		if (shop && (data.finder.findShop > 0 || player.isCreative())) {
			data.finder.findShop(sp, data);
		} else if (stair && (data.finder.findStair > 0 || player.isCreative())) {
			data.finder.findStair(sp, data);
		}
	}

}
