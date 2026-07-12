package dev.xkmc.dungeon_infinity.content.screen;

import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.cap.RoomFinder;
import dev.xkmc.dungeon_infinity.content.map.MapUI;
import dev.xkmc.dungeon_infinity.content.map.MazeMapColors;
import dev.xkmc.dungeon_infinity.content.packet.UseFinderToServer;
import dev.xkmc.dungeon_infinity.content.packet.UseWaypointPacket;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class MazeMapScreen extends Screen implements MapUI {

	private final long seed;
	private final boolean canUseWaypoint;

	private int layerY, diffY;

	private final TextButton depthLabel, up, down, findQuad, findStair, findWarehouse, findWorkshop, findShop, configBtn;

	public MazeMapScreen(long seed, boolean canUseWaypoint) {
		super(Component.literal("Maze Map"));
		this.seed = seed;
		this.canUseWaypoint = canUseWaypoint;
		depthLabel = new TextButton();
		up = new TextButton();
		down = new TextButton();
		findQuad = new TextButton();
		findStair = new TextButton();
		findWarehouse = new TextButton();
		findWorkshop = new TextButton();
		findShop = new TextButton();
		configBtn = new TextButton();
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int mx = (int) event.x();
		int my = (int) event.y();
		int x0 = width / 2, y0 = height / 2;
		float rate = Math.min(x0 / 64f, y0 / 64f) / 1.5f;
		int cmx = (int) (((mx - x0) / rate + 63) / 5);
		int cmz = (int) (((my - y0) / rate + 63) / 5);
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			if (canUseWaypoint) {
				var pos = MazePos.map(player.blockPosition());
				var layerY = pos.y();
				pos = pos.atLayer(Mth.clamp(pos.y() + diffY, 0, 15));
				var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
				for (int wp : visit.getAllWaypoints()) {
					int x = wp / 400 % 400;
					int z = wp % 400;
					if (x / 16 == cmx && z / 16 == cmz) {
						DungeonInfinity.HANDLER.toServer(new UseWaypointPacket(pos.at(x, z)));
						diffY = 0;
						return true;
					}
				}
			}
			if (depthLabel.contains(mx, my) && diffY != 0) {
				diffY = 0;
				return true;
			}
			if (up.contains(mx, my)) {
				if (Mth.clamp(layerY + diffY + 1, 0, 15) != diffY + layerY) {
					diffY++;
					return true;
				}
			}
			if (down.contains(mx, my)) {
				if (Mth.clamp(layerY + diffY - 1, 0, 15) != diffY + layerY) {
					diffY--;
					return true;
				}
			}
			if (findQuad.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(RoomFinder.Type.QUAD));
				return true;
			}
			if (findStair.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(RoomFinder.Type.STAIR));
				return true;
			}
			if (findWarehouse.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(RoomFinder.Type.WAREHOUSE));
				return true;
			}
			if (findWorkshop.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(RoomFinder.Type.WORKSHOP));
				return true;
			}
			if (findShop.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(RoomFinder.Type.SHOP));
				return true;
			}
			if (configBtn.contains(mx, my)) {
				Minecraft.getInstance().setScreen(new MapSettingsScreen(seed, canUseWaypoint));
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	int cmx, cmz, mx, my;

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
		super.extractRenderState(g, mx, my, pt);
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		var pos = MazePos.map(player.blockPosition());
		layerY = pos.y();
		var mpy = Mth.clamp(layerY + diffY, 0, 15);
		diffY = mpy - layerY;
		pos = pos.atLayer(mpy);
		int x0 = g.guiWidth() / 2, y0 = g.guiHeight() / 2;
		float rate = Math.min(x0 / 64f, y0 / 64f) / 1.5f;
		cmx = (int) (((mx - x0) / rate + 63) / 5);
		cmz = (int) (((my - y0) / rate + 63) / 5);
		this.mx = mx;
		this.my = my;
		int mapLeft = (int) (x0 - rate * 64);
		int mapRight = (int) (x0 + rate * 64);
		int mapTop = (int) (y0 - rate * 64);
		int mapBottom = (int) (y0 + rate * 64);
		g.fill(mapLeft, mapTop, mapRight, mapBottom, 0xC0161616);
		g.fill(mapLeft, mapTop, mapRight, mapTop + 1, 0xFF4A4A4A);
		g.fill(mapLeft, mapBottom - 1, mapRight, mapBottom, 0xFF2A2A2A);
		g.fill(mapLeft, mapTop, mapLeft + 1, mapBottom, 0xFF4A4A4A);
		g.fill(mapRight - 1, mapTop, mapRight, mapBottom, 0xFF2A2A2A);

		renderMap(player, g, seed, pos, x0, y0, rate, diffY == 0);
		extractLegend(player, g, x0, y0, rate, pos);
		extractBuff(player, g, x0, y0, rate);

		Component configText = DILang.MAP_SETTINGS.get();
		int cfgW = font.width(configText);
		int cfgBtnPX = 6;
		int cfgBtnPY = 3;
		int cfgX = (g.guiWidth() - cfgW) / 2;
		int cfgY = mapBottom + 9;
		configBtn.set(cfgX - cfgBtnPX, cfgY - cfgBtnPY, cfgW + cfgBtnPX * 2, font.lineHeight + cfgBtnPY * 2);
		boolean cfgH = configBtn.contains(mx, my);
		g.fill(cfgX - cfgBtnPX, cfgY - cfgBtnPY, cfgX + cfgW + cfgBtnPX, cfgY + font.lineHeight + cfgBtnPY, cfgH ? 0xFF5A5A5A : 0xFF3D3D3D);
		g.fill(cfgX - cfgBtnPX, cfgY - cfgBtnPY, cfgX + cfgW + cfgBtnPX, cfgY - cfgBtnPY + 1, cfgH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(cfgX - cfgBtnPX, cfgY + font.lineHeight + cfgBtnPY - 1, cfgX + cfgW + cfgBtnPX, cfgY + font.lineHeight + cfgBtnPY, 0xFF2A2A2A);
		g.fill(cfgX - cfgBtnPX, cfgY - cfgBtnPY, cfgX - cfgBtnPX + 1, cfgY + font.lineHeight + cfgBtnPY, cfgH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(cfgX + cfgW + cfgBtnPX - 1, cfgY - cfgBtnPY, cfgX + cfgW + cfgBtnPX, cfgY + font.lineHeight + cfgBtnPY, 0xFF2A2A2A);
		g.text(font, configText, cfgX, cfgY, cfgH ? 0xFFFFAA00 : 0xFFFFFFFF);
	}

	public void extractLegend(Player player, GuiGraphicsExtractor g, int x0, int y0, float rate, MazePos pos) {
		int x1 = (int) (x0 + rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int lh = font.lineHeight;
		int h = lh + 3;
		int panelW = 110;
		int panelX = x1 + 10;
		int panelY = y1 - 10;
		int panelH = Math.min(200, g.guiHeight() - panelY - 10);

		g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xC0161616);
		g.fill(panelX, panelY, panelX + panelW, panelY + 1, 0xFF4A4A4A);
		g.fill(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF2A2A2A);
		g.fill(panelX, panelY, panelX + 1, panelY + panelH, 0xFF4A4A4A);
		g.fill(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF2A2A2A);

		int btnPadX = 4;
		int btnPadY = 1;
		int cx = panelX + 8;
		int cy = panelY + 8;
		int centerX = panelX + panelW / 2;

		Component infoTitle = DILang.INFO_TITLE.get().copy().withStyle(ChatFormatting.BOLD);
		float ts = Math.min(1.1f, (panelW - 16) / (float) font.width(infoTitle));
		g.pose().pushMatrix();
		g.pose().translate(centerX, cy);
		g.pose().scale(ts, ts);
		g.text(font, infoTitle, -font.width(infoTitle) / 2, 0, -1, true);
		g.pose().popMatrix();
		cy += (int) (ts * h);
		g.fill(cx, cy, panelX + panelW - 8, cy + 1, 0xFF4A4A4A);
		cy += 4;

		var data = DIMeta.HISTORY.type().getOrCreate(player);
		var findable = diffY == 0 && (data.finder.finder > 0 || player.isCreative());
		var mag = DILang.MAGNIFIER.get();
		int mw = font.width(mag);

		Component depthText = DILang.DEPTH.get(16 - pos.y());
		g.text(font, depthText, cx, cy, -1);

		int maxDepthW = font.width(DILang.DEPTH.get(16));
		depthLabel.set(cx - btnPadX, cy - btnPadY, maxDepthW + btnPadX * 2, lh + btnPadY * 2);
		int btnY = cy - btnPadY;
		int rightEdge = panelX + panelW - 8;
		Component upText = DILang.UP.get();
		Component downText = DILang.DOWN.get();
		int uw = font.width(upText);
		int dw = font.width(downText);
		int downX = rightEdge - dw - btnPadX;
		int upX = downX - uw - 9;

		boolean upEnabled = pos.y() < 15;
		up.set(upX - btnPadX, btnY, uw + btnPadX * 2, lh + btnPadY * 2);
		boolean uh = upEnabled && up.contains(mx, my);
		int upBg = upEnabled ? (uh ? 0xFF5A5A5A : 0xFF3D3D3D) : 0xFF2A2A2A;
		int upBorder = upEnabled ? (uh ? 0xFF7A7A7A : 0xFF5A5A5A) : 0xFF3D3D3D;
		g.fill(upX - btnPadX, btnY, upX + uw + btnPadX, btnY + lh + btnPadY * 2, upBg);
		g.fill(upX - btnPadX, btnY, upX + uw + btnPadX, btnY + 1, upBorder);
		g.fill(upX - btnPadX, btnY + lh + btnPadY * 2 - 1, upX + uw + btnPadX, btnY + lh + btnPadY * 2, 0xFF2A2A2A);
		g.fill(upX - btnPadX, btnY, upX - btnPadX + 1, btnY + lh + btnPadY * 2, upBorder);
		g.fill(upX + uw + btnPadX - 1, btnY, upX + uw + btnPadX, btnY + lh + btnPadY * 2, 0xFF2A2A2A);
		g.text(font, upText, upX, cy, uh ? 0xFFFFAA00 : upEnabled ? 0xFFFFFFFF : 0xFF606060);
		if (!upEnabled) up.disable();

		boolean downEnabled = pos.y() > 0;
		down.set(downX - btnPadX, btnY, dw + btnPadX * 2, lh + btnPadY * 2);
		boolean dh = downEnabled && down.contains(mx, my);
		int downBg = downEnabled ? (dh ? 0xFF5A5A5A : 0xFF3D3D3D) : 0xFF2A2A2A;
		int downBorder = downEnabled ? (dh ? 0xFF7A7A7A : 0xFF5A5A5A) : 0xFF3D3D3D;
		g.fill(downX - btnPadX, btnY, downX + dw + btnPadX, btnY + lh + btnPadY * 2, downBg);
		g.fill(downX - btnPadX, btnY, downX + dw + btnPadX, btnY + 1, downBorder);
		g.fill(downX - btnPadX, btnY + lh + btnPadY * 2 - 1, downX + dw + btnPadX, btnY + lh + btnPadY * 2, 0xFF2A2A2A);
		g.fill(downX - btnPadX, btnY, downX - btnPadX + 1, btnY + lh + btnPadY * 2, downBorder);
		g.fill(downX + dw + btnPadX - 1, btnY, downX + dw + btnPadX, btnY + lh + btnPadY * 2, 0xFF2A2A2A);
		g.text(font, downText, downX, cy, dh ? 0xFFFFAA00 : downEnabled ? 0xFFFFFFFF : 0xFF606060);
		if (!downEnabled) down.disable();

		cy += h;
		int finderCount = findable ? data.finder.finder : -1;
		Component finderText = DILang.FINDER.get(Math.max(0, finderCount));
		g.text(font, finderText, cx, cy, finderCount >= 0 ? -1 : 0xFF606060);
		cy += h;
		cy += 3;

		renderFitText(g, DILang.ROOM_TYPES.get(), cx, cy, panelW - 16, 0xFFCCCCCC);
		cy += h;

		Component[] legendNames = {DILang.BATTLE.get(), DILang.QUAD.get(), DILang.BOSS.get(),
				DILang.DOWN_STAIR.get(), DILang.UP_STAIR.get(), DILang.WORKSHOP.get(),
				DILang.SHOP.get(), DILang.WAREHOUSE.get()};
		int[] legendColors = {MazeMapColors.F, MazeMapColors.Q, MazeMapColors.R,
				MazeMapColors.G, MazeMapColors.Y, MazeMapColors.K,
				MazeMapColors.S, MazeMapColors.H};
		int[] findOrder = {-1, 0, -1, 1, -1, 2, 3, 4};
		TextButton[] findBtns = {findQuad, findStair, findWorkshop, findShop, findWarehouse};

		float roomScale = 1f;
		for (int idx = 0; idx < legendNames.length; idx++) {
			boolean hasMag = findable && findOrder[idx] >= 0;
			float maxW = hasMag ? (panelW - 28 - mw) : (panelW - 16);
			float s = Math.min(1f, maxW / font.width(legendNames[idx]));
			if (s < roomScale) roomScale = s;
		}

		for (int i = 0; i < legendNames.length; i++) {
			boolean hasMag = findable && findOrder[i] >= 0;
			int magX = hasMag ? panelX + panelW - 12 - mw : 0;
			g.pose().pushMatrix();
			g.pose().translate(cx, cy);
			g.pose().scale(roomScale, roomScale);
			g.text(font, legendNames[i], 0, 0, legendColors[i]);
			g.pose().popMatrix();
			if (hasMag) {
				int fi = findOrder[i];
				TextButton btn = findBtns[fi];
				btn.set(magX - btnPadX, cy - btnPadY, mw + btnPadX * 2, lh + btnPadY * 2);
				boolean mh = btn.contains(mx, my);
				g.fill(magX - btnPadX, cy - btnPadY, magX + mw + btnPadX, cy + lh + btnPadY, mh ? 0xFF5A5A5A : 0xFF3D3D3D);
				g.fill(magX - btnPadX, cy - btnPadY, magX + mw + btnPadX, cy - btnPadY + 1, mh ? 0xFF7A7A7A : 0xFF5A5A5A);
				g.fill(magX - btnPadX, cy + lh + btnPadY - 1, magX + mw + btnPadX, cy + lh + btnPadY, 0xFF2A2A2A);
				g.fill(magX - btnPadX, cy - btnPadY, magX - btnPadX + 1, cy + lh + btnPadY, mh ? 0xFF7A7A7A : 0xFF5A5A5A);
				g.fill(magX + mw + btnPadX - 1, cy - btnPadY, magX + mw + btnPadX, cy + lh + btnPadY, 0xFF2A2A2A);
				g.text(font, mag, magX, cy, mh ? 0xFFFFAA00 : 0xFFFFFFFF);
				if (mh) {
					g.setComponentTooltipForNextFrame(font, List.of(DILang.SEARCH.get()), mx, my);
				}
			}
			cy += h;
		}
	}

	public void extractBuff(Player player, GuiGraphicsExtractor g, int x0, int y0, float rate) {
		int x1 = (int) (x0 - rate * 64);
		var font = getFont();
		int lh = font.lineHeight;
		int h = lh + 3;

		int panelW = 110;
		int panelX = x1 - panelW - 10;
		int panelY = (int) (y0 - rate * 64) - 10;
		int panelH = Math.min(200, g.guiHeight() - panelY - 10);

		g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xC0161616);
		g.fill(panelX, panelY, panelX + panelW, panelY + 1, 0xFF4A4A4A);
		g.fill(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF2A2A2A);
		g.fill(panelX, panelY, panelX + 1, panelY + panelH, 0xFF4A4A4A);
		g.fill(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF2A2A2A);

		int cx = panelX + 8;
		int cy = panelY + 8;
		int centerX = panelX + panelW / 2;

		Component buffTitle = DILang.BUFF_TITLE.get().copy().withStyle(ChatFormatting.BOLD);
		float ts = Math.min(1.1f, (panelW - 16) / (float) font.width(buffTitle));
		g.pose().pushMatrix();
		g.pose().translate(centerX, cy);
		g.pose().scale(ts, ts);
		g.text(font, buffTitle, -font.width(buffTitle) / 2, 0, -1, true);
		g.pose().popMatrix();
		cy += (int) (ts * h);
		g.fill(cx, cy, panelX + panelW - 8, cy + 1, 0xFF4A4A4A);
		cy += 4;

		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		for (var e : data.buffs.entrySet()) {
			var buff = MazeBuff.get(e.getKey());
			var title = buff.getTitle(e.getValue());
			int w = font.width(title);
			g.text(font, title, cx, cy, 0xFFFFAA00);
			if (cx <= mx && mx <= cx + w && cy <= my && my <= cy + h) {
				g.setComponentTooltipForNextFrame(font, buff.getDetail(e.getValue()), mx, my);
			}
			cy += h;
		}
	}

	private void renderFitText(GuiGraphicsExtractor g, Component text, float x, float y, float maxW, int color) {
		float tw = font.width(text);
		float scale = Math.min(1f, maxW / tw);
		g.pose().pushMatrix();
		g.pose().translate(x, y);
		g.pose().scale(scale, scale);
		g.text(font, text, 0, 0, color);
		g.pose().popMatrix();
	}

	public void renderWaypoints(GuiGraphicsExtractor g, MazeHistory.Visit visit) {
		boolean hoverWaypoint = false;
		for (int wp : visit.getAllWaypoints()) {
			int x = wp / 400 % 400;
			int z = wp % 400;
			x = x / 16 * 16 + 8;
			z = z / 16 * 16 + 8;
			int col = MazeMapColors.P;
			g.pose().pushMatrix();
			g.pose().translate(x / 16f * 5f, z / 16f * 5f);
			if (x / 16 == cmx && z / 16 == cmz && canUseWaypoint) {
				g.pose().scale(2, 2);
				hoverWaypoint = true;
			}
			g.submitGuiElementRenderState(Waypoint.of(g, 1, col));
			g.pose().popMatrix();
		}
		if (hoverWaypoint) {
			g.setComponentTooltipForNextFrame(getFont(), List.of(DILang.WAYPOINT.get()), mx, my);
		}
	}

}
