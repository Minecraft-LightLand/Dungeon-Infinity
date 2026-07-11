package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2core.util.TooltipHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class MapOverlay implements GuiLayer, MapUI {

	@Override
	public void render(GuiGraphicsExtractor g, DeltaTracker pt) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!MazeHistory.inMazeDim(player)) return;

		Long seed = findSeed(player);
		if (seed == null) return;
		var pos = MazePos.map(player.blockPosition());

		int w = g.guiWidth();
		int h = g.guiHeight();
		int cfg = MapOverlayConfig.size;
		int r = Math.min(w * cfg / 400, h * cfg / 400);
		int margin = 10;

		float rate = r / 128f * MapOverlayConfig.scale;

		int px = MapOverlayConfig.posX(w);
		int py = MapOverlayConfig.posY(h);
		int x0 = calcPos(px, margin, r, w);
		int y0 = calcPos(py, margin, r, h);

		int left = x0 - r / 2;
		int right = x0 + r / 2;
		int top = y0 - r / 2;
		int bottom = y0 + r / 2;

		g.enableScissor(left, top, right, bottom);
		g.fill(left, top, right, bottom, 0xFF161616);
		if (MapOverlayConfig.followPlayer) {
			renderMap(player, g, seed, pos, x0, y0, rate, true);
		} else {
			renderFullMap(player, g, seed, pos, x0, y0, rate);
		}
		g.disableScissor();

		int borderCol = 0xFF4A4A4A;
		g.fill(left, top, right, top + 1, borderCol);
		g.fill(left, bottom - 1, right, bottom, borderCol);
		g.fill(left, top, left + 1, bottom, borderCol);
		g.fill(right - 1, top, right, bottom, borderCol);
	}

	private Long findSeed(Player player) {
		int mode = MapOverlayConfig.screenMapMode;
		if (mode == 0) return null;

		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		boolean hasInHand = main.is(DIItems.MAP.get()) || off.is(DIItems.MAP.get());

		if (mode == 1) {
			if (main.is(DIItems.MAP.get())) return main.get(DIItems.SEED);
			if (off.is(DIItems.MAP.get())) return off.get(DIItems.SEED);
			return null;
		}

		if (mode == 2 && hasInHand) return null;

		if (hasInHand) {
			if (main.is(DIItems.MAP.get()) && main.has(DIItems.SEED))
				return main.get(DIItems.SEED);
			if (off.has(DIItems.SEED))
				return off.get(DIItems.SEED);
		}
		for (int i = 0; i < 9; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(DIItems.MAP.get()) && stack.has(DIItems.SEED))
				return stack.get(DIItems.SEED);
		}
		return null;
	}

	private static int calcPos(int coord, int margin, int r, int max) {
		if (coord == 0) return margin + r / 2;
		if (coord == max) return max - margin - r / 2;
		return max / 2;
	}

	@Override
	public void doCustomTransform(GuiGraphicsExtractor g, MazePos pos) {
		g.pose().translate(63 - pos.px() / 16f * 5f, 63 - pos.pz() / 16f * 5f);
	}

	private void renderFullMap(Player player, GuiGraphicsExtractor g, long seed, MazePos pos, float x0, float y0, float rate) {
		var tex = MazeMapTextureManager.get().getDetail(seed, pos);
		var path = MazeMapTextureManager.get().getPath(seed, pos);
		var fog = MazeMapTextureManager.get().getFog(seed, pos);
		var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
		tex.update(visit);
		path.update(visit);
		fog.update(visit);
		g.pose().pushMatrix();
		g.pose().translate(x0, y0);
		g.pose().scale(rate, rate);
		g.pose().translate(-63, -63);
		g.blit(RenderPipelines.GUI_TEXTURED, tex.id, 0, 0, 0, 0, 125, 125, 128, 128);
		g.blit(RenderPipelines.GUI_TEXTURED, path.id, 0, 0, 0, 0, 125, 125, 128, 128);
		g.pose().pushMatrix();
		g.pose().scale(5, 5);
		if (!player.isCreative() || !TooltipHelper.hasShiftDown())
			g.blit(RenderPipelines.GUI_TEXTURED, fog.id, 0, 0, 0, 0, 25, 25, 32, 32);
		g.pose().popMatrix();
		renderPlayer(player, g, pos);
		renderWaypoints(g, visit);
		g.pose().popMatrix();
	}
}
