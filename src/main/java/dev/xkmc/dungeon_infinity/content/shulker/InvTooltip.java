package dev.xkmc.dungeon_infinity.content.shulker;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.ItemContainerContents;

public record InvTooltip(ItemContainerContents cont, int w, int h) implements TooltipComponent {

}
