package dev.xkmc.dungeon_infinity.content.buff.special;

import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class HealGolemBuff extends MazeBuff {


	public HealGolemBuff(Identifier id, int max) {
		super(id, max);
	}

	public void onDefeat(ServerPlayer sp, int lv, int size) {
		for (var e : sp.level().getEntities(sp, sp.getBoundingBox().inflate(35, 16, 35))) {
			if (e instanceof AbstractGolemEntity<?, ?> g) {
				if (g.getOwnerPlayer() == sp) {
					g.repair(g.getMaxHealth() * 0.25f * lv);
				}
			}
		}
		for (var e : sp.getInventory()) {
			if (e.getItem() instanceof GolemHolder<?, ?>) {
				float mhp = GolemHolder.getMaxHealth(e);
				if (mhp < 0) continue;
				float hp = GolemHolder.getHealth(e);
				GolemHolder.setHealth(e, Math.min(mhp, hp + mhp * 0.25f * lv));
			}
		}
	}

	@Override
	public List<Component> getDetail(int lv) {
		return List.of(Component.translatable(id.getNamespace() + ".buff." + id.getPath() + ".desc", 25 * lv));
	}

}
