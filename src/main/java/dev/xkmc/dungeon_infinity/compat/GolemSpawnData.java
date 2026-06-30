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

	public static final Identifier STONE_ROOM_BASIC = DungeonInfinity.loc("stone/room");
	public static final Identifier STONE_ROOM_LARGE = DungeonInfinity.loc("stone/room");
	public static final Identifier STONE_ROOM_RANGED = DungeonInfinity.loc("stone/room");
	public static final Identifier STONE_ROOM_RIDER = DungeonInfinity.loc("stone/room");
	public static final Identifier STONE_QUAD = DungeonInfinity.loc("stone/quad");
	public static final Identifier STONE_BOSS = DungeonInfinity.loc("stone/boss");

	// ==================== MINESHAFT 矿道级（次低难度）====================
	// 使用 FactoryGolemSpawn 的配置，铜/铁材料由战利品表产出

	public static final Identifier MINESHAFT_ROOM_BASIC = DungeonInfinity.loc("mineshaft/room");
	public static final Identifier MINESHAFT_ROOM_LARGE = DungeonInfinity.loc("mineshaft/room");
	public static final Identifier MINESHAFT_ROOM_RANGED = DungeonInfinity.loc("mineshaft/room");
	public static final Identifier MINESHAFT_ROOM_RIDER = DungeonInfinity.loc("mineshaft/room");
	public static final Identifier MINESHAFT_QUAD = DungeonInfinity.loc("mineshaft/quad");
	public static final Identifier MINESHAFT_BOSS = DungeonInfinity.loc("mineshaft/boss");

	// ==================== COPPER 铜制级（基准难度）====================
	// 傀儡地牢废弃工厂配置

	public static final Identifier COPPER_ROOM_BASIC = DungeonInfinity.loc("copper/room");
	public static final Identifier COPPER_ROOM_LARGE = DungeonInfinity.loc("copper/room");
	public static final Identifier COPPER_ROOM_RANGED = DungeonInfinity.loc("copper/room");
	public static final Identifier COPPER_ROOM_RIDER = DungeonInfinity.loc("copper/room");
	public static final Identifier COPPER_QUAD = DungeonInfinity.loc("copper/quad");
	public static final Identifier COPPER_BOSS = DungeonInfinity.loc("copper/boss");

	// ==================== DEEPSLATE 深板岩级（工厂+猪灵混编，线性难度）====================

	public static final Identifier DEEPSLATE_ROOM_BASIC = DungeonInfinity.loc("deepslate/room");
	public static final Identifier DEEPSLATE_ROOM_LARGE = DungeonInfinity.loc("deepslate/room");
	public static final Identifier DEEPSLATE_ROOM_RANGED = DungeonInfinity.loc("deepslate/room");
	public static final Identifier DEEPSLATE_ROOM_RIDER = DungeonInfinity.loc("deepslate/room");
	public static final Identifier DEEPSLATE_QUAD = DungeonInfinity.loc("deepslate/quad");
	public static final Identifier DEEPSLATE_BOSS = DungeonInfinity.loc("deepslate/boss");

	// ==================== SCULK 幽匿级（最高难度）====================
	// 照搬傀儡地牢 SculkGolemSpawn，钻石装备+幽匿材料

	public static final Identifier SCULK_ROOM_BASIC = DungeonInfinity.loc("sculk/room");
	public static final Identifier SCULK_ROOM_LARGE = DungeonInfinity.loc("sculk/room");
	public static final Identifier SCULK_ROOM_RANGED = DungeonInfinity.loc("sculk/room");
	public static final Identifier SCULK_ROOM_RIDER = DungeonInfinity.loc("sculk/room");
	public static final Identifier SCULK_QUAD = DungeonInfinity.loc("sculk/quad");
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
		// BASIC: 教学关，基础近战兵
		col.add(GolemDungeons.TRIAL, STONE_ROOM_BASIC, new TrialConfig().setReward(DILootGen.STONE_ROOM_BASIC)
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 2))
		);

		// LARGE: 引入大型兵，作为小boss感
		col.add(GolemDungeons.TRIAL, STONE_ROOM_LARGE, new TrialConfig().setReward(DILootGen.STONE_ROOM_LARGE)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_BASIC, 2))
		);

		// RANGED: 引入远程兵种，教会玩家优先处理后排
		col.add(GolemDungeons.TRIAL, STONE_ROOM_RANGED, new TrialConfig().setReward(DILootGen.STONE_ROOM_RANGED)
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(EARLY_RANGED, 2))
		);

		// RIDER: 引入药水骑兵，debuff威胁+高机动性
		col.add(GolemDungeons.TRIAL, STONE_ROOM_RIDER, new TrialConfig().setReward(DILootGen.STONE_ROOM_RIDER)
				.add(of(FactoryGolemSpawn.HUMANOID_TIPPED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, STONE_QUAD, new TrialConfig().setReward(DILootGen.STONE_QUAD)
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 1), of(EARLY_RANGED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_BASIC, 2), of(EARLY_RANGED, 1))
		);

		// --- MINESHAFT 矿道级（工厂兵，混入大型）---
		// BASIC: 基础矿道兵，铜铁装备混编
		col.add(GolemDungeons.TRIAL, MINESHAFT_ROOM_BASIC, new TrialConfig().setReward(DILootGen.MINESHAFT_ROOM_BASIC)
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_BASIC, 1))
		);

		// LARGE: 大型兵种，LARGE_1+远程护卫
		col.add(GolemDungeons.TRIAL, MINESHAFT_ROOM_LARGE, new TrialConfig().setReward(DILootGen.MINESHAFT_ROOM_LARGE)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(EARLY_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
		);

		// RANGED: 远程火力压制，大量弓箭手+近战护卫
		col.add(GolemDungeons.TRIAL, MINESHAFT_ROOM_RANGED, new TrialConfig().setReward(DILootGen.MINESHAFT_ROOM_RANGED)
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(EARLY_RANGED, 3))
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 1), of(EARLY_RANGED, 2))
		);

		// RIDER: 火箭骑兵+药水兵混编，高机动debuff组合
		col.add(GolemDungeons.TRIAL, MINESHAFT_ROOM_RIDER, new TrialConfig().setReward(DILootGen.MINESHAFT_ROOM_RIDER)
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, MINESHAFT_QUAD, new TrialConfig().setReward(DILootGen.MINESHAFT_QUAD)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 2), of(EARLY_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 4),
						of(EARLY_RANGED, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.LARGE_2, 1))
		);

		col.add(GolemDungeons.TRIAL, MINESHAFT_BOSS, new TrialConfig().setReward(DILootGen.MINESHAFT_BOSS)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_BASIC, 1))
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(EARLY_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_1, 2),
						of(EARLY_RANGED, 1),
						of(FactoryGolemSpawn.LARGE_2, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		// --- COPPER 铜制级（基准，原 STONE）---
		// BASIC: 铜制近战基础
		col.add(GolemDungeons.TRIAL, COPPER_ROOM_BASIC, new TrialConfig().setReward(DILootGen.COPPER_ROOM_BASIC)
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_RANGED, 1))
		);

		// LARGE: LARGE_2大型兵+近战护卫
		col.add(GolemDungeons.TRIAL, COPPER_ROOM_LARGE, new TrialConfig().setReward(DILootGen.COPPER_ROOM_LARGE)
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 2))
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
		);

		// RANGED: 远程火力+药水支援
		col.add(GolemDungeons.TRIAL, COPPER_ROOM_RANGED, new TrialConfig().setReward(DILootGen.COPPER_ROOM_RANGED)
				.add(of(FactoryGolemSpawn.HUMANOID_RANGED, 3), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
		);

		// RIDER: 火箭骑兵+大型兵，高机动突击
		col.add(GolemDungeons.TRIAL, COPPER_ROOM_RIDER, new TrialConfig().setReward(DILootGen.COPPER_ROOM_RIDER)
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 2))
				.add(of(FactoryGolemSpawn.LARGE_3, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		col.add(GolemDungeons.TRIAL, COPPER_QUAD, new TrialConfig().setReward(DILootGen.COPPER_QUAD)
				.add(of(FactoryGolemSpawn.LARGE_2, 1),
						of(FactoryGolemSpawn.HUMANOID_MELEE, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(FactoryGolemSpawn.LARGE_3, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 2),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
		);

		col.add(GolemDungeons.TRIAL, COPPER_BOSS, new TrialConfig().setReward(DILootGen.COPPER_BOSS)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_2, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		// --- DEEPSLATE 深板岩级（工厂+猪灵混编）---
		// BASIC: 猪灵近战+工厂基础兵混编入门
		col.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_BASIC, new TrialConfig().setReward(DILootGen.DEEPSLATE_ROOM_BASIC)
				.add(of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_BASIC, 2))
		);

		// LARGE: 猪灵大型兵+工厂大型兵混编
		col.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_LARGE, new TrialConfig().setReward(DILootGen.DEEPSLATE_ROOM_LARGE)
				.add(of(PiglinGolemSpawn.LARGE, 1), of(FactoryGolemSpawn.LARGE_2, 1))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2))
		);

		// RANGED: 猪灵远程+工厂火箭兵，远程火力网
		col.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_RANGED, new TrialConfig().setReward(DILootGen.DEEPSLATE_ROOM_RANGED)
				.add(of(PiglinGolemSpawn.HUMANOID_RANGED, 3), of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		// RIDER: 猪灵肩炮大型兵+工厂火箭骑兵，终极机动火力
		col.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_RIDER, new TrialConfig().setReward(DILootGen.DEEPSLATE_ROOM_RIDER)
				.add(of(PiglinGolemSpawn.LARGE_SHOULDER, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
		);

		col.add(GolemDungeons.TRIAL, DEEPSLATE_QUAD, new TrialConfig().setReward(DILootGen.DEEPSLATE_QUAD)
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.LARGE, 1),
						of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(PiglinGolemSpawn.LARGE_SHOULDER, 1), of(PiglinGolemSpawn.LARGE, 2),
						of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		col.add(GolemDungeons.TRIAL, DEEPSLATE_BOSS, new TrialConfig().setReward(DILootGen.DEEPSLATE_BOSS)
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(PiglinGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_3, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(PiglinGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(PiglinGolemSpawn.LARGE, 2), of(PiglinGolemSpawn.HUMANOID_MELEE, 3), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		// --- SCULK 幽匿级（钻石装备，最高难度）---
		// BASIC: 幽匿近战入门
		col.add(GolemDungeons.TRIAL, SCULK_ROOM_BASIC, new TrialConfig().setReward(DILootGen.SCULK_ROOM_BASIC)
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
		);

		// LARGE: 幽匿大型兵+近战护卫
		col.add(GolemDungeons.TRIAL, SCULK_ROOM_LARGE, new TrialConfig().setReward(DILootGen.SCULK_ROOM_LARGE)
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);

		// RANGED: 幽匿远程火力+全副武装精英
		col.add(GolemDungeons.TRIAL, SCULK_ROOM_RANGED, new TrialConfig().setReward(DILootGen.SCULK_ROOM_RANGED)
				.add(of(SculkGolemSpawn.HUMANOID_RANGED, 3), of(SculkGolemSpawn.SCULK_ALL, 1))
				.add(of(SculkGolemSpawn.SCULK_BETTER, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2))
		);

		// RIDER: 幽匿精英骑兵+大型兵，终极挑战
		col.add(GolemDungeons.TRIAL, SCULK_ROOM_RIDER, new TrialConfig().setReward(DILootGen.SCULK_ROOM_RIDER)
				.add(of(SculkGolemSpawn.SCULK_ALL, 2), of(SculkGolemSpawn.SCULK_BETTER, 1))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);

		col.add(GolemDungeons.TRIAL, SCULK_QUAD, new TrialConfig().setReward(DILootGen.SCULK_QUAD)
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.SCULK_ALL, 1), of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 3))
				.add(of(SculkGolemSpawn.SCULK_ALL, 3), of(SculkGolemSpawn.SCULK_BETTER, 1))
		);

		col.add(GolemDungeons.TRIAL, SCULK_BOSS, new TrialConfig().setReward(DILootGen.SCULK_BOSS)
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);
	}

}