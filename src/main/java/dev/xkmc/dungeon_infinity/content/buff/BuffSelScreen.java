package dev.xkmc.dungeon_infinity.content.buff;

import dev.xkmc.dungeon_infinity.content.cap.packet.SelectBuffToServer;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class BuffSelScreen extends Screen {

	private final boolean large;

	public BuffSelScreen(boolean large) {
		super(Component.literal("Buff Selection"));
		this.large = large;
	}

	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return false;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		var list = large ? data.getLargeBuffList() : data.getSmallBuffList();
		int n = list.size();
		int w = width, h = height;
		int ph = h / 4;
		int lh = font.lineHeight;
		int margin = h / 12;
		int pw = (int) (w / 1.5f);
		int y0 = (h - ph * n - margin * (n - 1)) / 2;
		double mx = event.x();
		double my = event.y();
		for (int i = 0; i < n; i++) {
			int xi = (w - pw) / 2;
			int yi = y0 + (ph + margin) * i;
			boolean hover = mx >= xi && mx <= xi + pw && my >= yi && my <= yi + ph;
			if (hover) {
				if (large) data.largeBuff = 0;
				else data.smallBuff = 0;
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
		int ph = h / 4;
		int lh = font.lineHeight;
		int margin = h / 12;
		int pw = (int) (w / 1.5f);
		int y0 = (h - ph * n - margin * (n - 1)) / 2;
		for (int i = 0; i < n; i++) {
			var e = list.get(i);
			var buff = e.getFirst();
			int xi = (w - pw) / 2;
			int yi = y0 + (ph + margin) * i;
			boolean hover = mx >= xi && mx <= xi + pw && my >= yi && my <= yi + ph;
			g.fill(xi, yi, xi + pw, yi + ph, hover ? 0xffffaa00 : 0xffafafaf);
			g.fill(xi + 5, yi + 5, xi + pw - 5, yi + ph - 5, 0xffafafaf);
			int x1 = xi + pw / 2;
			int y1 = yi + 3;
			var title = buff.getTitle(e.getSecond());
			g.text(font, title, x1 - font.width(title) / 2, y1, -1);
			y1 += (int) (lh * 1.5);
			for (var comp : buff.getDetail(e.getSecond())) {
				y1 += renderText(g, comp, xi, y1, pw);
			}
		}
	}

	private int renderText(GuiGraphicsExtractor g, Component comp, int x, int y, int maxW) {
		var list = font.split(comp, maxW - 10);
		for (int i = 0; i < list.size(); i++) {
			g.text(font, list.get(i), i == 0 ? x + 5 : x + 10, y, -1);
			y += font.lineHeight;
		}
		return list.size() * font.lineHeight;
	}

}
