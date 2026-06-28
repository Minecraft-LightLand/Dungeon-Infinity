package dev.xkmc.dungeon_infinity.content.shulker;

import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public class VirtualShulkerMenu extends AbstractContainerMenu {

	public static VirtualShulkerMenu fromNetwork(MenuType<VirtualShulkerMenu> type, int containerId, Inventory inventory) {
		var handler = new ItemStacksResourceHandler(CONTAINER_SIZE);
		return new VirtualShulkerMenu(type, containerId, inventory, null, handler, handler::set);
	}

	private static final int CONTAINER_SIZE = 27;

	private final @Nullable ItemStack focus;
	private final @Nullable PlayerSlot<?> playerSlot;
	private final ResourceHandler<ItemResource> container;


	public VirtualShulkerMenu(MenuType<VirtualShulkerMenu> type, int containerId, Inventory inventory, PlayerSlot<?> slot, ItemAccess access) {
		var handler = new ShulkerHandler(access, DataComponents.CONTAINER, CONTAINER_SIZE);
		this(type, containerId, inventory, slot, handler, handler::set);
	}

	public VirtualShulkerMenu(MenuType<VirtualShulkerMenu> type, int containerId, Inventory inventory, @Nullable PlayerSlot<?> slot, ResourceHandler<ItemResource> container, IndexModifier<ItemResource> modifier) {
		super(type, containerId);
		focus = slot == null ? null : slot.getItem(inventory.player);
		this.playerSlot = slot;
		this.container = container;
		int rows = 3;
		int columns = 9;

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				this.addSlot(new ShulkerBoxSlot(container, modifier, x + y * columns, 8 + x * 18, 18 + y * 18));
			}
		}

		this.addStandardInventorySlots(inventory, 8, 84);
	}

	@Override
	public boolean stillValid(Player player) {
		return player.isAlive() && (focus == null || playerSlot != null && focus == playerSlot.getItem(player));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack clicked = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			clicked = stack.copy();
			if (slotIndex < this.container.size()) {
				if (!this.moveItemStackTo(stack, this.container.size(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stack, 0, this.container.size(), false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return clicked;
	}

}
