package dev.xkmc.dungeon_infinity.init.data;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnData;
import dev.xkmc.dungeon_infinity.content.config.ColumnLayoutConfig;
import dev.xkmc.dungeon_infinity.content.config.ShopConfig;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.golemdungeons.init.reg.GDModifiers;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DIConfigGen extends ConfigDataProvider {

	private static CompletableFuture<HolderLookup.Provider> pvd;

	public DIConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Golem Spawn Config");
		this.pvd = pvd;
	}

	public static <T> Holder<T> resolve(ResourceKey<T> key) {
		try {
			return pvd.get().holderOrThrow(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ItemStackTemplate ench(ResourceKey<Enchantment> key, int lv) {
		var ench = pvd.getNow(null).getOrThrow(key);
		var mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		mutable.set(ench, lv);
		return new ItemStackTemplate(Items.ENCHANTED_BOOK, DataComponentPatch.builder()
				.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable()).build());
	}

	public void add(ConfigDataProvider.Collector map) {
		map.add(DungeonInfinity.COLUMN, DungeonInfinity.loc("preset"), new ColumnLayoutConfig()
				.column("preset")
				.style("sculk", 4)
				.style("deepslate", 3)
				.style("copper", 3)
				.style("mineshaft", 3)
				.style("stone", 3)
				.room("warehouse", 8)
				.room("workshop", 8)
				.room("shop", 8)
				.end()
		);
		GolemSpawnData.gen(map);
		genRooms(map);
		genShop(map);
	}

	private TemplateConfig.SpawnPool.Entry entry(int size, int y, Identifier room, int weight) {
		return new TemplateConfig.SpawnPool.Entry(new TemplateConfig.SpawnContext(size, y), room, weight);
	}

	// rooms
	private void genRooms(ConfigDataProvider.Collector map) {
		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("stone"), new TemplateConfig()
				.start("stone")
				.room("path/corner").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 70).variant("_waypoint", 30).end()
				.room("path/cross").variant("", 50).variant("_waypoint", 50).end()
				.spawn("room", 1, 1,
						entry(2, 15, GolemSpawnData.EARLY_ROOM_BASIC, 100),
						entry(3, 15, GolemSpawnData.EARLY_ROOM_LARGE, 100),
						entry(3, 15, GolemSpawnData.EARLY_ROOM_RIDER, 100),
						entry(3, 15, GolemSpawnData.EARLY_ROOM_RANGED, 100),
						entry(5, 15, GolemSpawnData.EARLY_ROOM_MIXED, 100),
						entry(2, 14, GolemSpawnData.STONE_ROOM_BASIC, 100),
						entry(3, 14, GolemSpawnData.STONE_ROOM_LARGE, 100),
						entry(3, 14, GolemSpawnData.STONE_ROOM_RIDER, 100),
						entry(3, 14, GolemSpawnData.STONE_ROOM_RANGED, 100),
						entry(5, 14, GolemSpawnData.STONE_ROOM_MIXED, 100)
				)
				.room("room/end").variant("", 100).end()
				.room("room/corner").variant("", 100).end()
				.room("room/cross").variant("", 100).end()
				.room("room/straight").variant("", 100).end()
				.room("room/t_way").variant("", 100).end()
				.endSpawn()
				.room("stairs").variant("", 100).end()
				.room("cross_stairs").variant("", 100).end()
				.room("boss").variant("", 100).end()
				.spawn("quad", 0, 1,
						entry(4, 15, GolemSpawnData.EARLY_QUAD, 100),
						entry(4, 14, GolemSpawnData.STONE_QUAD, 100)
				).room("quad").variant("", 100).end().endSpawn()
				.root("test")
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("mineshaft"), new TemplateConfig()
				.start("mineshaft")
				.room("path/corner").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 73).variant("_waypoint", 27).end()
				.room("path/cross").variant("", 50).variant("_waypoint", 50).end()
				.spawn("room", 1, 0,
						entry(2, 0, GolemSpawnData.MINESHAFT_ROOM_BASIC, 100),
						entry(3, 0, GolemSpawnData.MINESHAFT_ROOM_LARGE, 100),
						entry(3, 0, GolemSpawnData.MINESHAFT_ROOM_RIDER, 100),
						entry(3, 0, GolemSpawnData.MINESHAFT_ROOM_RANGED, 100),
						entry(5, 0, GolemSpawnData.MINESHAFT_ROOM_MIXED, 100)
				)
				.room("room/end").variant("", 100).end()
				.room("room/corner").variant("", 100).end()
				.room("room/cross").variant("", 100).end()
				.room("room/straight").variant("", 100).end()
				.room("room/t_way").variant("", 100).end()
				.endSpawn()
				.room("stairs").variant("", 100).end()
				.room("cross_stairs").variant("", 100).end()
				.room("boss").variant("", 100, GolemSpawnData.MINESHAFT_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.MINESHAFT_QUAD).end()
				.root("test")
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("copper"), new TemplateConfig()
				.start("copper")
				.room("path/corner").variant("", 100).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 76).variant("_waypoint", 24).end()
				.room("path/cross").variant("", 50).variant("_waypoint", 50).end()
				.spawn("room", 1, 0,
						entry(2, 0, GolemSpawnData.COPPER_ROOM_BASIC, 100),
						entry(3, 0, GolemSpawnData.COPPER_ROOM_LARGE, 100),
						entry(3, 0, GolemSpawnData.COPPER_ROOM_RIDER, 100),
						entry(3, 0, GolemSpawnData.COPPER_ROOM_RANGED, 100),
						entry(5, 0, GolemSpawnData.COPPER_ROOM_MIXED, 100)
				)
				.room("room/end").variant("", 100).end()
				.room("room/corner").variant("", 100).end()
				.room("room/cross").variant("", 100).end()
				.room("room/straight").variant("", 100).end()
				.room("room/t_way").variant("", 100).end()
				.endSpawn()
				.room("stairs").variant("", 100).end()
				.room("cross_stairs").variant("", 100).end()
				.room("boss").variant("", 100, GolemSpawnData.COPPER_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.COPPER_QUAD).end()
				.root("test")
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("deepslate"), new TemplateConfig()
				.start("deepslate")
				.room("path/corner").variant("", 90).variant("_attic", 10).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 79).variant("_waypoint", 21).end()
				.room("path/cross").variant("", 50).variant("_waypoint", 50).end()
				.spawn("room", 1, 0,
						entry(2, 0, GolemSpawnData.DEEPSLATE_ROOM_BASIC, 100),
						entry(3, 0, GolemSpawnData.DEEPSLATE_ROOM_LARGE, 100),
						entry(3, 0, GolemSpawnData.DEEPSLATE_ROOM_RIDER, 100),
						entry(3, 0, GolemSpawnData.DEEPSLATE_ROOM_RANGED, 100),
						entry(5, 0, GolemSpawnData.DEEPSLATE_ROOM_MIXED, 100)
				)
				.room("room/end").variant("", 100).end()
				.room("room/corner").variant("", 100).end()
				.room("room/cross").variant("", 100).end()
				.room("room/straight").variant("", 100).end()
				.room("room/t_way").variant("", 100).end()
				.endSpawn()
				.room("stairs").variant("", 100).end()
				.room("cross_stairs").variant("", 100).end()
				.room("boss").variant("", 100, GolemSpawnData.DEEPSLATE_BOSS).end()
				.room("quad").variant("", 100, GolemSpawnData.DEEPSLATE_QUAD).end()
				.root("test")
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

		map.add(DungeonInfinity.TEMPLATES, DungeonInfinity.loc("sculk"), new TemplateConfig()
				.start("sculk")
				.room("path/corner").variant("", 90).variant("_attic", 10).end()
				.room("path/straight").variant("", 100).end()
				.room("path/t_way").variant("", 82).variant("_waypoint", 18).end()
				.room("path/cross").variant("", 50).variant("_waypoint", 50).end()
				.spawn("room", 1, 1,
						entry(2, 1, GolemSpawnData.SCULK_ROOM_BASIC, 100),
						entry(3, 1, GolemSpawnData.SCULK_ROOM_LARGE, 100),
						entry(3, 1, GolemSpawnData.SCULK_ROOM_RIDER, 100),
						entry(3, 1, GolemSpawnData.SCULK_ROOM_RANGED, 100),
						entry(5, 1, GolemSpawnData.SCULK_ROOM_MIXED, 100),
						entry(2, 0, GolemSpawnData.DEEPEST_ROOM_BASIC, 100),
						entry(3, 0, GolemSpawnData.DEEPEST_ROOM_LARGE, 100),
						entry(3, 0, GolemSpawnData.DEEPEST_ROOM_RIDER, 100),
						entry(3, 0, GolemSpawnData.DEEPEST_ROOM_RANGED, 100),
						entry(5, 0, GolemSpawnData.DEEPEST_ROOM_MIXED, 100)
				)
				.room("room/end").variant("", 100).end()
				.room("room/corner").variant("", 100).end()
				.room("room/cross").variant("", 100).end()
				.room("room/straight").variant("", 100).end()
				.room("room/t_way").variant("", 100).end()
				.endSpawn()
				.room("stairs").variant("", 100).end()
				.room("cross_stairs").variant("", 100).end()
				.room("boss").variant("", 100, GolemSpawnData.SCULK_BOSS).end()
				.spawn("quad", 0, 1,
						entry(4, 1, GolemSpawnData.SCULK_QUAD, 100),
						entry(4, 0, GolemSpawnData.DEEPEST_QUAD, 100)
				).room("quad").variant("", 100).end().endSpawn()
				.root("test")
				.room("path/end").variants("workshop", "warehouse", "shop").end()
				.end());

	}

	// shops
	public void genShop(ConfigDataProvider.Collector map) {

		ItemStackTemplate heal = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.HEALING))
				.build());

		ItemStackTemplate healsp = new ItemStackTemplate(Items.SPLASH_POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.HEALING))
				.build());

		ItemStackTemplate heal2 = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_HEALING))
				.build());

		ItemStackTemplate heal2sp = new ItemStackTemplate(Items.SPLASH_POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_HEALING))
				.build());

		ItemStackTemplate regen = new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.REGENERATION))
				.build());

		ItemStackTemplate regensp = new ItemStackTemplate(Items.SPLASH_POTION, DataComponentPatch.builder()
				.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.REGENERATION))
				.build());

		var gispear = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.SPEAR.ordinal()][VanillaGolemWeaponMaterial.IRON.ordinal()].get();
		var giaxe = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.AXE.ordinal()][VanillaGolemWeaponMaterial.IRON.ordinal()].get();
		var gisword = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.SWORD.ordinal()][VanillaGolemWeaponMaterial.IRON.ordinal()].get();
		var gdspear = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.SPEAR.ordinal()][VanillaGolemWeaponMaterial.DIAMOND.ordinal()].get();
		var gdaxe = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.AXE.ordinal()][VanillaGolemWeaponMaterial.DIAMOND.ordinal()].get();
		var gdsword = GolemItems.METALGOLEM_WEAPON[GolemWeaponType.SWORD.ordinal()][VanillaGolemWeaponMaterial.DIAMOND.ordinal()].get();

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("icon"), new ShopConfig().setIcon(Map.of(
				"groceries", Items.BREAD,
				"recycler", Items.COPPER_INGOT,
				"blacksmith", Items.SHIELD
		)));

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("stone"), new ShopConfig()
				.start("stone")
				.addPool("food")
				.add(1, Items.BREAD, 16, 200, 64)
				.add(1, Items.BAKED_POTATO, 12, 100, 64)
				.add(1, Items.COOKED_BEEF, 3, 50, 16)
				.add(3, Items.GOLDEN_CARROT, 1, 50, 8)
				.end()
				.addPool("consumable")
				.add(1, Items.GLOWSTONE_DUST, 4, 200, 32)
				.add(1, Items.ARROW, 16, 400, 64)
				.add(4, DIItems.KEY_OF_TOMB.get(), 1, 200, 4)
				.add(2, heal, 100, 3)
				.add(3, healsp, 100, 3)
				.end()
				.addPool("ingredient")
				.add(1, Items.OAK_LOG, 16, 200, 64)
				.add(1, GolemItems.GOLEM_TEMPLATE.get(), 1, 200, 64)
				.add(4, GolemItems.EMPTY_UPGRADE.get(), 1, 100, 64)
				.add(1, Items.REDSTONE, 4, 100, 8)
				.add(8, Items.SHULKER_SHELL, 1, 100, 2)
				.end()
				.addPool("material")
				.add(1, Items.COPPER_INGOT, 4, 100, 32)
				.add(1, Items.IRON_INGOT, 1, 100, 16)
				.add(4, Items.GOLD_INGOT, 1, 50, 8)
				.add(8, Items.DIAMOND, 1, 50, 8)
				.end()
				.addPool("recycle")
				.buy(GolemItems.GOLEM_TEMPLATE.get(), 4, 2, 100, 32)
				.buy(Items.COPPER_INGOT, 16, 2, 100, 32)
				.buy(Items.IRON_INGOT, 16, 8, 100, 16)
				.buy(Items.GOLD_INGOT, 4, 8, 50, 16)
				.end()
				.addPool("recycle_upgrade")
				.buy(GolemItems.QUARTZ.get(), 1, 2, 100, 32)
				.buy(GolemItems.SPEED.get(), 1, 2, 100, 32)
				.buy(GolemItems.GOLD.get(), 1, 8, 100, 32)
				.buy(GolemItems.DIAMOND.get(), 1, 8, 100, 32)
				.end()
				.addPool("equipments")
				.add(2, Items.BOW, 1, 200, 4)
				.add(2, Items.SHIELD, 1, 200, 4)
				.add(1, Items.STONE_SWORD, 1, 200, 4)
				.add(1, Items.STONE_AXE, 1, 200, 4)
				.end()
				.addPool("golem_equipments")
				.add(10, GolemItems.IRON_BOW.asItem(), 1, 100, 1)
				.add(3, gispear, 1, 50, 1)
				.add(4, giaxe, 1, 50, 1)
				.add(5, gisword, 1, 50, 1)
				.add(24, GolemItems.GOLEMGUARD_HELMET.asItem(), 1, 100, 1)
				.add(32, GolemItems.GOLEMGUARD_CHESTPLATE.asItem(), 1, 100, 1)
				.add(16, GolemItems.GOLEMGUARD_SHINGUARD.asItem(), 1, 100, 1)
				.end()
				.shop("groceries", "food", 2, "consumable", 3, "ingredient", 3)
				.shop("recycler", "recycle", 4, "recycle_upgrade", 4)
				.shop("blacksmith", "material", 3, "equipments", 3, "golem_equipments", 2)
				.end()
		);

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("mineshaft"), new ShopConfig()
				.start("mineshaft")
				.addPool("food")
				.add(1, Items.BREAD, 16, 100, 64)
				.add(1, Items.BAKED_POTATO, 12, 100, 64)
				.add(1, Items.COOKED_BEEF, 3, 100, 32)
				.add(3, Items.GOLDEN_CARROT, 1, 50, 16)
				.add(24, Items.GOLDEN_APPLE, 1, 50, 1)
				.end()
				.addPool("consumable")
				.add(1, Items.GLOWSTONE_DUST, 4, 200, 32)
				.add(1, Items.EXPERIENCE_BOTTLE, 4, 400, 64)
				.add(1, Items.ARROW, 16, 200, 64)
				.add(4, DIItems.KEY_OF_TOMB.get(), 1, 200, 6)
				.add(4, heal2, 100, 3)
				.add(6, heal2sp, 100, 3)
				.end()
				.addPool("ingredient")
				.add(1, Items.OAK_LOG, 16, 100, 64)
				.add(1, GolemItems.GOLEM_TEMPLATE.get(), 1, 100, 64)
				.add(4, GolemItems.EMPTY_UPGRADE.get(), 1, 100, 64)
				.add(1, Items.REDSTONE, 4, 100, 16)
				.add(1, Items.LAPIS_LAZULI, 4, 100, 32)
				.add(8, Items.SHULKER_SHELL, 1, 100, 4)
				.end()
				.addPool("material")
				.add(1, Items.COPPER_INGOT, 4, 100, 32)
				.add(1, Items.IRON_INGOT, 1, 100, 32)
				.add(4, Items.GOLD_INGOT, 1, 100, 8)
				.add(8, Items.DIAMOND, 1, 500, 16)
				.end()
				.addPool("recycle")
				.buy(GolemItems.GOLEM_TEMPLATE.get(), 4, 2, 100, 32)
				.buy(Items.COPPER_INGOT, 16, 2, 100, 32)
				.buy(Items.IRON_INGOT, 16, 8, 100, 16)
				.buy(Items.GOLD_INGOT, 4, 8, 100, 16)
				.end()
				.addPool("recycle_upgrade")
				.buy(GolemItems.QUARTZ.get(), 1, 2, 50, 32)
				.buy(GolemItems.SPEED.get(), 1, 2, 50, 32)
				.buy(GolemItems.GOLD.get(), 1, 8, 50, 32)
				.buy(GolemItems.WEAK.get(), 1, 2, 50, 32)
				.buy(GolemItems.SLOW.get(), 1, 2, 50, 32)
				.buy(GolemItems.WITHER.get(), 1, 2, 50, 32)
				.buy(GolemItems.DIAMOND.get(), 1, 8, 100, 32)
				.buy(GolemItems.MOUNT_UPGRADE.get(), 1, 8, 100, 32)
				.buy(GolemItems.SIZE_UPGRADE.get(), 1, 8, 100, 32)
				.buy(GolemItems.PICKUP.get(), 1, 8, 100, 32)
				.end()
				.addPool("equipments")
				.add(2, Items.BOW, 1, 200, 8)
				.add(2, Items.SHIELD, 1, 200, 8)
				.add(1, Items.STONE_SWORD, 1, 200, 4)
				.add(1, Items.STONE_AXE, 1, 200, 4)
				.add(12, Items.DIAMOND_SWORD, 1, 100, 4)
				.add(16, Items.TRIDENT, 1, 100, 1)
				.add(8, ench(Enchantments.PROTECTION, 1), 100, 4)
				.add(16, ench(Enchantments.INFINITY, 1), 100, 1)
				.end()
				.addPool("golem_equipments")
				.add(10, GolemItems.IRON_BOW.asItem(), 1, 100, 4)
				.add(3, gispear, 1, 50, 4)
				.add(4, giaxe, 1, 50, 4)
				.add(5, gisword, 1, 50, 4)
				.add(24, GolemItems.GOLEMGUARD_HELMET.asItem(), 1, 100, 4)
				.add(32, GolemItems.GOLEMGUARD_CHESTPLATE.asItem(), 1, 100, 4)
				.add(16, GolemItems.GOLEMGUARD_SHINGUARD.asItem(), 1, 100, 4)
				.end()
				.shop("groceries", "food", 2, "consumable", 3, "ingredient", 3)
				.shop("recycler", "recycle", 4, "recycle_upgrade", 5)
				.shop("blacksmith", "material", 3, "equipments", 3, "golem_equipments", 3)
				.end()
		);

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("copper"), new ShopConfig()
				.start("copper")
				.addPool("exchange")
				.add(8, Items.DIAMOND, 1, 100, 64)
				.end()
				.addPool("food")
				.add(1, Items.COOKED_BEEF, 3, 100, 64)
				.add(3, Items.GOLDEN_CARROT, 1, 50, 32)
				.setCurrency(Items.DIAMOND)
				.add(3, Items.GOLDEN_APPLE, 1, 50, 4)
				.end()
				.addPool("consumable")
				.add(1, Items.GLOWSTONE_DUST, 4, 200, 32)
				.add(1, Items.ARROW, 16, 200, 64)
				.add(1, Items.EXPERIENCE_BOTTLE, 4, 200, 64)
				.add(4, DIItems.KEY_OF_TOMB.get(), 1, 200, 8)
				.add(4, heal2, 50, 3)
				.add(6, heal2sp, 50, 3)
				.add(6, regen, 50, 3)
				.add(8, regensp, 50, 3)
				.end()
				.addPool("ingredient")
				.add(1, Items.OAK_LOG, 16, 50, 64)
				.add(1, GolemItems.GOLEM_TEMPLATE.get(), 1, 50, 64)
				.add(4, GolemItems.EMPTY_UPGRADE.get(), 1, 100, 64)
				.add(1, Items.REDSTONE, 4, 100, 32)
				.add(1, Items.LAPIS_LAZULI, 4, 100, 32)
				.add(8, Items.SHULKER_SHELL, 1, 100, 4)
				.end()
				.addPool("material")
				.add(1, Items.COPPER_INGOT, 4, 100, 32)
				.add(1, Items.IRON_INGOT, 1, 100, 16)
				.add(4, Items.GOLD_INGOT, 1, 100, 16)
				.end()
				.addPool("recycle")
				.buy(GolemItems.GOLEM_TEMPLATE.get(), 4, 2, 100, 32)
				.buy(Items.COPPER_INGOT, 16, 2, 100, 32)
				.buy(Items.IRON_INGOT, 16, 8, 100, 16)
				.buy(Items.GOLD_INGOT, 4, 8, 100, 16)
				.buy(Items.DIAMOND, 1, 6, 100, 16)
				.end()
				.addPool("recycle_upgrade")
				.buy(GolemItems.GOLD.get(), 1, 8, 50, 32)
				.buy(GolemItems.WEAK.get(), 1, 2, 50, 32)
				.buy(GolemItems.SLOW.get(), 1, 2, 50, 32)
				.buy(GolemItems.WITHER.get(), 1, 2, 50, 32)
				.buy(GolemItems.DIAMOND.get(), 1, 8, 50, 32)
				.buy(GolemItems.MOUNT_UPGRADE.get(), 1, 8, 50, 32)
				.buy(GolemItems.SIZE_UPGRADE.get(), 1, 8, 50, 32)
				.buy(GolemItems.PICKUP.get(), 1, 8, 50, 32)
				.buy(GolemItems.CAULDRON.get(), 1, 8, 100, 32)
				.setCurrency(Items.DIAMOND)
				.buy(GolemItems.RECYCLE.get(), 1, 2, 100, 32)
				.buy(GDModifiers.ITEM_REFORGE.get(), 1, 2, 100, 32)
				.end()
				.addPool("equipments")
				.add(2, Items.BOW, 1, 200, 4)
				.add(2, Items.SHIELD, 1, 200, 4)
				.add(1, Items.STONE_AXE, 1, 200, 4)
				.add(12, Items.DIAMOND_SWORD, 1, 100, 4)
				.add(16, Items.TRIDENT, 1, 100, 2)
				.add(16, ench(Enchantments.PROTECTION, 2), 100, 4)
				.add(16, ench(Enchantments.INFINITY, 1), 100, 1)
				.end()
				.addPool("golem_equipments")
				.add(10, GolemItems.IRON_BOW.asItem(), 1, 50, 4)
				.add(3, gispear, 1, 50, 4)
				.add(4, giaxe, 1, 50, 4)
				.add(5, gisword, 1, 50, 4)
				.add(24, GolemItems.GOLEMGUARD_HELMET.asItem(), 1, 50, 4)
				.add(32, GolemItems.GOLEMGUARD_CHESTPLATE.asItem(), 1, 50, 4)
				.add(16, GolemItems.GOLEMGUARD_SHINGUARD.asItem(), 1, 50, 4)
				.add(20, gdspear, 1, 50, 1)
				.add(24, gdaxe, 1, 50, 1)
				.add(26, gdsword, 1, 50, 1)
				.setCurrency(Items.DIAMOND)
				.add(12, GolemItems.WINDSPIRIT_HELMET.asItem(), 1, 100, 1)
				.add(16, GolemItems.WINDSPIRIT_CHESTPLATE.asItem(), 1, 100, 1)
				.add(10, GolemItems.WINDSPIRIT_SHINGUARD.asItem(), 1, 100, 1)
				.end()
				.shop("groceries", "food", 2, "consumable", 4, "ingredient", 3)
				.shop("recycler", "recycle", 4, "recycle_upgrade", 6)
				.shop("blacksmith", "exchange", 1, "material", 3, "equipments", 3, "golem_equipments", 3)
				.end()
		);

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("deepslate"), new ShopConfig()
				.start("deepslate")
				.addPool("exchange")
				.add(8, Items.DIAMOND, 1, 100, 64)
				.end()
				.addPool("food")
				.add(1, Items.COOKED_BEEF, 3, 100, 64)
				.add(3, Items.GOLDEN_CARROT, 1, 50, 64)
				.setCurrency(Items.DIAMOND)
				.add(3, Items.GOLDEN_APPLE, 1, 50, 16)
				.add(12, Items.ENCHANTED_GOLDEN_APPLE, 1, 20, 1)
				.end()
				.addPool("consumable")
				.add(1, Items.GLOWSTONE_DUST, 4, 200, 32)
				.add(16, Items.ARROW, 64, 200, 64)
				.add(1, Items.EXPERIENCE_BOTTLE, 4, 200, 64)
				.add(4, DIItems.KEY_OF_TOMB.get(), 1, 200, 12)
				.add(4, heal2, 50, 3)
				.add(6, heal2sp, 50, 3)
				.add(6, regen, 50, 3)
				.add(8, regensp, 50, 3)
				.setCurrency(Items.DIAMOND)
				.add(8, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 100, 4)
				.end()
				.addPool("ingredient")
				.add(1, Items.OAK_LOG, 16, 50, 64)
				.add(1, GolemItems.GOLEM_TEMPLATE.get(), 1, 50, 64)
				.add(4, GolemItems.EMPTY_UPGRADE.get(), 1, 50, 64)
				.add(1, Items.REDSTONE, 4, 50, 32)
				.add(1, Items.LAPIS_LAZULI, 4, 100, 32)
				.add(8, Items.SHULKER_SHELL, 1, 100, 8)
				.add(8, Items.NETHERRACK, 1, 100, 4)
				.end()
				.addPool("material")
				.add(1, Items.COPPER_INGOT, 4, 100, 64)
				.add(1, Items.IRON_INGOT, 1, 100, 32)
				.add(4, Items.GOLD_INGOT, 1, 100, 16)
				.add(8, Items.NETHERITE_SCRAP, 1, 100, 4)
				.end()
				.addPool("recycle")
				.buy(GolemItems.GOLEM_TEMPLATE.get(), 4, 2, 100, 32)
				.buy(Items.COPPER_INGOT, 16, 2, 100, 32)
				.buy(Items.IRON_INGOT, 16, 8, 100, 16)
				.buy(Items.GOLD_INGOT, 4, 8, 100, 16)
				.buy(Items.DIAMOND, 1, 6, 100, 16)
				.setCurrency(Items.DIAMOND)
				.buy(GolemItems.FLAME_THROWER.get(), 1, 1, 100, 8)
				.end()
				.addPool("recycle_upgrade")
				.buy(GolemItems.DIAMOND.get(), 1, 8, 50, 32)
				.buy(GolemItems.PICKUP.get(), 1, 8, 50, 32)
				.buy(GolemItems.CAULDRON.get(), 1, 8, 100, 32)
				.setCurrency(Items.DIAMOND)
				.buy(GolemItems.NETHERITE.get(), 1, 2, 100, 32)
				.buy(GolemItems.ENCHANTED_GOLD.get(), 1, 2, 100, 32)
				.buy(GolemItems.RECYCLE.get(), 1, 2, 100, 32)
				.buy(GDModifiers.ITEM_REFORGE.get(), 1, 2, 100, 32)
				.buy(GDModifiers.ITEM_RESISTANCE.get(), 1, 2, 100, 32)
				.end()
				.addPool("equipments")
				.add(2, Items.BOW, 1, 200, 4)
				.add(2, Items.SHIELD, 1, 200, 4)
				.add(12, Items.DIAMOND_SWORD, 1, 200, 4)
				.add(16, Items.TRIDENT, 1, 200, 2)
				.add(16, ench(Enchantments.INFINITY, 1), 100, 1)
				.setCurrency(Items.DIAMOND)
				.add(3, ench(Enchantments.MENDING, 1), 100, 4)
				.add(4, ench(Enchantments.PROTECTION, 3), 100, 4)
				.end()
				.addPool("golem_equipments")
				.add(20, gdspear, 1, 50, 4)
				.add(24, gdaxe, 1, 50, 4)
				.add(26, gdsword, 1, 50, 4)
				.setCurrency(Items.DIAMOND)
				.add(32, GolemItems.BEACON_BOOTS.asItem(), 1, 100, 1)
				.add(6, GolemItems.NETHERITE_BOW.asItem(), 1, 100, 1)
				.add(12, GolemItems.WINDSPIRIT_HELMET.asItem(), 1, 100, 4)
				.add(16, GolemItems.WINDSPIRIT_CHESTPLATE.asItem(), 1, 100, 4)
				.add(10, GolemItems.WINDSPIRIT_SHINGUARD.asItem(), 1, 100, 4)
				.end()
				.shop("groceries", "food", 2, "consumable", 4, "ingredient", 3)
				.shop("recycler", "recycle", 5, "recycle_upgrade", 6)
				.shop("blacksmith", "exchange", 1, "material", 4, "equipments", 3, "golem_equipments", 3)
				.end()
		);

		map.add(DungeonInfinity.SHOPS, DungeonInfinity.loc("sculk"), new ShopConfig()
				.start("sculk")
				.addPool("exchange")
				.add(8, Items.DIAMOND, 1, 100, 64)
				.end()
				.addPool("food")
				.add(3, Items.GOLDEN_CARROT, 1, 50, 64)
				.setCurrency(Items.DIAMOND)
				.add(3, Items.GOLDEN_APPLE, 1, 50, 16)
				.add(12, Items.ENCHANTED_GOLDEN_APPLE, 1, 20, 4)
				.end()
				.addPool("consumable")
				.add(1, Items.GLOWSTONE_DUST, 4, 200, 32)
				.add(16, Items.ARROW, 64, 200, 64)
				.add(1, Items.EXPERIENCE_BOTTLE, 4, 200, 64)
				.add(4, DIItems.KEY_OF_TOMB.get(), 1, 200, 16)
				.add(4, heal2, 50, 3)
				.add(6, heal2sp, 50, 3)
				.add(6, regen, 50, 3)
				.add(8, regensp, 50, 3)
				.setCurrency(Items.DIAMOND)
				.add(8, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 100, 4)
				.end()
				.addPool("ingredient")
				.add(1, Items.OAK_LOG, 16, 25, 64)
				.add(1, GolemItems.GOLEM_TEMPLATE.get(), 1, 50, 64)
				.add(4, GolemItems.EMPTY_UPGRADE.get(), 1, 25, 64)
				.add(1, Items.LAPIS_LAZULI, 4, 100, 32)
				.add(8, Items.SHULKER_SHELL, 1, 100, 8)
				.add(8, Items.NETHERRACK, 1, 100, 16)
				.end()
				.addPool("material")
				.add(1, Items.COPPER_INGOT, 4, 100, 64)
				.add(1, Items.IRON_INGOT, 1, 100, 64)
				.add(4, Items.GOLD_INGOT, 1, 100, 32)
				.add(8, Items.NETHERITE_SCRAP, 1, 100, 16)
				.end()
				.addPool("recycle")
				.buy(GolemItems.GOLEM_TEMPLATE.get(), 4, 2, 100, 32)
				.buy(Items.COPPER_INGOT, 16, 2, 100, 32)
				.buy(Items.IRON_INGOT, 16, 8, 100, 16)
				.buy(Items.GOLD_INGOT, 4, 8, 100, 16)
				.buy(Items.DIAMOND, 1, 6, 100, 16)
				.setCurrency(Items.DIAMOND)
				.buy(GolemItems.FLAME_THROWER.get(), 1, 1, 100, 8)
				.buy(GolemItems.BEACON_CANNON.get(), 1, 4, 100, 8)
				.end()
				.addPool("recycle_upgrade")
				.buy(GolemItems.CAULDRON.get(), 1, 8, 100, 32)
				.setCurrency(Items.DIAMOND)
				.buy(GolemItems.NETHERITE.get(), 1, 2, 100, 32)
				.buy(GolemItems.TALENTED.get(), 1, 2, 100, 32)
				.buy(GolemItems.ENCHANTED_GOLD.get(), 1, 2, 100, 32)
				.buy(GDModifiers.ITEM_REFORGE.get(), 1, 2, 100, 32)
				.buy(GDModifiers.ITEM_RESISTANCE.get(), 1, 2, 100, 32)
				.end()
				.addPool("equipments")
				.add(2, Items.BOW, 1, 200, 4)
				.add(2, Items.SHIELD, 1, 200, 4)
				.add(12, Items.DIAMOND_SWORD, 1, 200, 4)
				.add(16, Items.TRIDENT, 1, 200, 2)
				.add(16, ench(Enchantments.INFINITY, 1), 100, 1)
				.setCurrency(Items.DIAMOND)
				.add(3, ench(Enchantments.MENDING, 1), 100, 4)
				.add(8, ench(Enchantments.PROTECTION, 4), 100, 4)
				.end()
				.addPool("golem_equipments")
				.setCurrency(Items.DIAMOND)
				.add(6, GolemItems.NETHERITE_BOW.asItem(), 1, 50, 4)
				.add(32, GolemItems.BEACON_BOOTS.asItem(), 1, 100, 1)
				.add(64, GolemItems.SONIC_CANNON.asItem(), 1, 100, 1)
				.add(12, GolemItems.WINDSPIRIT_HELMET.asItem(), 1, 50, 4)
				.add(16, GolemItems.WINDSPIRIT_CHESTPLATE.asItem(), 1, 50, 4)
				.add(10, GolemItems.WINDSPIRIT_SHINGUARD.asItem(), 1, 50, 4)
				.end()
				.shop("groceries", "food", 2, "consumable", 4, "ingredient", 4)
				.shop("recycler", "recycle", 6, "recycle_upgrade", 6)
				.shop("blacksmith", "exchange", 1, "material", 4, "equipments", 3, "golem_equipments", 3)
				.end()
		);
	}

}
