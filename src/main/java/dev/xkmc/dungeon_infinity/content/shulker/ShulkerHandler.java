package dev.xkmc.dungeon_infinity.content.shulker;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ShulkerHandler extends ItemAccessItemHandler {

	public ShulkerHandler(ItemAccess itemAccess, DataComponentType<ItemContainerContents> component, int size) {
		super(itemAccess, component, size);
	}

	public void set(int slot, ItemResource item, int amount) {
		try (Transaction tr = Transaction.open(null)) {
			itemAccess.exchange(update(itemAccess.getResource(), slot, item, amount), 1, tr);
			tr.commit();
		}
	}
}
