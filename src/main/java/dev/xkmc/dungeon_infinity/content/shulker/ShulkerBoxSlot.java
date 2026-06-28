package dev.xkmc.dungeon_infinity.content.shulker;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ShulkerBoxSlot extends ResourceHandlerSlot {

	public ShulkerBoxSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> sm, int slot, int x, int y) {
		super(handler, sm, slot, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.canFitInsideContainerItems() && super.mayPlace(stack);
	}
}
