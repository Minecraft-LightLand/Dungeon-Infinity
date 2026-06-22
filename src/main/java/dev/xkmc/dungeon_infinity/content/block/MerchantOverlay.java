//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.dungeon_infinity.content.block;

import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.util.List;

public class MerchantOverlay implements GuiLayer {

	public void render(GuiGraphicsExtractor g, DeltaTracker deltaTracker) {
		if (Minecraft.getInstance().screen != null) return;
		ClientLevel level = Minecraft.getInstance().level;
		LocalPlayer player = Minecraft.getInstance().player;
		if (level == null || player == null) return;
		HitResult hit = Minecraft.getInstance().hitResult;
		if (!(hit instanceof BlockHitResult bhit)) return;
		if (!(level.getBlockEntity(bhit.getBlockPos()) instanceof MerchantBlockEntity tile)) return;
		List<Component> text = List.of(Component.literal(tile.type));
		if (text.isEmpty()) return;
		int sw = g.guiWidth();
		int sh = g.guiHeight();
		(new OverlayUtil(g, (int) ((double) sw * 0.7), (int) ((double) sh * (double) 0.5F), sw / 4)).renderLongText(Minecraft.getInstance().font, text);
	}
}
