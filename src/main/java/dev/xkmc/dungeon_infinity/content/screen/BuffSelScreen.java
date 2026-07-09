package dev.xkmc.dungeon_infinity.content.screen;

import dev.xkmc.dungeon_infinity.content.packet.RerollBuffToServer;
import dev.xkmc.dungeon_infinity.content.packet.SelectBuffToServer;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;

public class BuffSelScreen extends Screen {

	private final boolean large;

	private final TextButton reroll;

	public BuffSelScreen(boolean large) {
		super(Component.literal("Buff Selection"));
		this.large = large;
		reroll = new TextButton();
	}

	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return false;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;

		double mx = event.x();
		double my = event.y();

		if (reroll.contains(mx, my)) {
			data.reroll();
			DungeonInfinity.HANDLER.toServer(new RerollBuffToServer(1));
			return true;
		}

		var list = large ? data.getLargeBuffList() : data.getSmallBuffList();
		int n = list.size();
		int w = width, h = height;
		int pw = w / 4;
		int margin = w / 12;
		int ph = (int) (h / 1.5f);
		int x0 = (w - pw * n - margin * (n - 1)) / 2;
		for (int i = 0; i < n; i++) {
			int yi = (h - ph) / 2;
			int xi = x0 + (pw + margin) * i;
			boolean hover = mx >= xi && mx <= xi + pw && my >= yi && my <= yi + ph;
			if (hover) {
				if (large) data.largeBuff--;
				else data.smallBuff--;
				DungeonInfinity.HANDLER.toServer(new SelectBuffToServer(large, i));
				onClose();
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		var list = large ? data.getLargeBuffList() : data.getSmallBuffList();
		int n = list.size();
		int w = g.guiWidth(), h = g.guiHeight();
		int pw = w / 4;
		int margin = w / 12;
		int ph = (int) (h / 1.5f);
		int x0 = (w - pw * n - margin * (n - 1)) / 2;
		int y0 = (h - ph) / 2;
		Component text = DILang.REROLL.get(data.rerollChance);
		reroll.update(g, data.rerollChance > 0, w / 2 - font.width(text) / 2, y0 / 2, font, text, mx, my);
		int requiredWidth = 600;
		float rate = w < requiredWidth ? 1f * w / requiredWidth : 1;
		for (int i = 0; i < n; i++) {
			var e = list.get(i);
			var buff = e.getFirst();
			int xi = x0 + (pw + margin) * i;
			boolean hover = mx >= xi && mx <= xi + pw && my >= y0 && my <= y0 + ph;
			if (hover)
				g.fill(xi - 2, y0 - 2, xi + pw + 2, y0 + ph + 2, 0xffffaa00);
			g.fill(xi, y0, xi + pw, y0 + ph, 0xffafafaf);
			int x1 = xi + pw / 2;
			float y1 = y0 + 3;
			var title = buff.getTitle(e.getSecond());
			y1 = textCenter(g, title, x1, y1, rate, -1);
			y1 += rate * font.lineHeight;
			for (var comp : buff.getDetail(e.getSecond())) {
				y1 = renderText(g, comp, xi, y1, rate, pw);
			}
		}
	}

	private float textCenter(GuiGraphicsExtractor g, Component comp, float x, float y, float rate, int col) {
		g.pose().pushMatrix();
		g.pose().translate(x, y);
		g.pose().scale(rate, rate);
		g.text(font, comp, -font.width(comp) / 2, 0, -1);
		g.pose().popMatrix();
		return y + rate * font.lineHeight;
	}

	private float textLeft(GuiGraphicsExtractor g, FormattedCharSequence comp, float x, float y, float rate, int col) {
		g.pose().pushMatrix();
		g.pose().translate(x, y);
		g.pose().scale(rate, rate);
		g.text(font, comp, 0, 0, -1);
		g.pose().popMatrix();
		return y + rate * font.lineHeight;
	}

	private float renderText(GuiGraphicsExtractor g, Component comp, int x, float y, float rate, int maxW) {
		var list = font.split(comp, (int) ((maxW - 10) / rate));
		for (int i = 0; i < list.size(); i++) {
			y = textLeft(g, list.get(i), x + 5, y, rate, -1);
		}
		return y;
	}

}
