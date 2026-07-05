
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
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class EarlyGolemSpawn extends AbstractGolemSpawn {

	public static Identifier loc(String id) {
		return DungeonInfinity.loc("early/" + id);
	}

	public static final Identifier ITEM_HUMANOID_ARMOR_MISC = loc("humanoid_armor_misc");
	public static final Identifier ITEM_HUMANOID_MELEE = loc("humanoid_weapon_melee");
	public static final Identifier ITEM_HUMANOID_RIDER_MELEE = loc("humanoid_weapon_rider_melee");
	public static final Identifier ITEM_HUMANOID_BOW = loc("humanoid_weapon_bow");

	public static final Identifier LARGE_1 = loc("large_1");

	public static final Identifier HUMANOID_MELEE = loc("humanoid_melee");
	public static final Identifier HUMANOID_RANGED = loc("humanoid_ranged");
	public static final Identifier HUMANOID_RIDER_MELEE = loc("humanoid_rider_melee");
	public static final Identifier HUMANOID_RIDER_RANGED = loc("humanoid_rider_ranged");

	public static void add(ConfigDataProvider.Collector map) {

		// humanoid equipments
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_MISC, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 200)
					.add(EquipmentSlot.CHEST, 200)
					.add(EquipmentSlot.LEGS, 200)
					.add(EquipmentSlot.FEET, 200)
					.add(EquipmentSlot.HEAD, 50, Items.COPPER_HELMET, 0)
					.add(EquipmentSlot.CHEST, 50, Items.COPPER_CHESTPLATE, 0)
					.add(EquipmentSlot.LEGS, 50, Items.COPPER_LEGGINGS, 0)
					.add(EquipmentSlot.FEET, 50, Items.COPPER_BOOTS, 0)
					.add(EquipmentSlot.HEAD, 10, Items.IRON_HELMET, 0)
					.add(EquipmentSlot.CHEST, 10, Items.IRON_CHESTPLATE, 0)
					.add(EquipmentSlot.LEGS, 10, Items.IRON_LEGGINGS, 0)
					.add(EquipmentSlot.FEET, 10, Items.IRON_BOOTS, 0)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 200, Items.WOODEN_SHOVEL, 0)
					.add(EquipmentSlot.MAINHAND, 200, Items.WOODEN_PICKAXE, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.STONE_SHOVEL, 0)
					.add(EquipmentSlot.MAINHAND, 100, Items.STONE_PICKAXE, 0)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_RIDER_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 200, Items.WOODEN_SPEAR, 0)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_BOW, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 0)
					.add(EquipmentSlot.OFFHAND, 100, Items.ARROW)
			);

		}

		// metal golem wave
		{
			map.add(GolemDungeons.SPAWN, LARGE_1, createBase()
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(1000, 0))
					.upgradeChance(0.1f)
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

			map.add(GolemDungeons.SPAWN, HUMANOID_RIDER_MELEE, createRider()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_MELEE)
							.add(40, ITEM_HUMANOID_RIDER_MELEE))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RIDER_RANGED, createRider()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(30, ITEM_HUMANOID_BOW))
			);

		}

		// trial
		{

			// BASIC: 基础混编
			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_ROOM_BASIC)
					.add(of(HUMANOID_MELEE, 2))
			);

			// LARGE: 大型兵+远程护卫
			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_ROOM_LARGE)
					.add(of(LARGE_1, 1))
					.add(of(LARGE_1, 1), of(HUMANOID_RANGED, 1))
			);

			// RANGED: 远程火力网+火箭兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_ROOM_RANGED)
					.add(of(HUMANOID_RANGED, 2))
					.add(of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 2))
			);

			// RIDER: 火箭骑兵+药水兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_ROOM_RIDER)
					.add(of(HUMANOID_RIDER_MELEE, 1))
					.add(of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 1))
			);

			// MIXED: 全兵种混合
			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_ROOM_MIXED)
					.add(of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
					.add(of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_1, 1), of(HUMANOID_RANGED, 1))
			);

			map.add(GolemDungeons.TRIAL, GolemSpawnData.EARLY_QUAD, new TrialConfig().setReward(MazeRoomLootGen.MINESHAFT_QUAD)
					.add(of(LARGE_1, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_1, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RANGED, 2))
					.add(of(LARGE_1, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RANGED, 2), of(HUMANOID_RIDER_RANGED, 1))
			);

		}
	}

	private static SpawnConfig createBase() {
		return new SpawnConfig(DungeonFactionRegistry.REMNANT)
				.mat(ModularGolems.loc("copper"), 60)
				.mat(ModularGolems.loc("iron"), 30)
				.upgrade(GolemItems.QUARTZ.asItem(), 100)
				.upgrade(GolemItems.SPEED.asItem(), 100);
	}

	private static SpawnConfig createBaseHumanoid() {
		return createBase()
				.upgradeChance(0.1f)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 0.3)
						.addMount(EntityType.HORSE, 50)
						.addMount(EntityType.SKELETON_HORSE, 50)
						.addMount(GolemTypes.ENTITY_DOG.get(), 100)
				)
				.type(GolemTypes.TYPE_DOG.get(), new SpawnConfig.GolemTypeEntry(0, 0)
						.add(GolemItems.DIAMOND.get(), 0.75f)
						.add(GolemItems.SIZE_UPGRADE.get(), 0.5f)
				);
	}

	private static SpawnConfig createRider() {
		return createBase()
				.upgradeChance(0.1f)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 1)
						.addMount(EntityType.HORSE, 50)
						.addMount(EntityType.SKELETON_HORSE, 50)
						.addMount(GolemTypes.ENTITY_DOG.get(), 100)
				)
				.type(GolemTypes.TYPE_DOG.get(), new SpawnConfig.GolemTypeEntry(0, 0)
						.add(GolemItems.SPEED.get(), 0.5f)
						.add(GolemItems.SIZE_UPGRADE.get(), 1f)
				);
	}

}
