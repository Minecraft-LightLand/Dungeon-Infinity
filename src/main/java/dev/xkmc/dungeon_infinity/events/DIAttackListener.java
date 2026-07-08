package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.server.level.ServerPlayer;

public class DIAttackListener implements AttackListener {

	@Override
	public void onDamage(DamageData.Defence data) {
		if (data.getTarget() instanceof ServerPlayer sp) {
			if (MazeHistory.inMazeDim(sp)) {
				var buffs = DIMeta.HISTORY.type().getOrCreate(sp).buff;
				buffs.onDamage(sp, data);
			}
		}
	}

}
