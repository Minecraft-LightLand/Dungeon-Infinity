package dev.xkmc.dungeon_infinity.init.data;

import dev.xkmc.dungeon_infinity.content.trigger.DefeatRoomTrigger;
import dev.xkmc.dungeon_infinity.content.trigger.EnterRoomTrigger;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

public class DITriggers {

	public static final SR<CriterionTrigger<?>> REG;
	public static final Val<DefeatRoomTrigger> DEFEAT;
	public static final Val<EnterRoomTrigger> ENTER;

	public static void register() {
	}

	static {
		REG = SR.of(DungeonInfinity.REG, Registries.TRIGGER_TYPE);
		DEFEAT = REG.reg("defeat", DefeatRoomTrigger::new);
		ENTER = REG.reg("enter", EnterRoomTrigger::new);
	}

}
