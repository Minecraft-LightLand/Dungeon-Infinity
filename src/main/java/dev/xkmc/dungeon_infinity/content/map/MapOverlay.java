package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class MapOverlay implements GuiLayer, MapUI {

	@Override
	public void render(GuiGraphicsExtractor g, DeltaTracker pt) {
		int margin = 10;

		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!MazeHistory.inMazeDim(player)) return;
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		Long seed = null;
		if (main.is(DIItems.MAP.get()) && !off.isEmpty()) {
			seed = main.get(DIItems.SEED);
		} else if (off.is(DIItems.MAP.get())) {
			seed = off.get(DIItems.SEED);
		}
		if (seed == null) return;
		var pos = MazePos.map(player.blockPosition());

		int w = g.guiWidth();
		int h = g.guiHeight();
		int r = Math.min(w / 4, h / 4);
		int x0 = w - r / 2 - margin;
		int y0 = r / 2 + margin;
		float rate = r / 128f;
		g.enableScissor(w - r, 0, w, r);
		g.fill(w - r, 0, w, r, 0xaf7f7f7f);
		renderMap(player, g, seed, pos, x0, y0, rate, true);
		g.disableScissor();
	}

	@Override
	public void doCustomTransform(GuiGraphicsExtractor g, MazePos pos) {
		g.pose().translate(63 - pos.px() / 16f * 5f, 63 - pos.pz() / 16f * 5f);
	}
}
