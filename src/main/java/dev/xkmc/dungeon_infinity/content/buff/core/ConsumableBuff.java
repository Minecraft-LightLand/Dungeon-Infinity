package dev.xkmc.dungeon_infinity.content.buff.core;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ConsumableBuff extends MazeBuff {

	public ConsumableBuff(Identifier id, int max) {
		super(id, max);
	}

	@Override
	public Component getTitle(int lv) {
		return Component.translatable(id.getNamespace() + ".buff." + id.getPath()).append(" (" + lv + ")");
	}


}
