package dev.xkmc.dungeon_infinity.content.spawn;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnTicker;
import dev.xkmc.dungeon_infinity.content.cap.SectionRoom;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SpawnHelper {

	public static TemplateConfig.SpawnContext from(@Nullable SectionRoom[][][] rooms) {
		int size = 0, y = 16;
		for (var arr : rooms)
			for (var ar : arr)
				for (var a : ar)
					if (a != null) {
						size++;
						y = Math.min(y, a.getBlockPos().getY() / 16);
					}
		return new TemplateConfig.SpawnContext(size, y);
	}

	public static MobSpawnTicker createTickerFromTemplate(TemplateConfig.TemplateData info, @Nullable SectionRoom[][][] rooms, List<BlockPos> spawns, RandomSource rand) {
		var ans = new GolemSpawnTicker();
		var poolId = info.spawn();
		if (poolId != null) {
			var pool = DungeonInfinity.TEMPLATES.getMerged().spawn.get(poolId);
			if (pool != null) {
				ans.trial = pool.fetch(from(rooms), rand);
			}
		}
		for (var e : spawns) ans.addTargetPos(e);
		if (spawns.isEmpty()) {
			for (SectionRoom[][] room : rooms) {
				for (var e : room[0]) {
					if (e == null) continue;
					ans.addTargetPos(e.getBlockPos().offset(8, 3, 8));
				}
			}
		}
		return ans;
	}

}
