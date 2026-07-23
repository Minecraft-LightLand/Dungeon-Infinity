package dev.xkmc.dungeon_infinity.content.buff.special;

import dev.xkmc.dungeon_infinity.content.buff.core.ConsumableBuff;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public class ChorusBuff extends ConsumableBuff {

	public ChorusBuff(Identifier id, int max) {
		super(id, max);
	}

	@Override
	public void onSkillUse(ServerPlayer sp) {
		var data = DIMeta.HISTORY.type().getOrCreate(sp);
		var prev = data.getOrCreate(MazePos.map(sp.blockPosition())).getPath();
		if (prev.length == 0) return;
		removeOne(sp);
		int x = prev[0] >> 5;
		int z = prev[0] & 31;
		var mp = MazePos.map(sp.blockPosition());
		var pos = new BlockPos(mp.x() * 400 + x * 16 + 8, mp.y() * 16 + 8, mp.z() * 400 + z * 16 + 8);
		while (!sp.level().getBlockState(pos).isAir())
			pos = pos.below();
		while (sp.level().getBlockState(pos).isAir())
			pos = pos.below();
		var target = pos.above().getCenter();
		sp.teleportTo(target.x, target.y, target.z);
	}

}
