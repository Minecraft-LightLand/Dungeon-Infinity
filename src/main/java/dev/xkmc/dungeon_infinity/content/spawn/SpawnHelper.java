package dev.xkmc.dungeon_infinity.content.spawn;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnTicker;
import dev.xkmc.dungeon_infinity.content.cap.SectionRoom;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SpawnHelper {

	public static MobSpawnTicker createTickerFromTemplate(TemplateConfig.TemplateData info, @Nullable SectionRoom[][][] rooms, List<BlockPos> spawns) {
		var ans = new GolemSpawnTicker();
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
