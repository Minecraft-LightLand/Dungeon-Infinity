package dev.xkmc.dungeon_infinity.content.buff.special;

import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.dungeon_infinity.content.buff.core.PotionBuff;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class NinjaBuff extends MazeBuff {

	private final PotionBuff.PotionEntry potion;

	public NinjaBuff(Identifier id, int max, PotionBuff.PotionEntry potion) {
		super(id, max);
		this.potion = potion;
	}

	@Override
	public boolean onAttacked(ServerPlayer sp, int lv, DamageData.Attack data) {
		return data.getAttacker() instanceof Mob mob && mob.getTarget() != sp;
	}

	@Override
	public void onEnterBattle(ServerPlayer sp, int lv) {
		potion.apply(sp, lv);
	}

	@Override
	public List<Component> getDetail(int lv) {
		return List.of(Component.translatable(id.getNamespace() + ".buff." + id.getPath() + ".desc", potion.getDesc(lv)));
	}
}
