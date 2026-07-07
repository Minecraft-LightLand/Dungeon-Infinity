package dev.xkmc.dungeon_infinity.content.cap.packet;

import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public record DefeatRoomToClient(
		ArrayList<MazePos> list, boolean clearAll
) implements SerialPacketBase<DefeatRoomToClient> {

	@Override
	public void handle(Player player) {
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		for (var e : list) {
			data.getOrCreate(e).defeat(e);
		}
		if (list.isEmpty() || !clearAll) return;
		data.getOrCreate(list.getFirst()).markVisible(0, 0, 25, 25);
	}

}
