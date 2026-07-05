package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.dungeon_infinity.compat.room.*;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
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

	public static final Identifier DEEPEST_ROOM_BASIC = DungeonInfinity.loc("deepest/room/basic");
	public static final Identifier DEEPEST_ROOM_LARGE = DungeonInfinity.loc("deepest/room/large");
	public static final Identifier DEEPEST_ROOM_RANGED = DungeonInfinity.loc("deepest/room/ranged");
	public static final Identifier DEEPEST_ROOM_RIDER = DungeonInfinity.loc("deepest/room/rider");
	public static final Identifier DEEPEST_ROOM_MIXED = DungeonInfinity.loc("deepest/room/mixed");
	public static final Identifier DEEPEST_QUAD = DungeonInfinity.loc("deepest/quad");

	public static void gen(ConfigDataProvider.Collector map) {
		EarlyGolemSpawn.add(map);
		StoneGolemSpawn.add(map);
		MineshaftGolemSpawn.add(map);
		CopperGolemSpawn.add(map);
		DeepslateGolemSpawn.add(map);
		SculkGolemSpawn.add(map);
		DeepestGolemSpawn.add(map);
	}

}