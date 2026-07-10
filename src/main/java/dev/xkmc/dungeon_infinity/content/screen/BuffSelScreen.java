package dev.xkmc.dungeon_infinity.content.screen;

import dev.xkmc.dungeon_infinity.content.packet.RerollBuffToServer;
import dev.xkmc.dungeon_infinity.content.packet.SelectBuffToServer;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BuffSelScreen extends Screen {

	private final boolean large;

	private int sel = -1;

	private final TextButton reroll;
	private final TextButton confirm;

	public BuffSelScreen(boolean large) {
		super(Component.literal("Buff Selection"));
		this.large = large;
		reroll = new TextButton();
		confirm = new TextButton();
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
			sel = -1;
			return true;
		}
		if (sel >= 0 && confirm.contains(mx, my)) {
			if (large) data.largeBuff--;
			else data.smallBuff--;
			DungeonInfinity.HANDLER.toServer(new SelectBuffToServer(large, sel));
			onClose();
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
				sel = i;
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

		g.fill(0, 0, w, h, 0x80000000);

		float titleScale = 1.5f;
		int py0 = (int) (y0 / 2 - font.lineHeight * titleScale - 4);
		int py1 = y0 + ph + font.lineHeight;
		int panelPad = w / 30;
		int px0 = x0 - panelPad;
		int px1 = x0 + pw * n + margin * (n - 1) + panelPad;
		g.fill(px0, py0, px1, py1, 0xC0161616);
		g.fill(px0, py0, px1, py0 + 1, 0xFF4A4A4A);
		g.fill(px0, py1 - 1, px1, py1, 0xFF2A2A2A);
		g.fill(px0, py0, px0 + 1, py1, 0xFF4A4A4A);
		g.fill(px1 - 1, py0, px1, py1, 0xFF2A2A2A);

		Component screenTitle = DILang.SELECT_TITLE.get().copy().withStyle(ChatFormatting.BOLD);
		g.pose().pushMatrix();
		g.pose().translate(w / 2f, y0 / 2f);
		g.pose().scale(titleScale, titleScale);
		g.text(font, screenTitle, -font.width(screenTitle) / 2, 0, 0xFFFED83D, true);
		g.pose().popMatrix();

		int btnPadX = 10;
		int btnPadY = 5;
		int cardRight = x0 + pw * n + margin * (n - 1);
		int cardCenterX = (x0 + cardRight) / 2;
		int btnGap = 80;

		Component rerollBtnText = DILang.REFRESH.get();
		int rtw = font.width(rerollBtnText);
		int rbx = cardCenterX - btnGap - rtw / 2;
		int rby = h - y0 / 2;
		if (data.rerollChance > 0) {
			reroll.set(rbx - btnPadX, rby - btnPadY, rtw + btnPadX * 2, font.lineHeight + btnPadY * 2);
			boolean rh = reroll.contains(mx, my);
			g.fill(rbx - btnPadX, rby - btnPadY, rbx + rtw + btnPadX, rby + font.lineHeight + btnPadY, rh ? 0xFF5A5A5A : 0xFF3D3D3D);
			g.fill(rbx - btnPadX, rby - btnPadY, rbx + rtw + btnPadX, rby - btnPadY + 1, rh ? 0xFF7A7A7A : 0xFF5A5A5A);
			g.fill(rbx - btnPadX, rby + font.lineHeight + btnPadY - 1, rbx + rtw + btnPadX, rby + font.lineHeight + btnPadY, 0xFF2A2A2A);
			g.fill(rbx - btnPadX, rby - btnPadY, rbx - btnPadX + 1, rby + font.lineHeight + btnPadY, 0xFF5A5A5A);
			g.fill(rbx + rtw + btnPadX - 1, rby - btnPadY, rbx + rtw + btnPadX, rby + font.lineHeight + btnPadY, 0xFF2A2A2A);
			g.text(font, rerollBtnText, rbx, rby, rh ? 0xFFFFAA00 : 0xFFFFFFFF);
			if (rh) {
				g.setComponentTooltipForNextFrame(font, List.of(DILang.REMAIN.get(data.rerollChance)), mx, my);
			}
		} else {
			reroll.disable();
		}

		Component confirmText = DILang.CONFIRM.get();
		int ctw = font.width(confirmText);
		int cbx = cardCenterX + btnGap - ctw / 2;
		int cby = h - y0 / 2;
		if (sel >= 0) {
			confirm.set(cbx - btnPadX, cby - btnPadY, ctw + btnPadX * 2, font.lineHeight + btnPadY * 2);
			boolean ch = confirm.contains(mx, my);
			g.fill(cbx - btnPadX, cby - btnPadY, cbx + ctw + btnPadX, cby + font.lineHeight + btnPadY, ch ? 0xFF5A5A5A : 0xFF3D3D3D);
			g.fill(cbx - btnPadX, cby - btnPadY, cbx + ctw + btnPadX, cby - btnPadY + 1, ch ? 0xFF7A7A7A : 0xFF5A5A5A);
			g.fill(cbx - btnPadX, cby + font.lineHeight + btnPadY - 1, cbx + ctw + btnPadX, cby + font.lineHeight + btnPadY, 0xFF2A2A2A);
			g.fill(cbx - btnPadX, cby - btnPadY, cbx - btnPadX + 1, cby + font.lineHeight + btnPadY, 0xFF5A5A5A);
			g.fill(cbx + ctw + btnPadX - 1, cby - btnPadY, cbx + ctw + btnPadX, cby + font.lineHeight + btnPadY, 0xFF2A2A2A);
			g.text(font, confirmText, cbx, cby, ch ? 0xFFFFAA00 : 0xFFFFFFFF);
		} else {
			g.fill(cbx - btnPadX, cby - btnPadY, cbx + ctw + btnPadX, cby + font.lineHeight + btnPadY, 0xFF2A2A2A);
			confirm.set(cbx - btnPadX, cby - btnPadY, ctw + btnPadX * 2, font.lineHeight + btnPadY * 2);
			g.text(font, confirmText, cbx, cby, 0xFF808080);
		}

		for (int i = 0; i < n; i++) {
			var e = list.get(i);
			var buff = e.getFirst();
			int xi = x0 + (pw + margin) * i;
			boolean hover = mx >= xi && mx <= xi + pw && my >= y0 && my <= y0 + ph;

			g.fill(xi + 2, y0 + 2, xi + pw + 2, y0 + ph + 2, 0x40000000);
			g.fill(xi, y0, xi + pw, y0 + ph, 0xFF2D2D2D);
			g.fill(xi, y0, xi + pw, y0 + 1, 0xFF4A4A4A);

			if (sel == i || hover) {
				int col = hover ? 0xFFFFAA00 : 0xFFFFFFFF;
				g.fill(xi - 2, y0 - 2, xi + pw + 2, y0 - 1, col);
				g.fill(xi - 2, y0 + ph + 1, xi + pw + 2, y0 + ph + 2, col);
				g.fill(xi - 2, y0 - 2, xi - 1, y0 + ph + 2, col);
				g.fill(xi + pw + 1, y0 - 2, xi + pw + 2, y0 + ph + 2, col);
			}

			int x1 = xi + pw / 2;
			float y1 = y0 + 5;
			var title = buff.getTitle(e.getSecond()).copy().withStyle(ChatFormatting.BOLD);
			y1 = textCenter(g, title, x1, y1, 1, -1);
			y1 += 3;
			g.fill(xi + 10, (int) y1, xi + pw - 10, (int) y1 + 1, 0xFF4A4A4A);
			y1 += 4;
			for (var comp : buff.getDetail(e.getSecond())) {
				y1 = renderText(g, comp, xi, y1, 1, pw);
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
		for (FormattedCharSequence formattedCharSequence : list) {
			y = textLeft(g, formattedCharSequence, x + 5, y, rate, -1);
		}
		return y;
	}

}
