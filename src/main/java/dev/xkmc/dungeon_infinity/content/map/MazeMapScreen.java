package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
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

	private int layerY, diffY;

	private int btnUpX, btnUpY, btnDownX, btnDownY;

	protected MazeMapScreen(long seed) {
		super(Component.literal("Maze Map"));
		this.seed = seed;
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
			var font = getFont();
			if (mx >= btnUpX && mx <= btnUpX + font.width("↑") && my >= btnUpY && my <= btnUpY + font.lineHeight) {
				if (Mth.clamp(layerY + diffY + 1, 0, 15) != diffY + layerY) {
					diffY++;
					return true;
				}
			}
			if (mx >= btnDownX && mx <= btnDownX + font.width("↓") && my >= btnDownY && my <= btnDownY + font.lineHeight) {
				if (Mth.clamp(layerY + diffY - 1, 0, 15) != diffY + layerY) {
					diffY--;
					return true;
				}
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
		btnUpX = bx;
		btnUpY = by;
		btnDownX = bx;
		btnDownY = by + h;
		g.text(font, "↑", bx, by, -1);
		g.text(font, "↓", bx, by + h, -1);

		y1 -= h - 5;

		g.text(font, DILang.DEPTH.get(16 - pos.y()), x1, y1 += h, -1);
		y1 += h;
		g.text(font, DILang.BATTLE.get(), x1, y1 += h, MazeMapColors.F);
		g.text(font, DILang.QUAD.get(), x1, y1 += h, MazeMapColors.Q);
		g.text(font, DILang.BOSS.get(), x1, y1 += h, MazeMapColors.R);
		g.text(font, DILang.DOWN.get(), x1, y1 += h, MazeMapColors.G);
		g.text(font, DILang.UP.get(), x1, y1 += h, MazeMapColors.Y);
		g.text(font, DILang.WORKSHOP.get(), x1, y1 += h, MazeMapColors.K);
		g.text(font, DILang.SHOP.get(), x1, y1 += h, MazeMapColors.S);
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


}
