package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILootGen;
import dev.xkmc.golemdungeons.content.config.SpawnConfig;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.content.faction.DungeonFactionRegistry;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.FactoryGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.PiglinGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.SculkGolemSpawn;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.Identifier;

public class GolemSpawnData extends AbstractGolemSpawn {

	// ==================== 共用远程兵 =====================
	// 白板弓箭手，无甲无升级，用于前期层
	public static final Identifier EARLY_RANGED = DungeonInfinity.loc("early_ranged");

	// ==================== STONE 石制级（最低难度）====================
	// 使用 FactoryGolemSpawn 的配置，金材料由战利品表产出

	public static final Identifier STONE_ROOM = DungeonInfinity.loc("stone/room");
	public static final Identifier STONE_QUAD = DungeonInfinity.loc("stone/quad");
	public static final Identifier STONE_BOSS = DungeonInfinity.loc("stone/boss");

	// ==================== MINESHAFT 矿道级（次低难度）====================
	// 使用 FactoryGolemSpawn 的配置，铜/铁材料由战利品表产出

	public static final Identifier MINESHAFT_ROOM = DungeonInfinity.loc("mineshaft/room");
	public static final Identifier MINESHAFT_QUAD = DungeonInfinity.loc("mineshaft/quad");
	public static final Identifier MINESHAFT_BOSS = DungeonInfinity.loc("mineshaft/boss");

	// ==================== COPPER 铜制级（基准难度）====================
	// 傀儡地牢废弃工厂配置

	public static final Identifier COPPER_ROOM = DungeonInfinity.loc("copper/room");
	public static final Identifier COPPER_QUAD = DungeonInfinity.loc("copper/quad");
	public static final Identifier COPPER_STAIR = DungeonInfinity.loc("copper/stair");
	public static final Identifier COPPER_BOSS = DungeonInfinity.loc("copper/boss");

	// ==================== DEEPSLATE 深板岩级（工厂+猪灵混编，线性难度）====================

	public static final Identifier DEEPSLATE_ROOM = DungeonInfinity.loc("deepslate/room");
	public static final Identifier DEEPSLATE_QUAD = DungeonInfinity.loc("deepslate/quad");
	public static final Identifier DEEPSLATE_STAIR = DungeonInfinity.loc("deepslate/stair");
	public static final Identifier DEEPSLATE_BOSS = DungeonInfinity.loc("deepslate/boss");

	// ==================== SCULK 幽匿级（最高难度）====================
	// 照搬傀儡地牢 SculkGolemSpawn，钻石装备+幽匿材料

	public static final Identifier SCULK_ROOM = DungeonInfinity.loc("sculk/room");
	public static final Identifier SCULK_QUAD = DungeonInfinity.loc("sculk/quad");
	public static final Identifier SCULK_STAIR = DungeonInfinity.loc("sculk/stair");
	public static final Identifier SCULK_BOSS = DungeonInfinity.loc("sculk/boss");

	public static void gen(ConfigDataProvider.Collector col) {

		// ========== SPAWN CONFIGS ==========

		// EARLY_RANGED: 白板远程兵，铜材料 + 弓 + 箭，无护甲无升级
		col.add(GolemDungeons.SPAWN, EARLY_RANGED, new SpawnConfig(DungeonFactionRegistry.REMNANT)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 0))
				.mat(ModularGolems.loc("copper"), 100)
				.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
						.add(100, FactoryGolemSpawn.ITEM_HUMANOID_BOW))
		);

		// ========== TRIAL CONFIGS ==========

		// --- STONE 石制级（纯工厂人形兵）---
		col.add(GolemDungeons.TRIAL, STONE_ROOM, new TrialConfig().setReward(DILootGen.STONE_ROOM).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 1))
		);

		col.add(GolemDungeons.TRIAL, STONE_QUAD, new TrialConfig().setReward(DILootGen.STONE_QUAD).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 1), of(EARLY_RANGED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 2), of(EARLY_RANGED, 1))
		);

		col.add(GolemDungeons.TRIAL, STONE_BOSS, new TrialConfig().setReward(DILootGen.STONE_BOSS).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 2), of(EARLY_RANGED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 3), of(EARLY_RANGED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 4), of(EARLY_RANGED, 2))
		);

		// --- MINESHAFT 矿道级（工厂兵，混入大型怪）---
		col.add(GolemDungeons.TRIAL, MINESHAFT_ROOM, new TrialConfig().setReward(DILootGen.MINESHAFT_ROOM).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 1), of(EARLY_RANGED, 1))
		);

		col.add(GolemDungeons.TRIAL, MINESHAFT_QUAD, new TrialConfig().setReward(DILootGen.MINESHAFT_QUAD).genChest()
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_BASIC, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(EARLY_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 2),
						of(EARLY_RANGED, 1),
						of(FactoryGolemSpawn.LARGE_2, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, MINESHAFT_BOSS, new TrialConfig().setReward(DILootGen.MINESHAFT_BOSS).genChest()
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 2), of(EARLY_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 4),
						of(EARLY_RANGED, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.LARGE_2, 1))
		);

		// --- COPPER 铜制级（基准，原 STONE）---
		col.add(GolemDungeons.TRIAL, COPPER_ROOM, new TrialConfig().setReward(DILootGen.COPPER_ROOM).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 1))
		);

		col.add(GolemDungeons.TRIAL, COPPER_QUAD, new TrialConfig().setReward(DILootGen.COPPER_QUAD).genChest()
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, COPPER_STAIR, new TrialConfig().setReward(DILootGen.COPPER_STAIR).genChest()
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 3), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 2),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
		);

		col.add(GolemDungeons.TRIAL, COPPER_BOSS, new TrialConfig().setReward(DILootGen.COPPER_BOSS).genChest()
				.add(of(FactoryGolemSpawn.LARGE_2, 1),
						of(FactoryGolemSpawn.HUMANOID_MELEE, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(FactoryGolemSpawn.LARGE_3, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(FactoryGolemSpawn.LARGE_3, 4), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 2),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
		);

		// --- DEEPSLATE 深板岩级（工厂+猪灵混编）---
		col.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM, new TrialConfig().setReward(DILootGen.DEEPSLATE_ROOM).genChest()
				.add(of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 1))
		);

		col.add(GolemDungeons.TRIAL, DEEPSLATE_QUAD, new TrialConfig().setReward(DILootGen.DEEPSLATE_QUAD).genChest()
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 1), of(PiglinGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, DEEPSLATE_STAIR, new TrialConfig().setReward(DILootGen.DEEPSLATE_STAIR).genChest()
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 1), of(PiglinGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2),
						of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, DEEPSLATE_BOSS, new TrialConfig().setReward(DILootGen.DEEPSLATE_BOSS).genChest()
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.LARGE, 1),
						of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(PiglinGolemSpawn.LARGE_SHOULDER, 1), of(PiglinGolemSpawn.LARGE, 2),
						of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		// --- SCULK 幽匿级（钻石装备，最高难度）---
		col.add(GolemDungeons.TRIAL, SCULK_ROOM, new TrialConfig().setReward(DILootGen.SCULK_ROOM).genChest()
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
		);

		col.add(GolemDungeons.TRIAL, SCULK_QUAD, new TrialConfig().setReward(DILootGen.SCULK_QUAD).genChest()
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);

		col.add(GolemDungeons.TRIAL, SCULK_STAIR, new TrialConfig().setReward(DILootGen.SCULK_STAIR).genChest()
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2),
						of(SculkGolemSpawn.SCULK_ALL, 1))
		);

		col.add(GolemDungeons.TRIAL, SCULK_BOSS, new TrialConfig().setReward(DILootGen.SCULK_BOSS).genChest()
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.SCULK_ALL, 1), of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 3))
				.add(of(SculkGolemSpawn.SCULK_ALL, 8), of(SculkGolemSpawn.SCULK_BETTER, 1))
		);
	}

}
