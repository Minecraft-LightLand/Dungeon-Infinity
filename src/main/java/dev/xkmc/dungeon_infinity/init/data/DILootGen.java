package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.init.reg.GDItems;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class DILootGen {

	private static ResourceKey<LootTable> replace(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, DungeonInfinity.loc(path));
	}

	// ===== STONE 石制级 =====
	public static final ResourceKey<LootTable> STONE_ROOM = replace("stone/room");
	public static final ResourceKey<LootTable> STONE_QUAD = replace("stone/quad");
	public static final ResourceKey<LootTable> STONE_STAIR = replace("stone/stair");
	public static final ResourceKey<LootTable> STONE_BOSS = replace("stone/boss");

	// ===== MINESHAFT 矿道级 =====
	public static final ResourceKey<LootTable> MINESHAFT_ROOM = replace("mineshaft/room");
	public static final ResourceKey<LootTable> MINESHAFT_QUAD = replace("mineshaft/quad");
	public static final ResourceKey<LootTable> MINESHAFT_STAIR = replace("mineshaft/stair");
	public static final ResourceKey<LootTable> MINESHAFT_BOSS = replace("mineshaft/boss");

	// ===== COPPER 铜制级 =====
	public static final ResourceKey<LootTable> COPPER_ROOM = replace("copper/room");
	public static final ResourceKey<LootTable> COPPER_QUAD = replace("copper/quad");
	public static final ResourceKey<LootTable> COPPER_STAIR = replace("copper/stair");
	public static final ResourceKey<LootTable> COPPER_BOSS = replace("copper/boss");

	// ===== DEEPSLATE 深板岩级 =====
	public static final ResourceKey<LootTable> DEEPSLATE_ROOM = replace("deepslate/room");
	public static final ResourceKey<LootTable> DEEPSLATE_QUAD = replace("deepslate/quad");
	public static final ResourceKey<LootTable> DEEPSLATE_STAIR = replace("deepslate/stair");
	public static final ResourceKey<LootTable> DEEPSLATE_BOSS = replace("deepslate/boss");

	// ===== SCULK 幽匿级 =====
	public static final ResourceKey<LootTable> SCULK_ROOM = replace("sculk/room");
	public static final ResourceKey<LootTable> SCULK_QUAD = replace("sculk/quad");
	public static final ResourceKey<LootTable> SCULK_STAIR = replace("sculk/stair");
	public static final ResourceKey<LootTable> SCULK_BOSS = replace("sculk/boss");

	public static void genLoot(RegistrateLootTableProvider pvd) {

		// ==================== STONE ====================

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_ROOM,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLD_NUGGET, 4, 10))
								.add(LootTableTemplate.getItem(Items.BONE, 2, 5))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BREAD, 2, 4))
								.add(LootTableTemplate.getItem(Items.APPLE, 2, 4))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 2, 5))
								.add(LootTableTemplate.getItem(Items.ARROW, 6, 12))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_PORKCHOP, 2, 4))
								.add(LootTableTemplate.getItem(Items.BREAD, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_STAIR,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 3, 7))
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
								.add(LootTableTemplate.getItem(Items.COOKED_PORKCHOP, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(STONE_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 5, 10))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.GOLD_BLOCK, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 1, 2))
						)
		));

		// ==================== MINESHAFT ====================

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_ROOM,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_NUGGET, 5, 12))
								.add(LootTableTemplate.getItem(Items.COAL, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.BAKED_POTATO, 2, 5))
								.add(LootTableTemplate.getItem(Items.CARROT, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 2, 5))
								.add(LootTableTemplate.getItem(Items.REDSTONE, 4, 10))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 2, 4))
								.add(LootTableTemplate.getItem(Items.BAKED_POTATO, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_STAIR,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 3, 7))
								.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 4, 8))
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.75f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 6))
								.add(LootItem.lootTableItem(Items.GOLDEN_CARROT))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(MINESHAFT_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 5, 10))
								.add(LootTableTemplate.getItem(Items.EMERALD, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLD_INGOT, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 1, 2))
						)
		));

		// ==================== COPPER ====================

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_ROOM,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_INGOT, 4, 8))
								.add(LootTableTemplate.getItem(Items.AMETHYST_SHARD, 3, 6))
								.add(LootTableTemplate.getItem(Items.GUNPOWDER, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 3, 5))
								.add(LootTableTemplate.getItem(Items.MUSHROOM_STEW, 1, 2))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COPPER_BLOCK, 1, 3))
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 5))
								.add(LootTableTemplate.getItem(Items.IRON_INGOT, 3, 6))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 7))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_STAIR,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 1, 3))
								.add(LootTableTemplate.getItem(Items.OBSIDIAN, 2, 5))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.75f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(COPPER_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 6))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GDItems.MEDAL_OF_CONQUEROR.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 6, 10))
								.add(LootTableTemplate.getItem(Items.GOLDEN_APPLE, 2, 3))
						)
		));

		// ==================== DEEPSLATE ====================

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_ROOM,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.IRON_BLOCK, 1, 2))
								.add(LootTableTemplate.getItem(Items.EMERALD, 2, 5))
								.add(LootTableTemplate.getItem(Items.OBSIDIAN, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 4, 7))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 2))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 4))
								.add(LootTableTemplate.getItem(Items.CRYING_OBSIDIAN, 2, 4))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 5, 8))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_STAIR,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 3, 6))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.75f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 6, 10))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 3, 6))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(DEEPSLATE_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 8))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GDItems.TRIAL_MEDAL.get()))
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.COOKED_BEEF, 8, 12))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
		));

		// ==================== SCULK ====================

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_ROOM,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 2, 5))
								.add(LootTableTemplate.getItem(Items.AMETHYST_BLOCK, 1, 3))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.CHORUS_FRUIT, 2, 4))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 1, 3))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_QUAD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 4, 8))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 2, 4))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.5f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.CHORUS_FRUIT, 3, 6))
								.add(LootTableTemplate.getItem(Items.GOLDEN_CARROT, 2, 4))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_STAIR,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 6, 12))
								.add(LootTableTemplate.getItem(Items.NETHERITE_SCRAP, 2, 4))
								.add(LootTableTemplate.getItem(Items.DIAMOND, 4, 8))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get())
										.when(LootItemRandomChanceCondition.randomChance(0.75f)))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.CHORUS_FRUIT, 4, 8))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 1, 2))
						)
		));

		pvd.addLootAction(LootContextParamSets.CHEST, sub -> sub.accept(SCULK_BOSS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ECHO_SHARD, 8, 16))
								.add(LootTableTemplate.getItem(Items.NETHERITE_INGOT, 1, 2))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 4))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GDItems.TRIAL_MEDAL.get()))
								.add(LootItem.lootTableItem(GolemItems.NETHERITE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(GolemItems.RECYCLE.get()))
						)
						.withPool(LootPool.lootPool()
								.add(LootTableTemplate.getItem(Items.CHORUS_FRUIT, 6, 10))
								.add(LootTableTemplate.getItem(Items.ENCHANTED_GOLDEN_APPLE, 2, 3))
						)
		));
	}

}
