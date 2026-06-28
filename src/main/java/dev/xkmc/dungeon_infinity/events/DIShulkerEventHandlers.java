package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2menustacker.init.L2MSLangData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.contents.TranslatableContents;
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
				event.getToolTip().removeIf(e -> e.getContents() instanceof TranslatableContents tr &&
						(tr.getKey().equals("item.container.item_count") || tr.getKey().equals("item.container.more_items")));
			}
			event.getToolTip().add(L2MSLangData.QUICK_ACCESS.get());
		}
	}

}
