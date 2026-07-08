package dev.xkmc.dungeon_infinity.content.buff;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ShieldBuff extends MazeBuff {

	public ShieldBuff(Identifier id, int max) {
		super(id, max);
	}

	@Override
	public void onDamage(ServerPlayer sp, int lv, DamageData.Defence data) {
		data.addDealtModifier(DamageModifier.multTotal(1 - 0.05f * lv, id));
	}

	@Override
	public List<Component> getDetail(int lv) {
		return List.of(Component.translatable(id.getNamespace() + ".buff." + id.getPath() + ".desc", Math.round(0.05f * lv)));
	}

}
