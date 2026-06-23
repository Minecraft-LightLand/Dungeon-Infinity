package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.l2itemselector.wheel.WheelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class TypeSelWheelHandler {

	public static boolean press = false;

	public static boolean enableWheel(Player player, MerchantBlockEntity be) {
		Minecraft.getInstance().options.keyUse.setDown(false);
		if (WheelHandler.wheel instanceof TypeSelWheel)
			return false;
		if (WheelHandler.wheel != null) WheelHandler.wheel.onClose();
		WheelHandler.wheelIndex = 0;
		WheelHandler.wheel = new TypeSelWheel(be);
		WheelHandler.wheel.onOpen();
		WheelHandler.keyboardIndex = -1;
		Minecraft.getInstance().mouseHandler.releaseMouse();
		press = true;
		return true;
	}

}
