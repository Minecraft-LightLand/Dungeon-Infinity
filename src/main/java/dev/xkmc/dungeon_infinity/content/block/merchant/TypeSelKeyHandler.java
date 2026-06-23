package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.l2itemselector.wheel.DefaultKeyHandler;
import dev.xkmc.l2itemselector.wheel.WheelAdaptor;
import net.minecraft.world.entity.player.Player;

public class TypeSelKeyHandler extends DefaultKeyHandler.Fast {

	@Override
	public void rightClick(WheelAdaptor<?> wheel, Player player) {
		if (TypeSelWheelHandler.press) {
			TypeSelWheelHandler.press = false;
			return;
		}
		super.rightClick(wheel, player);
	}

}
