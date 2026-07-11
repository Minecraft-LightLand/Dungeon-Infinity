package dev.xkmc.dungeon_infinity.content.screen;

import dev.xkmc.dungeon_infinity.content.map.MapOverlayConfig;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class MapSettingsScreen extends Screen {

	private final TextButton btnScreenMapMode, btnFollowPlayer;
	private final TextButton[] posBtns = new TextButton[9];
	private final TextButton btnScaleUp, btnScaleDown;
	private final TextButton btnSizeUp, btnSizeDown;
	private final TextButton btnDone;

	private int mx, my;

	private final long seed;
	private final boolean canUseWaypoint;

	private static final String[] MODE_KEYS = {
			"modulargolems.screen_map.off",
			"modulargolems.screen_map.hold",
			"modulargolems.screen_map.hide",
			"modulargolems.screen_map.always"
	};

	public MapSettingsScreen(long seed, boolean canUseWaypoint) {
		super(Component.literal("Map Settings"));
		this.seed = seed;
		this.canUseWaypoint = canUseWaypoint;
		btnScreenMapMode = new TextButton();
		btnFollowPlayer = new TextButton();
		for (int i = 0; i < 9; i++) posBtns[i] = new TextButton();
		btnScaleUp = new TextButton();
		btnScaleDown = new TextButton();
		btnSizeUp = new TextButton();
		btnSizeDown = new TextButton();
		btnDone = new TextButton();
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int x = (int) event.x();
		int y = (int) event.y();
		if (btnDone.contains(x, y)) {
			Minecraft.getInstance().setScreen(new MazeMapScreen(seed, canUseWaypoint));
			return true;
		}
		if (btnScreenMapMode.contains(x, y)) {
			MapOverlayConfig.screenMapMode = (MapOverlayConfig.screenMapMode + 1) % 4;
			return true;
		}
		if (btnFollowPlayer.contains(x, y)) {
			MapOverlayConfig.followPlayer = !MapOverlayConfig.followPlayer;
			return true;
		}
		for (int i = 0; i < 9; i++) {
			if (posBtns[i].contains(x, y)) {
				MapOverlayConfig.position = i;
				return true;
			}
		}
		if (btnScaleUp.contains(x, y)) {
			MapOverlayConfig.scale = Math.min(3f, MapOverlayConfig.scale + 0.25f);
			return true;
		}
		if (btnScaleDown.contains(x, y)) {
			MapOverlayConfig.scale = Math.max(0.25f, MapOverlayConfig.scale - 0.25f);
			return true;
		}
		if (btnSizeUp.contains(x, y)) {
			MapOverlayConfig.size = Math.min(200, MapOverlayConfig.size + 10);
			return true;
		}
		if (btnSizeDown.contains(x, y)) {
			MapOverlayConfig.size = Math.max(30, MapOverlayConfig.size - 10);
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
		this.mx = mx;
		this.my = my;
		var font = getFont();
		int w = g.guiWidth();
		int h = g.guiHeight();
		int lh = font.lineHeight;
		int panelW = Math.min(280, w - 40);
		int panelX = (w - panelW) / 2;
		int panelY = 20;
		int panelH = h - 40;
		int btnPX = 4;
		int btnPY = 2;

		g.fill(0, 0, w, h, 0x80000000);
		g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xC0161616);
		g.fill(panelX, panelY, panelX + panelW, panelY + 1, 0xFF4A4A4A);
		g.fill(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF2A2A2A);
		g.fill(panelX, panelY, panelX + 1, panelY + panelH, 0xFF4A4A4A);
		g.fill(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF2A2A2A);

		int cx = panelX + 12;
		int cy = panelY + 12;

		Component title = DILang.MAP_SETTINGS.get().copy().withStyle(ChatFormatting.BOLD);
		g.pose().pushMatrix();
		float ts = 1.3f;
		g.pose().translate(w / 2f, cy + ts * font.lineHeight / 2f);
		g.pose().scale(ts, ts);
		g.text(font, title, -font.width(title) / 2, -font.lineHeight / 2, -1, true);
		g.pose().popMatrix();
		cy += (int) (ts * font.lineHeight) + 6;
		g.fill(cx, cy, panelX + panelW - 12, cy + 1, 0xFF4A4A4A);
		cy += 6;

		int btnX = panelX + panelW - 12 - 60;

		g.text(font, DILang.SCREEN_MAP.get(), cx, cy, -1);
		int mode = MapOverlayConfig.screenMapMode;
		Component modeName = Component.translatable(MODE_KEYS[mode]);
		int modeW = font.width(modeName);
		btnScreenMapMode.set(btnX, cy - btnPY, 60, lh + btnPY * 2);
		boolean smH = btnScreenMapMode.contains(mx, my);
		g.fill(btnX, cy - btnPY, btnX + 60, cy + lh + btnPY, smH ? 0xFF5A5A5A : 0xFF3D3D3D);
		g.fill(btnX, cy - btnPY, btnX + 60, cy - btnPY + 1, smH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(btnX, cy + lh + btnPY - 1, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX, cy - btnPY, btnX + 1, cy + lh + btnPY, smH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(btnX + 59, cy - btnPY, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, modeName, btnX + (60 - modeW) / 2, cy, smH ? 0xFFFFAA00 : -1);
		cy += lh + 5;

		g.text(font, DILang.FOLLOW_PLAYER.get(), cx, cy, -1);
		btnFollowPlayer.set(btnX, cy - btnPY, 60, lh + btnPY * 2);
		boolean fp = MapOverlayConfig.followPlayer;
		boolean fpH = btnFollowPlayer.contains(mx, my);
		int fpBg = fpH ? 0xFF5A5A5A : (fp ? 0xFF3D3D3D : 0xFF2A2A2A);
		int fpBorder = fpH ? 0xFF7A7A7A : (fp ? 0xFF5A5A5A : 0xFF3D3D3D);
		g.fill(btnX, cy - btnPY, btnX + 60, cy + lh + btnPY, fpBg);
		g.fill(btnX, cy - btnPY, btnX + 60, cy - btnPY + 1, fpBorder);
		g.fill(btnX, cy + lh + btnPY - 1, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX, cy - btnPY, btnX + 1, cy + lh + btnPY, fpBorder);
		g.fill(btnX + 59, cy - btnPY, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, fp ? DILang.ON.get() : DILang.OFF.get(), btnX + (60 - font.width(fp ? DILang.ON.get() : DILang.OFF.get())) / 2, cy, fpH ? 0xFFFFAA00 : -1);
		cy += lh + 10;

		Component posTitle = DILang.POSITION.get();
		g.text(font, posTitle, cx, cy, -1);
		cy += lh + 3;

		int gridSize = Math.min(12, (panelW - 40) / 3);
		int gridGap = 2;
		int gridStartX = cx + (panelW - 24 - gridSize * 3 - gridGap * 2) / 2;
		for (int i = 0; i < 9; i++) {
			int gx = i % 3;
			int gy = i / 3;
			int bx = gridStartX + gx * (gridSize + gridGap);
			int by = cy + gy * (gridSize + gridGap);
			posBtns[i].set(bx, by, gridSize, gridSize);
			boolean hover = posBtns[i].contains(mx, my);
			boolean selected = MapOverlayConfig.position == i;
			int bg = selected ? 0xFF5A5A5A : (hover ? 0xFF3D3D3D : 0xFF2A2A2A);
			g.fill(bx, by, bx + gridSize, by + gridSize, bg);
			if (selected) {
				g.fill(bx, by, bx + gridSize, by + 1, 0xFF7A7A7A);
				g.fill(bx, by, bx + 1, by + gridSize, 0xFF7A7A7A);
				g.fill(bx, by + gridSize - 1, bx + gridSize, by + gridSize, 0xFF2A2A2A);
				g.fill(bx + gridSize - 1, by, bx + gridSize, by + gridSize, 0xFF2A2A2A);
			}
		}
		cy += 3 * (gridSize + gridGap) + 8;

		Component scaleText = DILang.OVERLAY_SCALE.get(Math.round(MapOverlayConfig.scale * 100));
		g.text(font, scaleText, cx, cy, -1);
		btnScaleDown.set(btnX, cy - btnPY, 25, lh + btnPY * 2);
		btnScaleUp.set(btnX + 35, cy - btnPY, 25, lh + btnPY * 2);
		boolean scH = btnScaleDown.contains(mx, my);
		int scBg = scH ? 0xFF5A5A5A : 0xFF3D3D3D;
		int scBorder = scH ? 0xFF7A7A7A : 0xFF5A5A5A;
		g.fill(btnX, cy - btnPY, btnX + 25, cy + lh + btnPY, scBg);
		g.fill(btnX, cy - btnPY, btnX + 25, cy - btnPY + 1, scBorder);
		g.fill(btnX, cy + lh + btnPY - 1, btnX + 25, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX, cy - btnPY, btnX + 1, cy + lh + btnPY, scBorder);
		g.fill(btnX + 24, cy - btnPY, btnX + 25, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, Component.literal("-"), btnX + 10, cy + 1, scH ? 0xFFFFAA00 : -1);
		boolean scH2 = btnScaleUp.contains(mx, my);
		int scBg2 = scH2 ? 0xFF5A5A5A : 0xFF3D3D3D;
		int scBorder2 = scH2 ? 0xFF7A7A7A : 0xFF5A5A5A;
		g.fill(btnX + 35, cy - btnPY, btnX + 60, cy + lh + btnPY, scBg2);
		g.fill(btnX + 35, cy - btnPY, btnX + 60, cy - btnPY + 1, scBorder2);
		g.fill(btnX + 35, cy + lh + btnPY - 1, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX + 35, cy - btnPY, btnX + 36, cy + lh + btnPY, scBorder2);
		g.fill(btnX + 59, cy - btnPY, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, Component.literal("+"), btnX + 45, cy + 1, scH2 ? 0xFFFFAA00 : -1);
		cy += lh + 5;

		Component sizeText = DILang.OVERLAY_SIZE.get(MapOverlayConfig.size);
		g.text(font, sizeText, cx, cy, -1);
		btnSizeDown.set(btnX, cy - btnPY, 25, lh + btnPY * 2);
		btnSizeUp.set(btnX + 35, cy - btnPY, 25, lh + btnPY * 2);
		boolean szH = btnSizeDown.contains(mx, my);
		int szBg = szH ? 0xFF5A5A5A : 0xFF3D3D3D;
		int szBorder = szH ? 0xFF7A7A7A : 0xFF5A5A5A;
		g.fill(btnX, cy - btnPY, btnX + 25, cy + lh + btnPY, szBg);
		g.fill(btnX, cy - btnPY, btnX + 25, cy - btnPY + 1, szBorder);
		g.fill(btnX, cy + lh + btnPY - 1, btnX + 25, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX, cy - btnPY, btnX + 1, cy + lh + btnPY, szBorder);
		g.fill(btnX + 24, cy - btnPY, btnX + 25, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, Component.literal("-"), btnX + 10, cy + 1, szH ? 0xFFFFAA00 : -1);
		boolean szH2 = btnSizeUp.contains(mx, my);
		int szBg2 = szH2 ? 0xFF5A5A5A : 0xFF3D3D3D;
		int szBorder2 = szH2 ? 0xFF7A7A7A : 0xFF5A5A5A;
		g.fill(btnX + 35, cy - btnPY, btnX + 60, cy + lh + btnPY, szBg2);
		g.fill(btnX + 35, cy - btnPY, btnX + 60, cy - btnPY + 1, szBorder2);
		g.fill(btnX + 35, cy + lh + btnPY - 1, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.fill(btnX + 35, cy - btnPY, btnX + 36, cy + lh + btnPY, szBorder2);
		g.fill(btnX + 59, cy - btnPY, btnX + 60, cy + lh + btnPY, 0xFF2A2A2A);
		g.text(font, Component.literal("+"), btnX + 45, cy + 1, szH2 ? 0xFFFFAA00 : -1);

		Component doneText = DILang.DONE.get();
		int doneW = font.width(doneText);
		int donePX = 6, donePY = 3;
		int doneX = (w - doneW) / 2;
		int doneY = panelY + panelH - 20;
		btnDone.set(doneX - donePX, doneY - donePY, doneW + donePX * 2, lh + donePY * 2);
		boolean doneH = btnDone.contains(mx, my);
		g.fill(doneX - donePX, doneY - donePY, doneX + doneW + donePX, doneY + lh + donePY, doneH ? 0xFF5A5A5A : 0xFF3D3D3D);
		g.fill(doneX - donePX, doneY - donePY, doneX + doneW + donePX, doneY - donePY + 1, doneH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(doneX - donePX, doneY + lh + donePY - 1, doneX + doneW + donePX, doneY + lh + donePY, 0xFF2A2A2A);
		g.fill(doneX - donePX, doneY - donePY, doneX - donePX + 1, doneY + lh + donePY, doneH ? 0xFF7A7A7A : 0xFF5A5A5A);
		g.fill(doneX + doneW + donePX - 1, doneY - donePY, doneX + doneW + donePX, doneY + lh + donePY, 0xFF2A2A2A);
		g.text(font, doneText, doneX, doneY, doneH ? 0xFFFFAA00 : -1);
	}
}
