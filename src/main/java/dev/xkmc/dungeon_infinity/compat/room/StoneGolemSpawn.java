
package dev.xkmc.dungeon_infinity.compat.room;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnData;
import dev.xkmc.dungeon_infinity.compat.MazeRoomLootGen;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.golemdungeons.content.config.EquipmentConfig;
import dev.xkmc.golemdungeons.content.config.SpawnConfig;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.content.faction.DungeonFactionRegistry;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
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

public class StoneGolemSpawn extends AbstractGolemSpawn {

	public static Identifier loc(String id) {
		return DungeonInfinity.loc("stone/" + id);
	}

	public static final Identifier ITEM_LARGE_ARMOR_1 = loc("large_armor");
	public static final Identifier ITEM_LARGE_WEAPON_1 = loc("large_weapon");
	public static final Identifier ITEM_HUMANOID_ARMOR_MISC = loc("humanoid_armor_misc");
	public static final Identifier ITEM_HUMANOID_ARMOR_CHAIN = loc("humanoid_armor_chain");
	public static final Identifier ITEM_HUMANOID_ARMOR_GOLD = loc("humanoid_armor_gold");
	public static final Identifier ITEM_HUMANOID_MELEE = loc("humanoid_weapon_melee");
	public static final Identifier ITEM_HUMANOID_RIDER_MELEE = loc("humanoid_weapon_rider_melee");
	public static final Identifier ITEM_HUMANOID_BOW = loc("humanoid_weapon_bow");
	public static final Identifier ITEM_HUMANOID_TIPPED = loc("humanoid_weapon_tipped_arrow");
	public static final Identifier ITEM_HUMANOID_ROCKET = loc("humanoid_weapon_rocket_crossbow");
	public static final Identifier ITEM_HORSE_ARMOR = loc("horse_armor");
	public static final Identifier ITEM_DOG_ARMOR = loc("dog_armor");

	public static final Identifier LARGE_1 = loc("large_1");
	public static final Identifier LARGE_2 = loc("large_2");

	public static final Identifier HUMANOID_MELEE = loc("humanoid_melee");
	public static final Identifier HUMANOID_RANGED = loc("humanoid_ranged");
	public static final Identifier HUMANOID_TIPPED = loc("humanoid_tipped");
	public static final Identifier HUMANOID_ROCKET = loc("humanoid_rocket");
	public static final Identifier HUMANOID_RIDER_MELEE = loc("humanoid_rider_melee");
	public static final Identifier HUMANOID_RIDER_RANGED = loc("humanoid_rider_ranged");

	public static final Identifier MIXED = loc("mixed");

	public static void add(ConfigDataProvider.Collector map) {

		// metal golem equipments
		{
			map.add(GolemDungeons.ITEMS, ITEM_LARGE_ARMOR_1, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 100)
					.add(EquipmentSlot.HEAD, 30, GolemItems.GOLEMGUARD_HELMET, 0)
					.add(EquipmentSlot.CHEST, 100)
					.add(EquipmentSlot.CHEST, 30, GolemItems.GOLEMGUARD_CHESTPLATE, 0)
					.add(EquipmentSlot.LEGS, 100)
					.add(EquipmentSlot.LEGS, 30, GolemItems.GOLEMGUARD_SHINGUARD, 0)
			);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_WEAPON_1, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.AXE), 0)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SWORD), 0)
					.add(EquipmentSlot.MAINHAND, 50, getWeapon(VanillaGolemWeaponMaterial.IRON, GolemWeaponType.SPEAR), 0)
			);
		}

		// humanoid armors
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_MISC, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 200)
					.add(EquipmentSlot.CHEST, 200)
					.add(EquipmentSlot.LEGS, 200)
					.add(EquipmentSlot.FEET, 200)
					.add(EquipmentSlot.HEAD, 100, Items.COPPER_HELMET, 0)
					.add(EquipmentSlot.CHEST, 100, Items.COPPER_CHESTPLATE, 0)
					.add(EquipmentSlot.LEGS, 100, Items.COPPER_LEGGINGS, 0)
					.add(EquipmentSlot.FEET, 100, Items.COPPER_BOOTS, 0)
					.add(EquipmentSlot.HEAD, 20, Items.IRON_HELMET, 0)
					.add(EquipmentSlot.CHEST, 20, Items.IRON_CHESTPLATE, 0)
					.add(EquipmentSlot.LEGS, 20, Items.IRON_LEGGINGS, 0)
					.add(EquipmentSlot.FEET, 20, Items.IRON_BOOTS, 0)
					.add(EquipmentSlot.HEAD, 20, Items.GOLDEN_HELMET, 15)
					.add(EquipmentSlot.CHEST, 20, Items.GOLDEN_CHESTPLATE, 15)
					.add(EquipmentSlot.LEGS, 20, Items.GOLDEN_LEGGINGS, 15)
					.add(EquipmentSlot.FEET, 20, Items.GOLDEN_BOOTS, 15)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_GOLD, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 50, Items.GOLDEN_HELMET, 15)
					.add(EquipmentSlot.CHEST, 50, Items.GOLDEN_CHESTPLATE, 15)
					.add(EquipmentSlot.LEGS, 50, Items.GOLDEN_LEGGINGS, 15)
					.add(EquipmentSlot.FEET, 50, Items.GOLDEN_BOOTS, 15)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HORSE_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.BODY, 100)
					.add(EquipmentSlot.BODY, 30, Items.LEATHER_HORSE_ARMOR)
					.add(EquipmentSlot.BODY, 40, Items.IRON_HORSE_ARMOR)
					.add(EquipmentSlot.BODY, 30, Items.GOLDEN_HORSE_ARMOR)
			);

			map.add(GolemDungeons.ITEMS, ITEM_DOG_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.BODY, 100)
					.add(EquipmentSlot.BODY, 30, Items.WOLF_ARMOR)
			);

		}

		// humanoid weapons
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.WOODEN_AXE, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.WOODEN_SWORD, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.STONE_AXE, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.STONE_SWORD, 0)
					.add(EquipmentSlot.MAINHAND, 200, Items.STONE_SHOVEL, 0)
					.add(EquipmentSlot.MAINHAND, 200, Items.STONE_PICKAXE, 0)
					.add(EquipmentSlot.OFFHAND, 20, Items.SHIELD, 0)
					.add(EquipmentSlot.OFFHAND, 100)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_RIDER_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 200, Items.WOODEN_SPEAR, 0)
					.add(EquipmentSlot.MAINHAND, 200, Items.STONE_SPEAR, 0)
					.add(EquipmentSlot.OFFHAND, 100, Items.SHIELD, 10)
					.add(EquipmentSlot.OFFHAND, 100)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_BOW, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 0)
					.add(EquipmentSlot.OFFHAND, 100, Items.ARROW)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_TIPPED, new EquipmentConfig()
					.add(EquipmentSlot.OFFHAND, 50, tipped(Potions.INFESTED))
					.add(EquipmentSlot.OFFHAND, 50, tipped(Potions.OOZING))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.SLOWNESS))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.WEAKNESS))
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 0)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ROCKET, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 0)
					.add(EquipmentSlot.OFFHAND, 100, Items.FIREWORK_ROCKET, 2, 0)
			);
		}

		// metal golem wave
		{
			map.add(GolemDungeons.SPAWN, LARGE_1, createBase()
					.upgradeChance(0.1f)
			);

			map.add(GolemDungeons.SPAWN, LARGE_2, createBase()
					.upgradeChance(0.3f)
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR_1))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_WEAPON_1))
			);

		}

		// humanoid wave
		{

			map.add(GolemDungeons.SPAWN, HUMANOID_MELEE, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(100, ITEM_HUMANOID_MELEE))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RANGED, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(30, ITEM_HUMANOID_BOW))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_TIPPED, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(50, ITEM_HUMANOID_TIPPED))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_ROCKET, createBaseHumanoid()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ROCKET))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RIDER_MELEE, createRider()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_MELEE)
							.add(40, ITEM_HUMANOID_RIDER_MELEE))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RIDER_RANGED, createRider()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_CHAIN))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(30, ITEM_HUMANOID_BOW)
							.add(30, ITEM_HUMANOID_ROCKET)
							.add(30, ITEM_HUMANOID_TIPPED))
			);

		}

		// trial
		{

			// BASIC: 基础混编
			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.STONE_ROOM_BASIC)
					.add(of(LARGE_1, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
			);

			// LARGE: 大型兵+远程护卫
			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.STONE_ROOM_LARGE)
					.add(of(LARGE_1, 2), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_2, 2), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_2, 3), of(HUMANOID_RANGED, 2), of(HUMANOID_ROCKET, 1))
			);

			// RANGED: 远程火力网+火箭兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.STONE_ROOM_RANGED)
					.add(of(LARGE_1, 1), of(HUMANOID_RANGED, 2))
					.add(of(LARGE_2, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
					.add(of(LARGE_2, 2), of(HUMANOID_RIDER_RANGED, 2), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
			);

			// RIDER: 火箭骑兵+药水兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.STONE_ROOM_RIDER)
					.add(of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 1))
					.add(of(HUMANOID_RIDER_MELEE, 2), of(HUMANOID_RIDER_RANGED, 2))
					.add(of(HUMANOID_RIDER_MELEE, 3), of(HUMANOID_RIDER_RANGED, 3))
			);

			// MIXED: 全兵种混合
			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.STONE_ROOM_MIXED)
					.add(of(LARGE_1, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_2, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_2, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 2), of(HUMANOID_TIPPED, 1))
			);

			map.add(GolemDungeons.TRIAL, GolemSpawnData.STONE_QUAD, new TrialConfig().setReward(MazeRoomLootGen.STONE_QUAD)
					.add(of(LARGE_2, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_2, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 2), of(HUMANOID_TIPPED, 1))
					.add(of(LARGE_2, 3), of(HUMANOID_MELEE, 2), of(HUMANOID_RIDER_MELEE, 2), of(HUMANOID_RIDER_RANGED, 3), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
			);

		}
	}

	private static SpawnConfig createBase() {
		return new SpawnConfig(DungeonFactionRegistry.REMNANT)
				.mat(ModularGolems.loc("copper"), 50)
				.mat(ModularGolems.loc("iron"), 40)
				.mat(ModularGolems.loc("gold"), noArm(10))
				.upgrade(GolemItems.QUARTZ.asItem(), 100)
				.upgrade(GolemItems.GOLD.asItem(), 30)
				.upgrade(GolemItems.SPEED.asItem(), 100)
				.upgrade(GolemItems.DIAMOND.asItem(), 50);
	}

	private static SpawnConfig createBaseHumanoid() {
		return createBase()
				.upgradeChance(0.3f)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 0.3)
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

	private static SpawnConfig createRider() {
		return createBase()
				.upgradeChance(0.3f)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 1)
						.addMount(EntityType.HORSE, 50)
						.addMount(EntityType.SKELETON_HORSE, 50)
						.addMount(GolemTypes.ENTITY_DOG.get(), 100)
				)
				.type(GolemTypes.TYPE_DOG.get(), new SpawnConfig.GolemTypeEntry(0, 0)
						.add(GolemItems.SPEED.get(), 0.5f)
						.add(GolemItems.SIZE_UPGRADE.get(), 1f)
				)
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.HORSE)
						.add(20, ITEM_HORSE_ARMOR))
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.SKELETON_HORSE)
						.add(20, ITEM_HORSE_ARMOR))
				.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_DOG.get())
						.add(20, ITEM_DOG_ARMOR));
	}

}
