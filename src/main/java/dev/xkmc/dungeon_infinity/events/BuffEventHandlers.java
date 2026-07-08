package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class BuffEventHandlers {

	@SubscribeEvent
	public static void levelTick(LevelTickEvent.Post event) {

	}

}
