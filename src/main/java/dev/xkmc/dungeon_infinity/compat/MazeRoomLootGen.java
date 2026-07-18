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
				single(Items.APPLE, 1, 3),
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
				single(Items.APPLE, 2, 4),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
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
				single(Items.APPLE, 2, 4),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
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

		add(pvd, STONE_ROOM_RIDER,
				LootPool.lootPool()
						.add(weight(Items.GOLD_INGOT, 1, 2, 20))
						.add(weight(Items.IRON_INGOT, 2, 3, 80)),
				single(Items.APPLE, 2, 4),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
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
				single(Items.BAKED_POTATO, 2, 3),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
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
				single(Items.BAKED_POTATO, 2, 3),
				LootPool.lootPool()
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(7 / 16f))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.DIAMOND.get(), 2))
						.add(weight(GolemItems.PICKUP.get(), 3)),
				single(GDItems.XP_PILL, 1, 1),
				single(Items.EMERALD, 4, 6)
		);

		add(pvd, MINESHAFT_BOSS,
				single(Items.GOLD_INGOT, 4, 6),
				single(Items.IRON_INGOT, 6, 10),
				single(Items.DIAMOND, 2, 3),
				single(Items.BAKED_POTATO, 2, 3),
				single(Items.LAPIS_LAZULI, 3, 5),
				LootPool.lootPool()
						.add(weight(GolemItems.RECYCLE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1)),
				single(GDItems.XP_PILL, 1, 2),
				single(Items.EMERALD, 6, 10)
		);

		// ==================== MINESHAFT ====================

		add(pvd, MINESHAFT_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 2, 3),
				single(Items.OAK_LOG, 3, 5),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(4 / 16f))
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 1, 1),
				single(Items.EMERALD, 2, 4)
		);

		add(pvd, MINESHAFT_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 2, 3),
				LootTableTemplate.getPool(2, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(4 / 8f))
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 1, 1),
				single(Items.EMERALD, 3, 6)
		);

		add(pvd, MINESHAFT_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.BAKED_POTATO, 2, 3),
				LootTableTemplate.getPool(2, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(8 / 16f))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.CAULDRON.get(), 1))
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1)),
				single(Items.ARROW, 16, 32),
				single(GDItems.XP_PILL, 1, 1),
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
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
				LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(5 / 8f))
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(GDItems.XP_PILL, 1, 1),
				single(Items.EMERALD, 3, 6)
		);

		add(pvd, MINESHAFT_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 1, 2),
				single(Items.EMERALD, 4, 8)
		);

		add(pvd, MINESHAFT_QUAD,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 50))
						.add(weight(Items.DIAMOND, 1, 1, 50)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 3, 5)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 2))
						.add(weight(GolemItems.GOLD.get(), 2))
						.add(weight(GolemItems.PICKUP.get(), 2))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 2, 2),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_BOSS,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 3, 5, 30))
						.add(weight(Items.IRON_INGOT, 2, 5, 60)),
				single(Items.DIAMOND, 3, 5),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 0)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.EMERALD, 10, 16),
				single(GDItems.ANCIENT_FORGE, 1, 1)
		);

		// ==================== COPPER ====================

		add(pvd, COPPER_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 2, 3),
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
				single(GDItems.XP_PILL, 1, 2),
				single(Items.EMERALD, 4, 8)
		);

		add(pvd, COPPER_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 2, 2),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.COOKED_BEEF, 2, 3),
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
				single(GDItems.XP_PILL, 2, 2),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_RIDER,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.DIAMOND.get(), 1))
						.add(weight(GolemItems.SPEED.get(), 2))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(GDItems.XP_PILL, 2, 2),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, COPPER_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.EMERALD, 8, 12)
		);

		add(pvd, COPPER_QUAD,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.DIAMOND, 1, 2, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.GOLDEN_APPLE, 2, 3),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.TALENTED.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 2, 4),
				single(Items.EMERALD, 10, 16)
		);

		add(pvd, DEEPSLATE_BOSS,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 2, 3, 10))
						.add(weight(Items.COPPER_INGOT, 4, 6, 20))
						.add(weight(Items.IRON_INGOT, 4, 6, 60)),
				single(Items.DIAMOND, 4, 8),
				single(Items.NETHERITE_INGOT, 1, 2),
				single(Items.ENCHANTED_GOLDEN_APPLE, 1, 1),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.FLAME_SWORD, 1, 1),
				single(GDItems.XP_PILL, 3, 4),
				single(Items.EMERALD, 12, 20)
		);

		// ==================== DEEPSLATE ====================

		add(pvd, DEEPSLATE_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(1, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.QUARTZ.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 2, 2),
				single(Items.EMERALD, 6, 10)
		);

		add(pvd, DEEPSLATE_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1))
						.add(weight(GolemItems.NETHERITE.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, DEEPSLATE_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1))
						.add(weight(GolemItems.CAULDRON.get(), 1))
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, DEEPSLATE_ROOM_RIDER,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1))
						.add(weight(GolemItems.PICKUP.get(), 1))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, DEEPSLATE_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1))
						.add(weight(GolemItems.RECYCLE.get(), 1)),
				single(GDItems.XP_PILL, 2, 4),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, DEEPSLATE_QUAD,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_APPLE, 2, 3),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1))
						.add(weight(GolemItems.TALENTED.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1)),
				single(GDItems.XP_PILL, 3, 4),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, SCULK_BOSS,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.DIAMOND, 4, 8),
				single(Items.NETHERITE_INGOT, 1, 2),
				single(Items.ENCHANTED_GOLDEN_APPLE, 1, 1),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.TALENTED.get(), 1))
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1)),
				single(GDItems.SCULK_SCYTHE, 1, 1),
				single(GDItems.XP_PILL, 4, 5),
				single(Items.NETHERITE_INGOT, 2, 2)
		);

		// ==================== SCULK ====================

		add(pvd, SCULK_ROOM_BASIC,
				LootTableTemplate.getPool(2, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(1, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.SPEED.get(), 1))
						.add(weight(GolemItems.ADD_DIAMOND.get(), 1))
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.NETHERITE.get(), 1)),
				single(GDItems.XP_PILL, 2, 3),
				single(Items.DIAMOND, 1, 2)
		);

		add(pvd, SCULK_ROOM_LARGE,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.GOLD.get(), 1))
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1))
						.add(weight(GolemItems.NETHERITE.get(), 1))
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1)),
				single(GDItems.XP_PILL, 3, 4),
				single(Items.DIAMOND, 2, 3)
		);

		add(pvd, SCULK_ROOM_RANGED,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.COOKED_BEEF, 2, 3),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1))
						.add(weight(GolemItems.CAULDRON.get(), 3)),
				LootPool.lootPool()
						.add(weight(GolemItems.WEAK.get(), 1))
						.add(weight(GolemItems.WITHER.get(), 1))
						.add(weight(GolemItems.SLOW.get(), 1)),
				single(GDItems.XP_PILL, 3, 4),
				single(Items.DIAMOND, 2, 3)
		);

		add(pvd, SCULK_ROOM_RIDER,
				LootTableTemplate.getPool(3, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(2, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1))
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GolemItems.MOUNT_UPGRADE.get(), 1))
						.add(weight(GolemItems.SIZE_UPGRADE.get(), 1)),
				single(GDItems.XP_PILL, 3, 4),
				single(Items.DIAMOND, 2, 3)
		);

		add(pvd, SCULK_ROOM_MIXED,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_CARROT, 1, 2),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1)),
				LootPool.lootPool()
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1)),
				single(GDItems.XP_PILL, 4, 5),
				single(Items.DIAMOND, 3, 4)
		);

		add(pvd, SCULK_QUAD,
				LootTableTemplate.getPool(4, 0)
						.add(weight(Items.GOLD_INGOT, 3, 5, 30))
						.add(weight(Items.DIAMOND, 2, 3, 20))
						.add(weight(Items.COPPER_INGOT, 6, 8, 20))
						.add(weight(Items.IRON_INGOT, 6, 8, 30)),
				single(Items.GOLDEN_APPLE, 2, 3),
				LootTableTemplate.getPool(3, 1)
						.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 5))
						.add(LootTableTemplate.getItem(Items.TNT, 3, 5))
						.add(LootTableTemplate.getItem(GolemItems.GOLEM_TEMPLATE.get(), 4, 6)),
				LootPool.lootPool()
						.add(weight(GDModifiers.ITEM_RESISTANCE.get(), 1))
						.add(weight(GDModifiers.ITEM_REFORGE.get(), 1)),
				LootPool.lootPool()
						.add(weight(GolemItems.ENCHANTED_GOLD.get(), 1))
						.add(weight(GolemItems.TALENTED.get(), 1))
						.add(weight(GolemItems.ADD_NETHERITE.get(), 1)),
				single(GDItems.XP_PILL, 5, 6),
				single(Items.DIAMOND, 3, 4)
		);

	}

}