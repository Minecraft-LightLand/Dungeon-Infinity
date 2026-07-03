package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.cap.MobRoomHolder;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MazeRoomBattleStartEvent extends PlayerEvent {

	private final MobRoomHolder ins;

	public MazeRoomBattleStartEvent(Player player, MobRoomHolder ins) {
		super(player);
		this.ins = ins;
	}

	public MobRoomHolder getIns() {
		return ins;
	}
}
