package dev.xkmc.dungeon_infinity.content.screen;

import dev.xkmc.dungeon_infinity.content.buff.MazeBuff;
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

	private final TextButton up, down, findQuad, findStair, findWarehouse, findWorkshop, findShop;

	public MazeMapScreen(long seed, boolean canUseWaypoint) {
		super(Component.literal("Maze Map"));
		this.seed = seed;
		this.canUseWaypoint = canUseWaypoint;
		up = new TextButton();
		down = new TextButton();
		findQuad = new TextButton();
		findStair = new TextButton();
		findWarehouse = new TextButton();
		findWorkshop = new TextButton();
		findShop = new TextButton();
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
		extractLegend(player, g, x0, y0, rate, pos);
		extractBuff(player, g, x0, y0, rate);
	}

	public void extractLegend(Player player, GuiGraphicsExtractor g, int x0, int y0, float rate, MazePos pos) {
		int x1 = (int) (x0 + rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int h = font.lineHeight + 3;

		int bx = x1 + 50;
		int by = y1;
		up.update(g, pos.y() < 15, bx, by, font, DILang.UP.get(), mx, my);
		down.update(g, pos.y() > 0, bx, by + h, font, DILang.DOWN.get(), mx, my);
		y1 -= h - 5;

		var data = DIMeta.HISTORY.type().getOrCreate(player);
		var findable = diffY == 0 && (data.finder.finder > 0 || player.isCreative());
		var mag = DILang.MAGNIFIER.get();

		g.text(font, DILang.DEPTH.get(16 - pos.y()), x1, y1 += h, -1);
		y1 += h;
		if (findable) {
			g.text(font, DILang.FINDER.get(data.finder.finder), x1, y1 += h, -1);
			y1 += h;
		}
		g.text(font, DILang.BATTLE.get(), x1, y1 += h, MazeMapColors.F);
		g.text(font, DILang.QUAD.get(), x1, y1 += h, MazeMapColors.Q);
		findQuad.update(g, findable, x1 + font.width(DILang.QUAD.get()), y1, font, mag, mx, my);
		g.text(font, DILang.BOSS.get(), x1, y1 += h, MazeMapColors.R);
		g.text(font, DILang.DOWN_STAIR.get(), x1, y1 += h, MazeMapColors.G);
		findStair.update(g, findable, x1 + font.width(DILang.DOWN_STAIR.get()), y1, font, mag, mx, my);
		g.text(font, DILang.UP_STAIR.get(), x1, y1 += h, MazeMapColors.Y);
		g.text(font, DILang.WORKSHOP.get(), x1, y1 += h, MazeMapColors.K);
		findWorkshop.update(g, findable, x1 + font.width(DILang.WORKSHOP.get()), y1, font, mag, mx, my);
		g.text(font, DILang.SHOP.get(), x1, y1 += h, MazeMapColors.S);
		findShop.update(g, findable, x1 + font.width(DILang.SHOP.get()), y1, font, mag, mx, my);
		g.text(font, DILang.WAREHOUSE.get(), x1, y1 += h, MazeMapColors.H);
		findWarehouse.update(g, findable, x1 + font.width(DILang.WAREHOUSE.get()), y1, font, mag, mx, my);
	}

	public void extractBuff(Player player, GuiGraphicsExtractor g, int x0, int y0, float rate) {
		int x1 = (int) (x0 - rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int h = font.lineHeight + 3;
		y1 -= h - 5;
		var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
		for (var e : data.buffs.entrySet()) {
			var buff = MazeBuff.get(e.getKey());
			var title = buff.getTitle(e.getValue());
			int w = font.width(title);
			int x2 = (x1 - w) / 2;
			g.text(font, title, x2, y1 += h, -1);
			if (x2 <= mx && mx <= x2 + w && y1 <= my && my <= y1 + h) {
				g.setComponentTooltipForNextFrame(font, buff.getDetail(e.getValue()), mx, my);
			}

		}
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
