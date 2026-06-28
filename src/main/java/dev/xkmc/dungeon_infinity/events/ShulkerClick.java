package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.shulker.VirtualShulkerMenu;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2menustacker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2menustacker.click.writable.WritableStackClickHandler;
import dev.xkmc.l2menustacker.init.L2MenuStacker;
import dev.xkmc.l2menustacker.screen.base.ScreenTracker;
import dev.xkmc.l2menustacker.screen.packets.CacheMouseToClient;
import dev.xkmc.l2menustacker.screen.source.EnderSource;
import dev.xkmc.l2menustacker.screen.source.InventorySource;
import dev.xkmc.l2menustacker.screen.source.SimpleSlotData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.access.HandlerItemAccess;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public class ShulkerClick extends WritableStackClickHandler {

	public ShulkerClick(Identifier rl) {
		super(rl);
	}

	@Override
	protected void handle(ServerPlayer sp, ClickedPlayerSlotResult slot) {
		var ps = slot.slot();
		var stack = ps.getItem(sp);
		if (!stack.has(DataComponents.CONTAINER)) {
			stack.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
		}
		ItemAccess access;
		if (ps.type() instanceof InventorySource && ps.data() instanceof SimpleSlotData(int index)) {
			access = ItemAccess.forPlayerSlot(sp, index);
		} else if (ps.type() instanceof EnderSource && ps.data() instanceof SimpleSlotData(int index)) {
			var inv = VanillaContainerWrapper.of(sp.getEnderChestInventory());
			access = new HandlerItemAccess(inv, index);
		} else return;

		ScreenTracker.onServerOpen(sp);
		L2MenuStacker.PACKET_HANDLER.toClientPlayer(new CacheMouseToClient(), sp);
		sp.openMenu(new SimpleMenuProvider((wid, inv, pl) -> new VirtualShulkerMenu(DIMeta.SHULKER.get(), wid, inv, ps, access),
				stack.getHoverName()));
	}

	@Override
	public boolean isAllowed(ItemStack stack) {
		return stack.is(ItemTags.SHULKER_BOXES) && stack.getCount() == 1;
	}

}
