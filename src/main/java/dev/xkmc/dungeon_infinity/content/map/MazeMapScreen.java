package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.cap.packet.UseFinderToServer;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class MazeMapScreen extends Screen implements MapUI {

	private final long seed;

	private int layerY, diffY;

	private final FakeBtn up, down, findStair, findShop;

	protected MazeMapScreen(long seed) {
		super(Component.literal("Maze Map"));
		this.seed = seed;
		up = new FakeBtn();
		down = new FakeBtn();
		findStair = new FakeBtn();
		findShop = new FakeBtn();
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
			if (findStair.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(false, true));
				return true;
			}
			if (findShop.contains(mx, my)) {
				DungeonInfinity.HANDLER.toServer(new UseFinderToServer(true, false));
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
		renderMap(player, g, seed, pos, x0, y0, rate, diffY == 0);

		int x1 = (int) (x0 + rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int h = font.lineHeight + 3;

		int bx = x1 + 50;
		int by = y1;
		up.update(g, mpy < 15, bx, by, font, "↑");
		down.update(g, mpy > 0, bx, by + h, font, "↓");
		y1 -= h - 5;

		var data = DIMeta.HISTORY.type().getOrCreate(player);

		g.text(font, DILang.DEPTH.get(16 - pos.y()), x1, y1 += h, -1);
		y1 += h;
		g.text(font, DILang.BATTLE.get(), x1, y1 += h, MazeMapColors.F);
		g.text(font, DILang.QUAD.get(), x1, y1 += h, MazeMapColors.Q);
		g.text(font, DILang.BOSS.get(), x1, y1 += h, MazeMapColors.R);
		g.text(font, DILang.DOWN.get(), x1, y1 += h, MazeMapColors.G);

		findStair.update(g, diffY == 0 && (data.finder.findStair > 0 || player.isCreative()),
				x1 + font.width(DILang.DOWN.get()), y1, font, "\uD83D\uDD0E");

		g.text(font, DILang.UP.get(), x1, y1 += h, MazeMapColors.Y);
		g.text(font, DILang.WORKSHOP.get(), x1, y1 += h, MazeMapColors.K);
		g.text(font, DILang.SHOP.get(), x1, y1 += h, MazeMapColors.S);

		findShop.update(g, diffY == 0 && (data.finder.findShop > 0 || player.isCreative()),
				x1 + font.width(DILang.SHOP.get()), y1, font, "\uD83D\uDD0E");

		g.text(font, DILang.WAREHOUSE.get(), x1, y1 += h, MazeMapColors.H);
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
			if (x / 16 == cmx && z / 16 == cmz) {
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

	private class FakeBtn {

		private boolean enabled;

		private int x, y, w, h;

		public void set(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			enabled = true;
		}

		public void disable() {
			enabled = false;
		}

		public boolean contains(int mx, int my) {
			return enabled && mx >= x && my >= y && mx <= x + w && my <= y + h;
		}

		public void update(GuiGraphicsExtractor g, boolean enable, int x, int y, Font font, String s) {
			if (enable) {
				set(x, y, font.width(s), font.lineHeight);
				g.text(font, s, x, y, contains(mx, my) ? 0xFFFFAA00 : 0xFFFFFFFF);
			} else disable();
		}
	}

}
