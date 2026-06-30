
package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.golemdungeons.content.config.EquipmentConfig;
import dev.xkmc.golemdungeons.content.config.SpawnConfig;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.content.faction.DungeonFactionRegistry;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.loot.GDLootGen;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
import dev.xkmc.golemdungeons.init.reg.GDItems;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class FactoryGolemSpawn extends AbstractGolemSpawn {

	public static final Identifier ITEM_LARGE_ARMOR = GolemDungeons.loc("factory_large_armor");
	public static final Identifier ITEM_LARGE_ARMOR_BETTER = GolemDungeons.loc("factory_large_armor_better");
	public static final Identifier ITEM_LARGE_WEAPON = GolemDungeons.loc("factory_large_weapon");
	public static final Identifier ITEM_LARGE_WEAPON_BETTER = GolemDungeons.loc("factory_large_weapon_better");
	public static final Identifier ITEM_HUMANOID_ARMOR_MISC = GolemDungeons.loc("factory_humanoid_armor_misc");
	public static final Identifier ITEM_HUMANOID_ARMOR_CHAIN = GolemDungeons.loc("factory_humanoid_armor_chain");
	public static final Identifier ITEM_HUMANOID_ARMOR_GOLD = GolemDungeons.loc("factory_humanoid_armor_gold");
	public static final Identifier ITEM_HUMANOID_MELEE = GolemDungeons.loc("factory_humanoid_weapon_melee");
	public static final Identifier ITEM_HUMANOID_BOW = GolemDungeons.loc("factory_humanoid_weapon_bow");
	public static final Identifier ITEM_HUMANOID_TIPPED = GolemDungeons.loc("factory_humanoid_weapon_tipped_arrow");
	public static final Identifier ITEM_HUMANOID_ROCKET = GolemDungeons.loc("factory_humanoid_weapon_rocket_crossbow");
	public static final Identifier ITEM_HORSE_ARMOR = GolemDungeons.loc("factory_horse_armor");

	public static final Identifier FACTORY_ALL = GolemDungeons.loc("factory_remnant");

	public static final Identifier LARGE_1 = GolemDungeons.loc("factory_large_1");
	public static final Identifier LARGE_2 = GolemDungeons.loc("factory_large_2");
	public static final Identifier LARGE_3 = GolemDungeons.loc("factory_large_3");

	public static final Identifier HUMANOID_BASIC = GolemDungeons.loc("factory_humanoid_basic");
	public static final Identifier HUMANOID_MELEE = GolemDungeons.loc("factory_humanoid_melee");
	public static final Identifier HUMANOID_RANGED = GolemDungeons.loc("factory_humanoid_ranged");
	public static final Identifier HUMANOID_TIPPED = GolemDungeons.loc("factory_humanoid_tipped");
	public static final Identifier HUMANOID_ROCKET = GolemDungeons.loc("factory_humanoid_rocket");

	public static void add(ConfigDataProvider.Collector map) {

		// metal golem equipments
		{
			map.add(GolemDungeons.ITEMS, ITEM_LARGE_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 100)
					.add(EquipmentSlot.HEAD, 20, GolemItems.GOLEMGUARD_HELMET, 20)
					.add(EquipmentSlot.CHEST, 100)
					.add(EquipmentSlot.CHEST, 20, GolemItems.GOLEMGUARD_CHESTPLATE, 20)
					.add(EquipmentSlot.LEGS, 100)
					.add(EquipmentSlot.LEGS, 20, GolemItems.GOLEMGUARD_SHINGUARD, 20)
			);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_WEAPON, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.AXE), 20)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SWORD), 20)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SPEAR), 20)
			);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_ARMOR_BETTER, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 50)
					.add(EquipmentSlot.HEAD, 100, GolemItems.GOLEMGUARD_HELMET, 20)
					.add(EquipmentSlot.CHEST, 50)
					.add(EquipmentSlot.CHEST, 100, GolemItems.GOLEMGUARD_CHESTPLATE, 20)
					.add(EquipmentSlot.LEGS, 50)
					.add(EquipmentSlot.LEGS, 100, GolemItems.GOLEMGUARD_SHINGUARD, 20)
			);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_WEAPON_BETTER, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.AXE), 20)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SWORD), 20)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SPEAR), 20)
					.add(EquipmentSlot.MAINHAND, 100, GDItems.ANCIENT_FORGE.get(), 20, 2)
			);
		}

		// humanoid armors
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_MISC, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 100)
					.add(EquipmentSlot.CHEST, 100)
					.add(EquipmentSlot.LEGS, 100)
					.add(EquipmentSlot.FEET, 100)
					.add(EquipmentSlot.HEAD, 20, Items.DIAMOND_HELMET, 10)
					.add(EquipmentSlot.CHEST, 20, Items.DIAMOND_CHESTPLATE, 10)
					.add(EquipmentSlot.LEGS, 20, Items.DIAMOND_LEGGINGS, 10)
					.add(EquipmentSlot.FEET, 20, Items.DIAMOND_BOOTS, 10)
					.add(EquipmentSlot.HEAD, 50, Items.IRON_HELMET, 15)
					.add(EquipmentSlot.CHEST, 50, Items.IRON_CHESTPLATE, 15)
					.add(EquipmentSlot.LEGS, 50, Items.IRON_LEGGINGS, 15)
					.add(EquipmentSlot.FEET, 50, Items.IRON_BOOTS, 15)
					.add(EquipmentSlot.HEAD, 50, Items.CHAINMAIL_HELMET, 20)
					.add(EquipmentSlot.CHEST, 50, Items.CHAINMAIL_CHESTPLATE, 20)
					.add(EquipmentSlot.LEGS, 50, Items.CHAINMAIL_LEGGINGS, 20)
					.add(EquipmentSlot.FEET, 50, Items.CHAINMAIL_BOOTS, 20)
					.add(EquipmentSlot.HEAD, 50, Items.GOLDEN_HELMET, 30)
					.add(EquipmentSlot.CHEST, 50, Items.GOLDEN_CHESTPLATE, 30)
					.add(EquipmentSlot.LEGS, 50, Items.GOLDEN_LEGGINGS, 30)
					.add(EquipmentSlot.FEET, 50, Items.GOLDEN_BOOTS, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_CHAIN, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 50, Items.CHAINMAIL_HELMET, 20)
					.add(EquipmentSlot.CHEST, 50, Items.CHAINMAIL_CHESTPLATE, 20)
					.add(EquipmentSlot.LEGS, 50, Items.CHAINMAIL_LEGGINGS, 20)
					.add(EquipmentSlot.FEET, 50, Items.CHAINMAIL_BOOTS, 20)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_GOLD, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 50, Items.GOLDEN_HELMET, 30)
					.add(EquipmentSlot.CHEST, 50, Items.GOLDEN_CHESTPLATE, 30)
					.add(EquipmentSlot.LEGS, 50, Items.GOLDEN_LEGGINGS, 30)
					.add(EquipmentSlot.FEET, 50, Items.GOLDEN_BOOTS, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HORSE_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.CHEST, 100)
					.add(EquipmentSlot.CHEST, 30, Items.LEATHER_HORSE_ARMOR)
					.add(EquipmentSlot.CHEST, 40, Items.IRON_HORSE_ARMOR)
					.add(EquipmentSlot.CHEST, 30, Items.GOLDEN_HORSE_ARMOR)
					.add(EquipmentSlot.CHEST, 20, Items.DIAMOND_HORSE_ARMOR)
			);

		}

		// humanoid weapons
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 200, Items.STONE_AXE, 10)
					.add(EquipmentSlot.MAINHAND, 200, Items.STONE_SWORD, 20)
					.add(EquipmentSlot.MAINHAND, 100, Items.STONE_PICKAXE, 10)
					.add(EquipmentSlot.MAINHAND, 100, Items.IRON_AXE, 10)
					.add(EquipmentSlot.MAINHAND, 100, Items.IRON_SWORD, 20)
					.add(EquipmentSlot.MAINHAND, 50, Items.IRON_PICKAXE, 10)
					.add(EquipmentSlot.MAINHAND, 150, Items.TRIDENT, 10)
					.add(EquipmentSlot.OFFHAND, 100, Items.SHIELD, 20)
					.add(EquipmentSlot.OFFHAND, 100)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_BOW, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.ARROW)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_TIPPED, new EquipmentConfig()
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.POISON))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.SLOWNESS))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.WEAKNESS))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.HARMING))
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 10)
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 10)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ROCKET, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.FIREWORK_ROCKET, 7, 0)
			);
		}

		// metal golem wave
		{
			map.add(GolemDungeons.SPAWN, LARGE_1, new SpawnConfig(DungeonFactionRegistry.REMNANT)
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0))
					.mat(ModularGolems.loc("copper"), 60)
					.mat(ModularGolems.loc("iron"), 20)
					.mat(ModularGolems.loc("gold"), noArm(20))
			);


			map.add(GolemDungeons.SPAWN, LARGE_2, new SpawnConfig(DungeonFactionRegistry.REMNANT)
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0))
					.mat(ModularGolems.loc("copper"), 50)
					.mat(ModularGolems.loc("iron"), 40)
					.mat(ModularGolems.loc("gold"), noArm(10)
							.add(GolemItems.SPEED.get(), 1))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_WEAPON))
			);


			map.add(GolemDungeons.SPAWN, LARGE_3, createBase()
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0)
							.add(GolemItems.SPEED.get(), 0.5f)
					)
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR_BETTER))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_WEAPON_BETTER))
			);
		}

		// humanoid wave
		{
			map.add(GolemDungeons.SPAWN, HUMANOID_BASIC, new SpawnConfig(DungeonFactionRegistry.REMNANT)
					.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 0))
					.mat(ModularGolems.loc("copper"), 50)
					.mat(ModularGolems.loc("iron"), 40)
					.mat(ModularGolems.loc("gold"), noArm(10))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_MELEE, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_MELEE))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RANGED, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(30, ITEM_HUMANOID_BOW))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_TIPPED, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(50, ITEM_HUMANOID_TIPPED))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_ROCKET, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ROCKET))
			);

		}

		// generic wave
		{
			map.add(GolemDungeons.SPAWN, FACTORY_ALL, createBaseHumanoid().asTrialKey(FACTORY_ALL)
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0)
							.add(GolemItems.SPEED.get(), 0.5f)
					)
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR_BETTER))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_WEAPON_BETTER))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_ARMOR_MISC)
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN)
							.add(20, ITEM_HUMANOID_ARMOR_GOLD))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_MELEE)
							.add(30, ITEM_HUMANOID_BOW)
							.add(50, ITEM_HUMANOID_TIPPED)
							.add(20, ITEM_HUMANOID_ROCKET))
			);
		}

		// trial
		{
			map.add(GolemDungeons.TRIAL, FACTORY_ALL, new TrialConfig().setReward(GDLootGen.FACTORY)
					.setCost(200).setTriggerRange(22, -5, 16)
					.add(of(LARGE_1, 1))
					.add(of(LARGE_1, 1),
							of(HUMANOID_BASIC, 2))
					.add(of(LARGE_1, 2),
							of(HUMANOID_MELEE, 1),
							of(HUMANOID_RANGED, 2))
					.add(of(LARGE_1, 1),
							of(LARGE_2, 1),
							of(HUMANOID_MELEE, 1),
							of(HUMANOID_RANGED, 1),
							of(HUMANOID_TIPPED, 1))
					.add(of(LARGE_2, 3),
							of(HUMANOID_MELEE, 2),
							of(HUMANOID_RANGED, 2),
							of(HUMANOID_TIPPED, 2),
							of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_3, 2),
							of(HUMANOID_MELEE, 2),
							of(HUMANOID_RANGED, 1),
							of(HUMANOID_TIPPED, 2),
							of(HUMANOID_ROCKET, 1),
							of(FACTORY_ALL, 3))
					.add(of(LARGE_3, 3),
							of(HUMANOID_MELEE, 2),
							of(HUMANOID_RANGED, 1),
							of(HUMANOID_TIPPED, 2),
							of(HUMANOID_ROCKET, 2),
							of(FACTORY_ALL, 5))
			);
		}
	}

	private static SpawnConfig createBase() {
		return new SpawnConfig(DungeonFactionRegistry.REMNANT)
				.mat(ModularGolems.loc("copper"), 50)
				.mat(ModularGolems.loc("iron"), 40)
				.mat(ModularGolems.loc("gold"), noArm(10)
						.add(GolemItems.SPEED.get(), 1))
				.upgrade(Items.AIR, 170)
				.upgrade(GolemItems.QUARTZ.asItem(), 100)
				.upgrade(GolemItems.GOLD.asItem(), 100)
				.upgrade(GolemItems.SPEED.asItem(), 100)
				.upgrade(GolemItems.DIAMOND.asItem(), 30);
	}

	private static SpawnConfig createBaseHumanoid() {
		return createBase()
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 0.5)
						.addMount(EntityType.HORSE, 50)
						.addMount(EntityType.SKELETON_HORSE, 50)
						.addMount(GolemTypes.ENTITY_DOG.get(), 100)
				)
				.type(GolemTypes.TYPE_DOG.get(), new SpawnConfig.GolemTypeEntry(0, 0)
						.add(GolemItems.DIAMOND.get(), 0.75f)
						.add(GolemItems.SIZE_UPGRADE.get(), 0.5f)
				)
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.HORSE)
						.add(20, ITEM_HORSE_ARMOR))
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.SKELETON_HORSE)
						.add(20, ITEM_HORSE_ARMOR));
	}

}
