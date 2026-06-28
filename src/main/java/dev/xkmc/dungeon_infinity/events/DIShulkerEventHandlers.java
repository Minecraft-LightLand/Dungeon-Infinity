package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2menustacker.init.L2MSLangData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = DungeonInfinity.MODID)
public class DIShulkerEventHandlers {

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (event.getItemStack().is(ItemTags.SHULKER_BOXES)) {
			var cont = event.getItemStack().get(DataComponents.CONTAINER);
			if (cont != null && cont.getSlots() > 0) {
				var first = event.getToolTip().getFirst();
				event.getToolTip().clear();
				event.getToolTip().add(first);
			}
			event.getToolTip().add(L2MSLangData.QUICK_ACCESS.get());
		}
	}

}
