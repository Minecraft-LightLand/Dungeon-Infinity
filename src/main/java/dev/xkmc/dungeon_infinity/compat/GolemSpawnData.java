package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.dungeon_infinity.compat.room.EarlyGolemSpawn;
import dev.xkmc.dungeon_infinity.compat.room.MineshaftGolemSpawn;
import dev.xkmc.dungeon_infinity.compat.room.StoneGolemSpawn;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.FactoryGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.PiglinGolemSpawn;
import dev.xkmc.golemdungeons.init.data.spawn.SculkGolemSpawn;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.resources.Identifier;

public class GolemSpawnData extends AbstractGolemSpawn {

	// ==================== STONE 石制级 ====================
	public static final Identifier EARLY_ROOM_BASIC = DungeonInfinity.loc("early/room/basic");
	public static final Identifier EARLY_ROOM_LARGE = DungeonInfinity.loc("early/room/large");
	public static final Identifier EARLY_ROOM_RANGED = DungeonInfinity.loc("early/room/ranged");
	public static final Identifier EARLY_ROOM_RIDER = DungeonInfinity.loc("early/room/rider");
	public static final Identifier EARLY_ROOM_MIXED = DungeonInfinity.loc("early/room/mixed");
	public static final Identifier EARLY_QUAD = DungeonInfinity.loc("early/quad");
	public static final Identifier STONE_ROOM_BASIC = DungeonInfinity.loc("stone/room/basic");
	public static final Identifier STONE_ROOM_LARGE = DungeonInfinity.loc("stone/room/large");
	public static final Identifier STONE_ROOM_RANGED = DungeonInfinity.loc("stone/room/ranged");
	public static final Identifier STONE_ROOM_RIDER = DungeonInfinity.loc("stone/room/rider");
	public static final Identifier STONE_ROOM_MIXED = DungeonInfinity.loc("stone/room/mixed");
	public static final Identifier STONE_QUAD = DungeonInfinity.loc("stone/quad");

	// ==================== MINESHAFT 矿道级 ====================
	public static final Identifier MINESHAFT_ROOM_BASIC = DungeonInfinity.loc("mineshaft/room/basic");
	public static final Identifier MINESHAFT_ROOM_LARGE = DungeonInfinity.loc("mineshaft/room/large");
	public static final Identifier MINESHAFT_ROOM_RANGED = DungeonInfinity.loc("mineshaft/room/ranged");
	public static final Identifier MINESHAFT_ROOM_RIDER = DungeonInfinity.loc("mineshaft/room/rider");
	public static final Identifier MINESHAFT_ROOM_MIXED = DungeonInfinity.loc("mineshaft/room/mixed");
	public static final Identifier MINESHAFT_QUAD = DungeonInfinity.loc("mineshaft/quad");
	public static final Identifier MINESHAFT_BOSS = DungeonInfinity.loc("mineshaft/boss");

	// ==================== COPPER 铜制级 ====================
	public static final Identifier COPPER_ROOM_BASIC = DungeonInfinity.loc("copper/room/basic");
	public static final Identifier COPPER_ROOM_LARGE = DungeonInfinity.loc("copper/room/large");
	public static final Identifier COPPER_ROOM_RANGED = DungeonInfinity.loc("copper/room/ranged");
	public static final Identifier COPPER_ROOM_RIDER = DungeonInfinity.loc("copper/room/rider");
	public static final Identifier COPPER_ROOM_MIXED = DungeonInfinity.loc("copper/room/mixed");
	public static final Identifier COPPER_QUAD = DungeonInfinity.loc("copper/quad");
	public static final Identifier COPPER_BOSS = DungeonInfinity.loc("copper/boss");

	// ==================== DEEPSLATE 深板岩级 ====================
	public static final Identifier DEEPSLATE_ROOM_BASIC = DungeonInfinity.loc("deepslate/room/basic");
	public static final Identifier DEEPSLATE_ROOM_LARGE = DungeonInfinity.loc("deepslate/room/large");
	public static final Identifier DEEPSLATE_ROOM_RANGED = DungeonInfinity.loc("deepslate/room/ranged");
	public static final Identifier DEEPSLATE_ROOM_RIDER = DungeonInfinity.loc("deepslate/room/rider");
	public static final Identifier DEEPSLATE_ROOM_MIXED = DungeonInfinity.loc("deepslate/room/mixed");
	public static final Identifier DEEPSLATE_QUAD = DungeonInfinity.loc("deepslate/quad");
	public static final Identifier DEEPSLATE_BOSS = DungeonInfinity.loc("deepslate/boss");

	// ==================== SCULK 幽匿级 ====================
	public static final Identifier SCULK_ROOM_BASIC = DungeonInfinity.loc("sculk/room/basic");
	public static final Identifier SCULK_ROOM_LARGE = DungeonInfinity.loc("sculk/room/large");
	public static final Identifier SCULK_ROOM_RANGED = DungeonInfinity.loc("sculk/room/ranged");
	public static final Identifier SCULK_ROOM_RIDER = DungeonInfinity.loc("sculk/room/rider");
	public static final Identifier SCULK_ROOM_MIXED = DungeonInfinity.loc("sculk/room/mixed");
	public static final Identifier SCULK_QUAD = DungeonInfinity.loc("sculk/quad");
	public static final Identifier SCULK_BOSS = DungeonInfinity.loc("sculk/boss");

	public static void gen(ConfigDataProvider.Collector map) {
		EarlyGolemSpawn.add(map);
		StoneGolemSpawn.add(map);
		MineshaftGolemSpawn.add(map);

		// --- COPPER 铜制级（基准难度，铜装备）---
		// BASIC: 铜制近战+远程混编
		map.add(GolemDungeons.TRIAL, COPPER_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.COPPER_ROOM_BASIC)
				.add(of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
		);

		// LARGE: 大型兵主力
		map.add(GolemDungeons.TRIAL, COPPER_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.COPPER_ROOM_LARGE)
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
		);

		// RANGED: 远程火力+药水支援
		map.add(GolemDungeons.TRIAL, COPPER_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.COPPER_ROOM_RANGED)
				.add(of(FactoryGolemSpawn.HUMANOID_RANGED, 3), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
		);

		// RIDER: 火箭骑兵突击
		map.add(GolemDungeons.TRIAL, COPPER_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.COPPER_ROOM_RIDER)
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1), of(FactoryGolemSpawn.LARGE_2, 1))
		);

		// MIXED: 全兵种混合
		map.add(GolemDungeons.TRIAL, COPPER_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.COPPER_ROOM_MIXED)
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(FactoryGolemSpawn.HUMANOID_ROCKET, 2), of(FactoryGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.LARGE_2, 1))
		);

		map.add(GolemDungeons.TRIAL, COPPER_QUAD, new TrialConfig().setReward(MazeRoomLootGen.COPPER_QUAD)
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

		map.add(GolemDungeons.TRIAL, COPPER_BOSS, new TrialConfig().setReward(MazeRoomLootGen.COPPER_BOSS)
				.add(of(FactoryGolemSpawn.LARGE_1, 1), of(FactoryGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_2, 1), of(FactoryGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(FactoryGolemSpawn.LARGE_2, 2),
						of(FactoryGolemSpawn.HUMANOID_RANGED, 1),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1),
						of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
		);

		// --- DEEPSLATE 深板岩级（工厂+猪灵混编）---
		// BASIC: 猪灵近战+工厂基础
		map.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_ROOM_BASIC)
				.add(of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_BASIC, 2))
		);

		// LARGE: 双方大型兵混编
		map.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_ROOM_LARGE)
				.add(of(PiglinGolemSpawn.LARGE, 1), of(FactoryGolemSpawn.LARGE_2, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 1))
		);

		// RANGED: 远程火力网
		map.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_ROOM_RANGED)
				.add(of(PiglinGolemSpawn.HUMANOID_RANGED, 3), of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
		);

		// RIDER: 肩炮大型+火箭骑兵
		map.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_ROOM_RIDER)
				.add(of(PiglinGolemSpawn.LARGE_SHOULDER, 1), of(FactoryGolemSpawn.HUMANOID_ROCKET, 2))
		);

		// MIXED: 猪灵+工厂全兵种混合
		map.add(GolemDungeons.TRIAL, DEEPSLATE_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_ROOM_MIXED)
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
		);

		map.add(GolemDungeons.TRIAL, DEEPSLATE_QUAD, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_QUAD)
				.add(of(PiglinGolemSpawn.LARGE, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2),
						of(FactoryGolemSpawn.HUMANOID_TIPPED, 1))
				.add(of(PiglinGolemSpawn.LARGE_BOW, 1), of(PiglinGolemSpawn.LARGE, 1),
						of(PiglinGolemSpawn.HUMANOID_RANGED, 2), of(FactoryGolemSpawn.HUMANOID_ROCKET, 1))
				.add(of(PiglinGolemSpawn.LARGE_SHOULDER, 1), of(PiglinGolemSpawn.LARGE, 2),
						of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		map.add(GolemDungeons.TRIAL, DEEPSLATE_BOSS, new TrialConfig().setReward(MazeRoomLootGen.DEEPSLATE_BOSS)
				.add(of(FactoryGolemSpawn.LARGE_2, 2), of(PiglinGolemSpawn.HUMANOID_MELEE, 1))
				.add(of(FactoryGolemSpawn.LARGE_3, 1), of(PiglinGolemSpawn.HUMANOID_MELEE, 2), of(PiglinGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(PiglinGolemSpawn.LARGE, 2), of(PiglinGolemSpawn.HUMANOID_MELEE, 3), of(PiglinGolemSpawn.HUMANOID_RANGED, 2))
		);

		// --- SCULK 幽匿级（最高难度，钻石装备）---
		// BASIC: 幽匿近战+远程基础
		map.add(GolemDungeons.TRIAL, SCULK_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_BASIC)
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);

		// LARGE: 大型幽匿兵
		map.add(GolemDungeons.TRIAL, SCULK_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_LARGE)
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_MELEE, 1))
		);

		// RANGED: 远程+精英
		map.add(GolemDungeons.TRIAL, SCULK_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_RANGED)
				.add(of(SculkGolemSpawn.HUMANOID_RANGED, 3), of(SculkGolemSpawn.SCULK_ALL, 1))
		);

		// RIDER: 精英骑兵
		map.add(GolemDungeons.TRIAL, SCULK_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_RIDER)
				.add(of(SculkGolemSpawn.SCULK_ALL, 2), of(SculkGolemSpawn.SCULK_BETTER, 1))
		);

		// MIXED: 幽匿全兵种混合
		map.add(GolemDungeons.TRIAL, SCULK_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_MIXED)
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 2), of(SculkGolemSpawn.SCULK_ALL, 1))
				.add(of(SculkGolemSpawn.SCULK_BETTER, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
		);


		map.add(GolemDungeons.TRIAL, SCULK_QUAD, new TrialConfig().setReward(MazeRoomLootGen.SCULK_QUAD)
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.SCULK_ALL, 1), of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 3))
				.add(of(SculkGolemSpawn.SCULK_ALL, 3), of(SculkGolemSpawn.SCULK_BETTER, 1))
		);

		map.add(GolemDungeons.TRIAL, SCULK_BOSS, new TrialConfig().setReward(MazeRoomLootGen.SCULK_BOSS)
				.add(of(SculkGolemSpawn.HUMANOID_MELEE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 1))
				.add(of(SculkGolemSpawn.LARGE, 1), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
				.add(of(SculkGolemSpawn.LARGE, 2), of(SculkGolemSpawn.HUMANOID_MELEE, 2), of(SculkGolemSpawn.HUMANOID_RANGED, 2))
		);
	}

}