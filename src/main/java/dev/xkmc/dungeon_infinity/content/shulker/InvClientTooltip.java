package dev.xkmc.dungeon_infinity.content.shulker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public record InvClientTooltip(InvTooltip inv) implements ClientTooltipComponent {

	public static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("container/slot");

	@Override
	public int getHeight(Font font) {
		return inv.h() * 18 + 2;
	}

	@Override
	public int getWidth(Font font) {
		return 18 * inv.w();
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor g) {
		var player = Minecraft.getInstance().player;
		var list = inv.cont();
		for (int i = 0; i < inv.w() * inv.h(); i++) {
			var stack = i >= list.getSlots() ? ItemStack.EMPTY : list.getStackInSlot(i);
			renderSlot(font, x + i % inv.w() * 18,
					y + i / inv.w() * 18, g, stack);
		}
	}

	private void renderSlot(Font font, int x, int y, GuiGraphicsExtractor g, ItemStack stack) {
		this.blit(g, x, y);
		if (!stack.isEmpty()) {
			g.item(stack, x + 1, y + 1, 0);
			g.itemDecorations(font, stack, x + 1, y + 1);
		}
	}

	private void blit(GuiGraphicsExtractor g, int x, int y) {
		g.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, x, y, 18, 18);
	}

}
