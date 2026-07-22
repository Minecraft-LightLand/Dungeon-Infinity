package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.buff.core.AllBuffs;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.TriState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Repairable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class BuffEventHandlers {

	@SubscribeEvent
	public static void pickup(ItemEntityPickupEvent.Pre event) {
		var player = event.getPlayer();
		if (!MazeHistory.inMazeDim(player)) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player);
		if (data.buff.buffs.getOrDefault(AllBuffs.HEARTHEART.id, 0) <= 0) return;
		var stack = event.getItemEntity().getItem();
		if (stack.has(DataComponents.UNBREAKABLE)) return;
		Repairable repairable = stack.get(DataComponents.REPAIRABLE);
		if (repairable == null || repairable.items().size() == 0) return;
		event.getItemEntity().setItem(new ItemStack(repairable.items().get(0)));
		event.setCanPickup(TriState.FALSE);
	}

}
