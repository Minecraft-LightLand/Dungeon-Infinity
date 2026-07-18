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
			"dungeon_infinity.screen_map.off",
			"dungeon_infinity.screen_map.hold",
			"dungeon_infinity.screen_map.hide",
			"dungeon_infinity.screen_map.always"
	};

	public MapSettingsScreen(long seed, boolean canUseWaypoint) {
		super(Component.literal("Map Settings"));
		this.seed = seed;
		this.canUseWaypoint = canUseWaypoint;
		btnScreenMapMode = new TextButton().fixedWidth(60);
		btnFollowPlayer = new TextButton().fixedWidth(60);
		for (int i = 0; i < 9; i++) posBtns[i] = new TextButton();
		btnScaleUp = new TextButton().fixedWidth(25);
		btnScaleDown = new TextButton().fixedWidth(25);
		btnSizeUp = new TextButton().fixedWidth(25);
		btnSizeDown = new TextButton().fixedWidth(25);
		btnDone = new TextButton().pad(6, 3);
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
		g.text(font, title, -font.width(title) / 2, (int) (font.lineHeight * (1 - ts) / 2 - font.lineHeight / 2), -1, true);
		g.pose().popMatrix();
		cy += (int) (ts * font.lineHeight) + 6;
		g.fill(cx, cy, panelX + panelW - 12, cy + 1, 0xFF4A4A4A);
		cy += 6;

		int btnX = panelX + panelW - 12 - 60;

		g.text(font, DILang.SCREEN_MAP.get(), cx, cy, -1);
		int mode = MapOverlayConfig.screenMapMode;
		btnScreenMapMode.update(g, true, btnX, cy - 2, font, Component.translatable(MODE_KEYS[mode]), mx, my);
		cy += lh + 5;

		g.text(font, DILang.FOLLOW_PLAYER.get(), cx, cy, -1);
		boolean fp = MapOverlayConfig.followPlayer;
		if (fp) {
			btnFollowPlayer.bg(0xFF3D3D3D, 0xFF5A5A5A, 0xFF2A2A2A).border(0xFF5A5A5A, 0xFF7A7A7A, 0xFF3D3D3D);
		} else {
			btnFollowPlayer.bg(0xFF2A2A2A, 0xFF5A5A5A, 0xFF2A2A2A).border(0xFF3D3D3D, 0xFF7A7A7A, 0xFF3D3D3D);
		}
		btnFollowPlayer.update(g, true, btnX, cy - 2, font, fp ? DILang.ON.get() : DILang.OFF.get(), mx, my);
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
		btnScaleDown.update(g, true, btnX, cy - 2, font, Component.literal("-"), mx, my);
		btnScaleUp.update(g, true, btnX + 35, cy - 2, font, Component.literal("+"), mx, my);
		cy += lh + 5;

		Component sizeText = DILang.OVERLAY_SIZE.get(MapOverlayConfig.size);
		g.text(font, sizeText, cx, cy, -1);
		btnSizeDown.update(g, true, btnX, cy - 2, font, Component.literal("-"), mx, my);
		btnSizeUp.update(g, true, btnX + 35, cy - 2, font, Component.literal("+"), mx, my);

		Component doneText = DILang.DONE.get();
		int doneW = btnDone.visualWidth(font, doneText);
		int doneX = (w - doneW) / 2;
		int doneY = panelY + panelH - 20;
		btnDone.update(g, true, doneX, doneY - 3, font, doneText, mx, my);
	}
}
