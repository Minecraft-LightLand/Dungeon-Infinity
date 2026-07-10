package dev.xkmc.dungeon_infinity.compat;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.init.reg.GDItems;
import dev.xkmc.golemdungeons.init.reg.GDModifiers;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

@SuppressWarnings("deprecation")
public class MazeRoomLootGen {

	private static ResourceKey<LootTable> loot(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, DungeonInfinity.loc(path));
	}

	private static ResourceKey<LootTable> maze(String path) {
		return loot("maze/" + path);
	}

	// ===== STONE 石制级 =====
	public static final ResourceKey<LootTable> STONE_ROOM_BASIC = maze("stone/room/basic");
	public static final ResourceKey<LootTable> STONE_ROOM_LARGE = maze("stone/room/large");
	public static final ResourceKey<LootTable> STONE_ROOM_RANGED = maze("stone/room/ranged");
	public static final ResourceKey<LootTable> STONE_ROOM_RIDER = maze("stone/room/rider");
	public static final ResourceKey<LootTable> STONE_ROOM_MIXED = maze("stone/room/mixed");
	public static final ResourceKey<LootTable> STONE_QUAD = maze("stone/quad");
	public static final ResourceKey<LootTable> STONE_BOSS = maze("stone/boss");

	// ===== MINESHAFT 矿道级 =====
	public static final ResourceKey<LootTable> MINESHAFT_ROOM_BASIC = maze("mineshaft/room/basic");
	public static final ResourceKey<LootTable> MINESHAFT_ROOM_LARGE = maze("mineshaft/room/large");
	public static final ResourceKey<LootTable> MINESHAFT_ROOM_RANGED = maze("mineshaft/room/ranged");
	public static final ResourceKey<LootTable> MINESHAFT_ROOM_RIDER = maze("mineshaft/room/rider");
	public static final ResourceKey<LootTable> MINESHAFT_ROOM_MIXED = maze("mineshaft/room/mixed");
	public static final ResourceKey<LootTable> MINESHAFT_QUAD = maze("mineshaft/quad");
	public static final ResourceKey<LootTable> MINESHAFT_BOSS = maze("mineshaft/boss");

	// ===== COPPER 铜制级 =====
	public static final ResourceKey<LootTable> COPPER_ROOM_BASIC = maze("copper/room/basic");
	public static final ResourceKey<LootTable> COPPER_ROOM_LARGE = maze("copper/room/large");
	public static final ResourceKey<LootTable> COPPER_ROOM_RANGED = maze("copper/room/ranged");
	public static final ResourceKey<LootTable> COPPER_ROOM_RIDER = maze("copper/room/rider");
	public static final ResourceKey<LootTable> COPPER_ROOM_MIXED = maze("copper/room/mixed");
	public static final ResourceKey<LootTable> COPPER_QUAD = maze("copper/quad");
	public static final ResourceKey<LootTable> COPPER_BOSS = maze("copper/boss");

	// ===== DEEPSLATE 深板岩级 =====
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM_BASIC = maze("deepslate/room/basic");
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM_LARGE = maze("deepslate/room/large");
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM_RANGED = maze("deepslate/room/ranged");
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM_RIDER = maze("deepslate/room/rider");
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM_MIXED = maze("deepslate/room/mixed");
	public static final ResourceKey<LootTable> DEEPSLATE_QUAD = maze("deepslate/quad");
	public static final ResourceKey<LootTable> DEEPSLATE_BOSS = maze("deepslate/boss");

	// ===== SCULK 幽匿级 =====
	public static final ResourceKey<LootTable> SCULK_ROOM_BASIC = maze("sculk/room/basic");
	public static final ResourceKey<LootTable> SCULK_ROOM_LARGE = maze("sculk/room/large");
	public static final ResourceKey<LootTable> SCULK_ROOM_RANGED = maze("sculk/room/ranged");
	public static final ResourceKey<LootTable> SCULK_ROOM_RIDER = maze("sculk/room/rider");
	public static final ResourceKey<LootTable> SCULK_ROOM_MIXED = maze("sculk/room/mixed");
	public static final ResourceKey<LootTable> SCULK_QUAD = maze("sculk/quad");
	public static final ResourceKey<LootTable> SCULK_BOSS = maze("sculk/boss");

	private static LootPoolSingletonContainer.Builder<?> byChance(ItemLike item, float chance) {
		return LootItem.lootTableItem(item).when(LootTableTemplate.chance(chance));
	}

	private static LootPool.Builder single(ItemLike item, int min, int max) {
		return LootPool.lootPool().add(LootTableTemplate.getItem(item.asItem(), min, max));
	}

	private static LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight);
	}

	private static LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int min, int max, int weight) {
		return LootTableTemplate.getItem(item.asItem(), min, max).setWeight(weight);
	}

	private static void add(RegistrateLootTableProvider pvd, ResourceKey<LootTable> key, LootPool.Builder... pools) {
		var table = LootTable.lootTable();
		for (var e : pools) table.withPool(e);
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(key, table));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {

		// ==================== STONE ====================

		// BASIC: 基础均衡掉落，金铁+食物+木头
		add(pvd, STONE_ROOM_BASIC,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 1, 10))
						.add(weight(Items.IRON_INGOT, 1, 3, 90)),
				single(Items.BREAD, 1, 3),
				single(Items.OAK_LOG, 2, 4),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(1 / 16f))
						.add(weight(GolemItems.QUARTZ.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 2, 3),
				single(Items.EMERALD, 1, 2)
		);

		// LARGE: 大型兵掉落，金属块+钻石+护甲材料
		add(pvd, STONE_ROOM_LARGE,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 2, 20))
						.add(weight(Items.IRON_INGOT, 2, 3, 80)),
				single(Items.BAKED_POTATO, 2, 4),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 3)),
				single(Items.EXPERIENCE_BOTTLE, 3, 5),
				single(Items.EMERALD, 1, 3)
		);

		// RANGED: 远程掉落，大量箭矢
		add(pvd, STONE_ROOM_RANGED,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 2, 20))
						.add(weight(Items.IRON_INGOT, 2, 3, 80)),
				single(Items.BAKED_POTATO, 2, 4),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 1)),
				single(Items.ARROW, 12, 24),
				single(Items.EXPERIENCE_BOTTLE, 3, 5),
				single(Items.EMERALD, 1, 3)
		);

		// RIDER: 骑兵掉落，火药+皮革+金胡萝卜
		add(pvd, STONE_ROOM_RIDER,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 2, 20))
						.add(weight(Items.IRON_INGOT, 2, 3, 80)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 3))
						.add(weight(GolemItems.SPEED.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 3, 5),
				single(Items.EMERALD, 1, 3)
		);

		// MIXED: 混合掉落，各类型汇集+额外奖励
		add(pvd, STONE_ROOM_MIXED,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 3, 20))
						.add(weight(Items.IRON_INGOT, 3, 5, 80)),
				single(Items.COOKED_BEEF, 3, 5),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(7 / 16f))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.DIAMOND.get(), 2))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 4, 6),
				single(Items.EMERALD, 2, 4)
		);

		add(pvd, STONE_QUAD,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 2, 4, 50))
						.add(weight(Items.IRON_INGOT, 6, 8, 50)),
				single(Items.COOKED_BEEF, 3, 5),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 16, 24)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(7 / 16f))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.DIAMOND.get(), 2))
						.add(weight(GolemItems.PICKUP.get(), 3)),
				single(Items.EXPERIENCE_BOTTLE, 6, 10),
				single(Items.EMERALD, 4, 6)
		);

		add(pvd, MINESHAFT_BOSS,
				single(Items.GOLD_INGOT, 4, 6),
				single(Items.IRON_INGOT, 6, 10),
				single(Items.DIAMOND, 2, 3),
				single(Items.COOKED_BEEF, 3, 5),
				single(Items.LAPIS_LAZULI, 3, 5),
				LootPool.lootPool()
						.add(weight(GolemItems.RECYCLE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 8, 12),
				single(Items.EMERALD, 6, 10)
		);

		// ==================== MINESHAFT ====================

		add(pvd, MINESHAFT_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 2, 4),
				single(Items.OAK_LOG, 3, 5),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(4 / 16f))
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 4, 6),
				single(Items.EMERALD, 2, 4)
		);

		add(pvd, MINESHAFT_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 3, 5),
				LootTableTemplate.getPool(2, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(4 / 8f))
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 6, 10),
				single(Items.EMERALD, 3, 6)
		);

		add(pvd, MINESHAFT_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 3, 5),
				LootTableTemplate.getPool(2, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(8 / 16f))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.CAULDRON.get(), 1))
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1)),
				single(Items.ARROW, 16, 32),
				single(Items.EXPERIENCE_BOTTLE, 6, 10),
				single(Items.EMERALD, 3, 6)
		);

		add(pvd, MINESHAFT_ROOM_RIDER,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(2, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(5 / 8f))
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 6, 10),
				single(Items.EMERALD, 3, 6)
		);

		add(pvd, MINESHAFT_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.GOLDEN_CARROT, 2, 3),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 8, 12),
				single(Items.EMERALD, 4, 8)
		);

		add(pvd, MINESHAFT_QUAD,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 50))
						.add(weight(Items.DIAMOND, 1, 1, 50)),
				single(Items.GOLDEN_CARROT, 2, 4),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.CLAY_BALL, 12, 20)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 2))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.PICKUP.get(), 2))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 10, 16),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_BOSS,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.DIAMOND, 3, 5),
				single(Items.GOLDEN_CARROT, 2, 4),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 12, 18),
				single(Items.EMERALD, 10, 16),
				single(GDItems.ANCIENT_FORGE, 1,1)
		);

		// ==================== COPPER ====================

		add(pvd, COPPER_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 2, 4),
				LootTableTemplate.getPool(1, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(4 / 8f))
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 6, 10),
				single(Items.EMERALD, 4, 8)
		);

		add(pvd, COPPER_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 3, 5),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 10, 16),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 3, 5),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.CAULDRON.get(), 1))
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 10, 16),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_RIDER,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.GOLDEN_CARROT, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 10, 16),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.GOLDEN_CARROT, 3, 5),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 12, 20),
				single(Items.EMERALD, 8, 12)
		);


		add(pvd, DEEPSLATE_BOSS,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.DIAMOND, 4, 8),
				single(Items.GOLDEN_CARROT, 3, 5),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(Items.EXPERIENCE_BOTTLE, 12, 20),
				single(Items.EMERALD, 10, 16)
		);

		// ==================== DEEPSLATE ====================

		// BASIC: 铁块+钻石+NETHERITE升级
		add(pvd, DEEPSLATE_ROOM_BASIC,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 1, 3))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 6)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))

		);

		// LARGE: 下界合金碎片+金胡萝卜
		add(pvd, DEEPSLATE_ROOM_LARGE,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 4))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 8))
						.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 3)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						.add(LootTableTemplate.getItem(Items.LEATHER, 1, 3)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 14)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))

		);

		// RANGED: 箭矢+火药+萤石粉
		add(pvd, DEEPSLATE_ROOM_RANGED,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 1, 3))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 5))
						.add(LootTableTemplate.getItem(Items.ARROW, 16, 32)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 6))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 6))
						.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						.add(LootTableTemplate.getItem(Items.LEATHER, 1, 2)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))

		);

		// RIDER: 附魔金苹果+TNT+下界合金碎片
		add(pvd, DEEPSLATE_ROOM_RIDER,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 4))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 8))
						.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 8))
						.add(LootTableTemplate.getItem(Items.TNT, 2, 4)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 14)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))

		);

		// MIXED: 混合掉落+下界合金碎片
		add(pvd, DEEPSLATE_ROOM_MIXED,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 5))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
						.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ARROW, 12, 24))
						.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 8))
						.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2, 5)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 6, 12))
						.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 16)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 5, 10))

		);

		add(pvd, DEEPSLATE_QUAD,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_INGOT, 4, 8)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
								.when(LootItemRandomChanceCondition.randomChance(0.5f))),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 5, 8))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 2)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get()))
						.add(LootItem.lootTableItem(GolemItems.BEACON_CANNON.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 10, 16)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))

		);

		add(pvd, SCULK_BOSS,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_INGOT, 4, 8)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2, 4)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.SLICING_AXE.get())),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 8, 12))
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2, 4)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get()))
						.add(LootItem.lootTableItem(GolemItems.BEACON_CANNON.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 14, 20)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 6, 12))

		);

		// ==================== SCULK ====================

		// BASIC: 回响碎片+钻石+NETHERITE
		add(pvd, SCULK_ROOM_BASIC,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 1, 3))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))

		);

		// LARGE: 大量回响碎片+附魔金苹果
		add(pvd, SCULK_ROOM_LARGE,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 5, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 3))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 3)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 14)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 3, 7))

		);

		// RANGED: 大量箭矢+附魔金苹果
		add(pvd, SCULK_ROOM_RANGED,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 1, 3))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
						.add(LootTableTemplate.getItem(Items.ARROW, 32, 64)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 3))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 13)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))

		);

		// RIDER: 终极掉落
		add(pvd, SCULK_ROOM_RIDER,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 5, 12)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 4))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 14)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))

		);

		// MIXED: 混合掉落+终极奖励
		add(pvd, SCULK_ROOM_MIXED,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
						.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 14)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 5))
						.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ARROW, 24, 48)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 6, 12)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 9, 16)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 5, 10))

		);

		add(pvd, SCULK_QUAD,
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 16, 32)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.DIAMOND, 16, 32)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ARROW, 32, 64)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
								.when(LootItemRandomChanceCondition.randomChance(0.5f))),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 12, 20)),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.ADD_NETHERITE.get())),
				LootPool.lootPool()
						.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get())),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 8, 16)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 12, 32)),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.EMERALD, 32, 64))

		);

	}

}