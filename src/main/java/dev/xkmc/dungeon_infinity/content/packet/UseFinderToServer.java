package dev.xkmc.dungeon_infinity.content.packet;

import dev.xkmc.dungeon_infinity.content.cap.RoomFinder;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record UseFinderToServer(RoomFinder.Type type) implements SerialPacketBase<UseFinderToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		if (data.finder.finder > 0 || player.isCreative()) {
			data.finder.find(sp, data, type);
		}
	}

}
