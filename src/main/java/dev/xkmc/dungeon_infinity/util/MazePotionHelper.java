package dev.xkmc.dungeon_infinity.util;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.l2core.base.effects.EffectBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public class MazePotionHelper {

	public static void whileInMaze(MazeHistory data, ServerPlayer sp) {
		if (sp.tickCount % 10 != 0) return;
		if (data.activeMobRoom != null) return;
		for (var eff : sp.getActiveEffects()) {
			if (eff.getEffect().value().getCategory() != MobEffectCategory.BENEFICIAL) return;
			int dur = eff.getDuration();
			if (dur % 20 < 10) {
				sp.addEffect(new EffectBuilder(new MobEffectInstance(eff)).setDuration(dur + 10).ins);
			}
		}
	}

}
