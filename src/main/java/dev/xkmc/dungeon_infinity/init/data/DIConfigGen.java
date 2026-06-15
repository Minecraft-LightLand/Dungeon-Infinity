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
				.start("stone")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.root("test")
				.room("boss").end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("mineshaft"), new TemplateConfig()
				.start("mineshaft")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.root("test")
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("copper"), new TemplateConfig()
				.start("copper")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.root("test")
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("deepslate"), new TemplateConfig()
				.start("deepslate")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.root("test")
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("sculk"), new TemplateConfig()
				.start("sculk")
				.room("room/end").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/corner").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/cross").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/straight").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.room("room/t_way").variant("", 100, GolemSpawnData.STONE_ROOM).end()
				.root("test")
				.room("boss").variant("", 100, GolemSpawnData.STONE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("cross_stairs").variant("", 100, GolemSpawnData.STONE_QUAD).end()
				.room("path/corner").variant("", 100).end()
				.room("path/cross").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 100).end()
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());
	}

}
