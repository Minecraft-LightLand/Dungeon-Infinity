package dev.xkmc.dungeon_infinity.content.buff.core;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;

public class PotionBuff extends MazeBuff {

	public record PotionEntry(Holder<MobEffect> eff, int amp, int dur) {

		public Component getDesc(int lv) {
			MutableComponent line = PotionContents.getPotionDescription(eff, amp);
			return Component.translatable("potion.withDuration", line, StringUtil.formatTickDuration(dur * lv, 20));
		}
	}

	private final List<PotionEntry> effs;

	public PotionBuff(Identifier id, int max, List<PotionEntry> items) {
		super(id, max);
		this.effs = items;
	}

	@Override
	public void onApply(ServerPlayer sp, int lv) {
		for (var e : effs) {
			var old = sp.getEffect(e.eff);
			int dur = e.dur * lv;
			if (old != null && old.getAmplifier() == e.amp) {
				dur += old.getDuration();
			}
			sp.addEffect(new MobEffectInstance(e.eff, dur, e.amp, false, false, true));
		}
	}

	@Override
	public void onUpdate(ServerPlayer sp, int lv) {
		if (lv == 0) {
			for (var e : effs) {
				sp.removeEffect(e.eff);
			}
		}
	}

	@Override
	public List<Component> getDetail(int lv) {
		List<Component> ans = new ArrayList<>(super.getDetail(lv));
		for (var e : effs)
			ans.add(Component.literal("- ").append(e.getDesc(lv)));
		return ans;
	}

}
