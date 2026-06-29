package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.dungeon_infinity.content.config.ShopConfig;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2itemselector.wheel.PersistentWheel;
import dev.xkmc.l2itemselector.wheel.WheelAdaptor;
import dev.xkmc.l2itemselector.wheel.WheelContext;
import dev.xkmc.l2itemselector.wheel.WheelKeyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record TypeSelWheel(MerchantBlockEntity be) implements PersistentWheel<TypeSelWheel.Entry> {

	@Override
	public boolean isValid(Player player) {
		return player.isAlive() && !be.isRemoved();
	}

	@Override
	public WheelKeyHandler getInputHandler() {
		return new TypeSelKeyHandler();
	}

	@Override
	public List<Entry> getWheelContent() {
		return ShopConfig.getAllTypes().entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue().getDefaultInstance())).toList();
	}

	@Override
	public int getIndex(Player player) {
		return new ArrayList<>(ShopConfig.getAllTypes().keySet()).indexOf(be.type);
	}

	@Override
	public void select(int i) {
		var type = new ArrayList<>(ShopConfig.getAllTypes().keySet()).get(i);
		be.type = type;
		DungeonInfinity.HANDLER.toServer(TypeSelToServer.of(be, type));
	}

	@Override
	public void renderImpl(GuiGraphicsExtractor g, Player player, List<Entry> list, WheelContext ctx) {
		PersistentWheel.super.renderImpl(g, player, list, ctx);
		int index = ctx.hover() >= 0 ? ctx.hover() : ctx.sel();
		if (index < 0) {
			index = this.getIndex(player);
		}

		if (index >= 0) {
			int x0 = g.guiWidth() / 2;
			int y0 = g.guiHeight() / 2;
			float r = Math.min((float) x0 / 1.5F, (float) y0) / 1.5F;
			float s = r * 0.02F;
			g.pose().pushMatrix();
			g.pose().translate((float) x0, (float) y0);
			g.pose().scale(s, s);
			g.pose().translate(0.0F, -8.0F);
			var entry = list.get(index);
			g.item(entry.stack(), -8, -16);
			g.itemDecorations(Minecraft.getInstance().font, entry.stack(), -8, -16);
			g.pose().popMatrix();
			MutableComponent text = Component.translatable(DungeonInfinity.MODID + ".merchant." + entry.key);
			Font font = Minecraft.getInstance().font;
			int y = (int) ((float) y0 + s * 3.0F);

			for (FormattedCharSequence line : font.split(text, (int) r)) {
				g.text(font, line, x0 - font.width(line) / 2, y, -1, true);
				Objects.requireNonNull(font);
				y += 9 + 1;
			}

		}
	}

	@Override
	public void renderIcon(GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v, boolean b1) {

	}

	public record Entry(String key, ItemStack stack) implements WheelAdaptor.Entry {

		public void render(GuiGraphicsExtractor g, float x0, float y0, float ai, float r0, float r, float da, boolean sel) {
			float s = sel ? 1.1F : 1.0F;
			s *= Math.min(r * 0.015F, da * r0 / 16.0F);
			float dx = x0 + Mth.cos(ai) * r0;
			float dy = y0 + Mth.sin(ai) * r0;
			g.pose().pushMatrix();
			g.pose().translate(dx, dy);
			g.pose().scale(s, s);
			g.item(this.stack, -8, -8);
			g.itemDecorations(Minecraft.getInstance().font, this.stack, -8, -8);
			g.pose().popMatrix();
		}

	}

}
