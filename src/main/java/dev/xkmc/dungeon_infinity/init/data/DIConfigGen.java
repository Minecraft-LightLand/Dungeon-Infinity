package dev.xkmc.dungeon_infinity.init.data;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnData;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;

import java.util.concurrent.CompletableFuture;

public class DIConfigGen extends ConfigDataProvider {

	public DIConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Golem Spawn Config");
	}

	public void add(ConfigDataProvider.Collector map) {
		GolemSpawnData.gen(map);

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("stone"), new TemplateConfig()
				// stone 主题 → 入门难度
				.start("stone")
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.root("test")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.end()

				// mineshaft 主题 → 次低难度
				.start("mineshaft")
				.room("boss").variant("", 100, GolemSpawnData.MINESHAFT_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.MINESHAFT_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.MINESHAFT_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.MINESHAFT_QUAD).end()
				.room("boss").variant("", 100).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("mineshaft"), new TemplateConfig()
				.start("mineshaft")
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("room/end").variant("", 100, GolemSpawnData.MINESHAFT_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.MINESHAFT_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.MINESHAFT_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.MINESHAFT_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.MINESHAFT_ROOM).end()
				.end()

				// copper 主题 → 基准难度
				.start("copper")
				.room("boss").variant("", 100, GolemSpawnData.COPPER_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.COPPER_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.COPPER_STAIR).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.COPPER_STAIR).end()
				.root("test")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("copper"), new TemplateConfig()
				.start("copper")
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("room/end").variant("", 100, GolemSpawnData.COPPER_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.COPPER_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.COPPER_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.COPPER_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.COPPER_ROOM).end()
				.end()

				// deepslate 主题 → 工厂+猪灵混编，线性难度
				.start("deepslate")
				.room("boss").variant("", 100, GolemSpawnData.DEEPSLATE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.DEEPSLATE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.DEEPSLATE_STAIR).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.DEEPSLATE_STAIR).end()
				.root("test")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("deepslate"), new TemplateConfig()
				.start("deepslate")
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("room/end").variant("", 100, GolemSpawnData.DEEPSLATE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.DEEPSLATE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.DEEPSLATE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.DEEPSLATE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.DEEPSLATE_ROOM).end()
				.end()

				// sculk 主题 → 最高难度
				.start("sculk")
				.room("boss").variant("", 100, GolemSpawnData.SCULK_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.SCULK_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.SCULK_STAIR).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.SCULK_STAIR).end()
				.root("test")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("sculk"), new TemplateConfig()
				.start("sculk")
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.root("test")
				.room("room/end").variant("", 100, GolemSpawnData.SCULK_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.SCULK_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.SCULK_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.SCULK_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.SCULK_ROOM).end()
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());
	}

}
