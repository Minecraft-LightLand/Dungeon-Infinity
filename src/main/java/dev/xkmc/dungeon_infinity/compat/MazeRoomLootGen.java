package dev.xkmc.dungeon_infinity.compat;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.init.reg.GDItems;
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

	public static void genLoot(RegistrateLootTableProvider pvd) {

		// ==================== STONE ====================

		// BASIC: 基础均衡掉落，金铁+食物+木头
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM_BASIC,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 1, 1, 10))
								.add(weight(Items.IRON_INGOT, 1, 3, 90))
						)
						.withPool(single(Items.BREAD, 1, 3))
						.withPool(single(Items.OAK_LOG, 2, 4))
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(1 / 16f))
								.add(weight(GolemItems.QUARTZ.get(), 1))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 2, 3))
						.withPool(single(Items.EMERALD, 1, 2))
		));

		// LARGE: 大型兵掉落，金属块+钻石+护甲材料
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM_LARGE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 1, 2, 20))
								.add(weight(Items.IRON_INGOT, 2, 3, 80))
						)
						.withPool(single(Items.BAKED_POTATO, 2, 4))
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
								.add(LootTableTemplate.getItem(Items.CLAY, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
								.add(weight(GolemItems.GOLD.get(), 1))
								.add(weight(GolemItems.DIAMOND.get(), 1))
								.add(weight(GolemItems.QUARTZ.get(), 3))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 3, 5))
						.withPool(single(Items.EMERALD, 1, 3))
		));

		// RANGED: 远程掉落，大量箭矢
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM_RANGED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 1, 2, 20))
								.add(weight(Items.IRON_INGOT, 2, 3, 80))
						)
						.withPool(single(Items.BAKED_POTATO, 2, 4))
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
								.add(LootTableTemplate.getItem(Items.CLAY, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
								.add(weight(GolemItems.GOLD.get(), 1))
								.add(weight(GolemItems.WEAK.get(), 1))
								.add(weight(GolemItems.WITHER.get(), 1))
								.add(weight(GolemItems.SLOW.get(), 1))
								.add(weight(GolemItems.SPEED.get(), 1))
						)
						.withPool(single(Items.ARROW, 12, 24))
						.withPool(single(Items.EXPERIENCE_BOTTLE, 3, 5))
						.withPool(single(Items.EMERALD, 1, 3))
		));

		// RIDER: 骑兵掉落，火药+皮革+金胡萝卜
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM_RIDER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 1, 2, 20))
								.add(weight(Items.IRON_INGOT, 2, 3, 80))
						)
						.withPool(single(Items.GOLDEN_CARROT, 1, 2))
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
								.add(LootTableTemplate.getItem(Items.CLAY, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(5 / 16f))
								.add(weight(GolemItems.GOLD.get(), 1))
								.add(weight(GolemItems.MOUNT_UPGRADE.get(), 3))
								.add(weight(GolemItems.SPEED.get(), 1))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 3, 5))
						.withPool(single(Items.EMERALD, 1, 3))
		));

		// MIXED: 混合掉落，各类型汇集+额外奖励
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM_MIXED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 1, 3, 20))
								.add(weight(Items.IRON_INGOT, 3, 5, 80))
						)
						.withPool(single(Items.COOKED_BEEF, 3, 5))
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
								.add(LootTableTemplate.getItem(Items.CLAY, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(7 / 16f))
								.add(weight(GolemItems.GOLD.get(), 2))
								.add(weight(GolemItems.DIAMOND.get(), 2))
								.add(weight(GolemItems.SPEED.get(), 2))
								.add(weight(GolemItems.SIZE_UPGRADE.get(), 1))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 4, 6))
						.withPool(single(Items.EMERALD, 2, 4))
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(weight(Items.GOLD_INGOT, 2, 4, 50))
								.add(weight(Items.IRON_INGOT, 6, 8, 50))
						)
						.withPool(single(Items.COOKED_BEEF, 3, 5))
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
								.add(LootTableTemplate.getItem(Items.CLAY, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(7 / 16f))
								.add(weight(GolemItems.GOLD.get(), 2))
								.add(weight(GolemItems.DIAMOND.get(), 2))
								.add(weight(GolemItems.PICKUP.get(), 3))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 4, 6))
						.withPool(single(Items.EMERALD, 4, 6))
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_BOSS,
				LootTable.lootTable()
						.withPool(single(Items.GOLD_INGOT, 4, 6))
						.withPool(single(Items.IRON_INGOT, 6, 10))
						.withPool(single(Items.DIAMOND, 2, 3))
						.withPool(single(Items.COOKED_BEEF, 3, 5))
						.withPool(single(Items.LAPIS_LAZULI, 3, 5))
						.withPool(LootPool.lootPool()
								.add(weight(GolemItems.RECYCLE.get(), 1))
								.add(weight(GolemItems.BELL.get(), 1))
								.add(weight(GolemItems.GOLD.get(), 1))
								.add(weight(GolemItems.DIAMOND.get(), 1))
								.add(weight(GolemItems.PICKUP.get(), 1))
						)
						.withPool(single(Items.EXPERIENCE_BOTTLE, 4, 6))
						.withPool(single(Items.EMERALD, 6, 10))
		));

		// ==================== MINESHAFT ====================

		// BASIC: 铜铁均衡掉落
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM_BASIC,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.COPPER_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 2, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 1, 3))
						)
		));

		// LARGE: 铜块+钻石+PICKUP升级
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM_LARGE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 1, 3))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.PICKUP.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 4))
						)
		));

		// RANGED: 大量箭矢+火药+烈焰棒
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM_RANGED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.COPPER_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.ARROW, 12, 24))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 1, 3))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 4))
						)
		));

		// RIDER: 火药+TNT+末影珍珠
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM_RIDER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 1, 2))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 3, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 6))
								.add(LootTableTemplate.getItem(Items.TNT, 1, 3))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.SLOW.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 5))
						)
		));

		// MIXED: 混合掉落，各类型全覆盖
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM_MIXED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 5, 12))
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 1, 3))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 8, 18))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 1, 3))
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
								.add(LootItem.lootTableItem(GolemItems.PICKUP.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 12, 20))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 32, 64))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.SIZE_UPGRADE.get()))
								.add(LootItem.lootTableItem(GolemItems.SLOW.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 8, 12))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 10, 20))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 8, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.DIAMOND.get()))
								.add(LootItem.lootTableItem(GolemItems.SIZE_UPGRADE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.SLOW.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 10, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 8, 12))
						)
		));

		// ==================== COPPER ====================

		// BASIC: 铜金铁均衡
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM_BASIC,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 2, 6))
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 3, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.QUARTZ.get()))
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 4))
						)
		));

		// LARGE: 铜块+钻石+烈焰棒
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM_LARGE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 5))
						)
		));

		// RANGED: 大量箭矢+药水材料
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM_RANGED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 2, 6))
								.add(LootTableTemplate.getItem(Items.ARROW, 12, 24))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 4))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 2, 5))
								.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.QUARTZ.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// RIDER: 火药+TNT+末影珍珠
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM_RIDER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 3, 8))
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 3, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 6))
								.add(LootTableTemplate.getItem(Items.TNT, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 4, 8))
								.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.QUARTZ.get()))
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// MIXED: 混合掉落，各类型全覆盖
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM_MIXED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 4, 10))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 8, 18))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 5))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 2, 4))
								.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 1, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
								.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.QUARTZ.get()))
								.add(LootItem.lootTableItem(GolemItems.GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 32, 64))
								.add(LootItem.lootTableItem(GolemItems.BEACON_CANNON.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.TALENTED.get()))
								.add(LootItem.lootTableItem(GolemItems.CAULDRON.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.WEAK.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GDItems.FLAME_SWORD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BLAZE_ROD, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENDER_PEARL, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.ADD_DIAMOND.get()))
								.add(LootItem.lootTableItem(GolemItems.CAULDRON.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.WEAK.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 12, 18))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 5, 10))
						)
		));

		// ==================== DEEPSLATE ====================

		// BASIC: 铁块+钻石+NETHERITE升级
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM_BASIC,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 1, 3))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// LARGE: 下界合金碎片+金胡萝卜
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM_LARGE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 8))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
		));

		// RANGED: 箭矢+火药+萤石粉
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM_RANGED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 1, 3))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 5))
								.add(LootTableTemplate.getItem(Items.ARROW, 16, 32))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 6))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 6))
								.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
								.add(LootTableTemplate.getItem(Items.LEATHER, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// RIDER: 附魔金苹果+TNT+下界合金碎片
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM_RIDER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 4))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 8))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 8))
								.add(LootTableTemplate.getItem(Items.TNT, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
								.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
		));

		// MIXED: 混合掉落+下界合金碎片
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM_MIXED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 2, 5))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 12, 24))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 8))
								.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 6, 12))
								.add(LootTableTemplate.getItem(Items.LEATHER, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 5, 10))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_INGOT, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 5, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get()))
								.add(LootItem.lootTableItem(GolemItems.BEACON_CANNON.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 10, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_INGOT, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.SLICING_AXE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 8, 12))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get()))
								.add(LootItem.lootTableItem(GolemItems.BEACON_CANNON.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 14, 20))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 6, 12))
						)
		));

		// ==================== SCULK ====================

		// BASIC: 回响碎片+钻石+NETHERITE
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM_BASIC,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 1, 3))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// LARGE: 大量回响碎片+附魔金苹果
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM_LARGE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 5, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 3))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 7))
						)
		));

		// RANGED: 大量箭矢+附魔金苹果
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM_RANGED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 1, 3))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 10))
								.add(LootTableTemplate.getItem(Items.ARROW, 32, 64))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 3))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 7, 13))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 3, 6))
						)
		));

		// RIDER: 终极掉落
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM_RIDER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 5, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 5, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 8, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
		));

		// MIXED: 混合掉落+终极奖励
		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM_MIXED,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 6, 14))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 5))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 24, 48))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.OAK_LOG, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
								.add(LootItem.lootTableItem(GolemItems.ENCHANTED_GOLD.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 9, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 5, 10))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 16, 32))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 16, 32))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ARROW, 32, 64))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 12, 20))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.ADD_NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.BEACON_BOOTS.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 8, 16))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EXPERIENCE_BOTTLE, 12, 32))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.EMERALD, 32, 64))
						)
		));

	}

}