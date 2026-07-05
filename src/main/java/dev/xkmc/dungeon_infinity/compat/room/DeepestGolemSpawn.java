
package dev.xkmc.dungeon_infinity.compat.room;

import dev.xkmc.dungeon_infinity.compat.GolemSpawnData;
import dev.xkmc.dungeon_infinity.compat.MazeRoomLootGen;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DIConfigGen;
import dev.xkmc.golemdungeons.content.config.EquipmentConfig;
import dev.xkmc.golemdungeons.content.config.ExtraEquipmentSlot;
import dev.xkmc.golemdungeons.content.config.SpawnConfig;
import dev.xkmc.golemdungeons.content.config.TrialConfig;
import dev.xkmc.golemdungeons.content.faction.DungeonFactionRegistry;
import dev.xkmc.golemdungeons.init.GolemDungeons;
import dev.xkmc.golemdungeons.init.data.spawn.AbstractGolemSpawn;
import dev.xkmc.golemdungeons.init.reg.GDItems;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class DeepestGolemSpawn extends AbstractGolemSpawn {

	public static Identifier loc(String id) {
		return DungeonInfinity.loc("deepest/" + id);
	}

	public static final Identifier ITEM_LARGE_ARMOR_2 = loc("large_armor_2");
	public static final Identifier ITEM_LARGE_WEAPON_3 = loc("large_weapon_3");
	public static final Identifier ITEM_LARGE_SHOULDER = loc("large_weapon_shoulder");
	public static final Identifier ITEM_HUMANOID_ARMOR_MISC = loc("humanoid_armor_misc");
	public static final Identifier ITEM_HUMANOID_MELEE = loc("humanoid_weapon_melee");
	public static final Identifier ITEM_HUMANOID_RIDER_MELEE = loc("humanoid_weapon_rider_melee");
	public static final Identifier ITEM_HUMANOID_BOW = loc("humanoid_weapon_bow");
	public static final Identifier ITEM_HUMANOID_TIPPED = loc("humanoid_weapon_tipped_arrow");
	public static final Identifier ITEM_HUMANOID_ROCKET = loc("humanoid_weapon_rocket_crossbow");
	public static final Identifier ITEM_HORSE_ARMOR = loc("horse_armor");
	public static final Identifier ITEM_DOG_ARMOR = loc("dog_armor");

	public static final Identifier LARGE_3 = loc("large_3");
	public static final Identifier LARGE_SHOULDER = loc("large_shoulder");

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
			map.add(GolemDungeons.ITEMS, ITEM_LARGE_ARMOR_2, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 100, GolemItems.BARBARICFLAMEVANGUARD_HELMET, 30)
					.add(EquipmentSlot.CHEST, 100, GolemItems.BARBARICFLAMEVANGUARD_CHESTPLATE, 30)
					.add(EquipmentSlot.LEGS, 100, GolemItems.BARBARICFLAMEVANGUARD_SHINGUARD, 30)
					.add(EquipmentSlot.FEET, 100, GolemItems.BARBARICFLAMEVANGUARD_BOOTS, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_WEAPON_3, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, GDItems.ANCIENT_FORGE.get(), 30)
					.add(EquipmentSlot.MAINHAND, 100, GDItems.FLAME_SWORD.get(), 30)
					.add(EquipmentSlot.MAINHAND, 200, GDItems.SCULK_SCYTHE.get(), 30)
			);

			var ench = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
			ench.set(DIConfigGen.resolve(Enchantments.POWER), 5);
			ench.set(DIConfigGen.resolve(Enchantments.MULTISHOT), 1);
			var patch = DataComponentPatch.builder()
					.set(DataComponents.ENCHANTMENTS, ench.toImmutable())
					.build();
			ItemStackTemplate bow = new ItemStackTemplate(GolemItems.NETHERITE_BOW.get(), patch);

			map.add(GolemDungeons.ITEMS, ITEM_LARGE_SHOULDER, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, bow)
					.add(EquipmentSlot.OFFHAND, 100, Items.ARROW, 0)
					.add(ExtraEquipmentSlot.LEFT_SHOULDER, new EquipmentConfig.EquipmentEntry(
							100, GolemItems.FLAME_THROWER.get()))
					.add(ExtraEquipmentSlot.RIGHT_SHOULDER, new EquipmentConfig.EquipmentEntry(
							100, GolemItems.BEACON_CANNON.get()))
					.add(ExtraEquipmentSlot.ARROW, new EquipmentConfig.EquipmentEntry(
							50, Items.FIRE_CHARGE))
					.add(ExtraEquipmentSlot.ARROW, new EquipmentConfig.EquipmentEntry(
							50, Items.TNT))
			);

		}

		// humanoid armors
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ARMOR_MISC, new EquipmentConfig()
					.add(EquipmentSlot.HEAD, 50, Items.NETHERITE_HELMET, 30)
					.add(EquipmentSlot.CHEST, 50, Items.NETHERITE_CHESTPLATE, 30)
					.add(EquipmentSlot.LEGS, 50, Items.NETHERITE_LEGGINGS, 30)
					.add(EquipmentSlot.FEET, 50, Items.NETHERITE_BOOTS, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HORSE_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.BODY, 50, Items.NETHERITE_HORSE_ARMOR)
			);

			map.add(GolemDungeons.ITEMS, ITEM_DOG_ARMOR, new EquipmentConfig()
					.add(EquipmentSlot.BODY, 30, Items.WOLF_ARMOR)
			);

		}

		// humanoid weapons
		{
			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.NETHERITE_AXE, 30)
					.add(EquipmentSlot.MAINHAND, 100, Items.NETHERITE_SWORD, 30)
					.add(EquipmentSlot.MAINHAND, 150, Items.TRIDENT, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.SHIELD, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_RIDER_MELEE, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 200, Items.NETHERITE_SPEAR, 30)
					.add(EquipmentSlot.MAINHAND, 100, Items.TRIDENT, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.SHIELD, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_BOW, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.ARROW)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_TIPPED, new EquipmentConfig()
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.STRONG_POISON))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.STRONG_SLOWNESS))
					.add(EquipmentSlot.OFFHAND, 100, tipped(Potions.WEAKNESS))
					.add(EquipmentSlot.OFFHAND, 200, tipped(Potions.STRONG_HARMING))
					.add(EquipmentSlot.MAINHAND, 100, Items.BOW, 30)
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 30)
			);

			map.add(GolemDungeons.ITEMS, ITEM_HUMANOID_ROCKET, new EquipmentConfig()
					.add(EquipmentSlot.MAINHAND, 100, Items.CROSSBOW, 30)
					.add(EquipmentSlot.OFFHAND, 100, Items.FIREWORK_ROCKET, 25, 0)
			);
		}

		// metal golem wave
		{
			map.add(GolemDungeons.SPAWN, LARGE_3, createBase()
					.upgradeChance(1f, 1f, 1f, 1f)
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR_2))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_WEAPON_3))
			);


			map.add(GolemDungeons.SPAWN, LARGE_SHOULDER, createBase()
					.upgradeChance(1f, 1f, 1f, 1f)
					.type(GolemTypes.TYPE_GOLEM.get(), new SpawnConfig.GolemTypeEntry(30, 0))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_ARMOR_2))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_GOLEM.get())
							.add(100, ITEM_LARGE_SHOULDER))
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
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_MELEE)
							.add(40, ITEM_HUMANOID_RIDER_MELEE))
			);

			map.add(GolemDungeons.SPAWN, HUMANOID_RIDER_RANGED, createRider()
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(20, ITEM_HUMANOID_ARMOR_MISC))
					.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_HUMANOID.get())
							.add(30, ITEM_HUMANOID_BOW)
							.add(30, ITEM_HUMANOID_ROCKET)
							.add(30, ITEM_HUMANOID_TIPPED))
			);

		}

		// trial
		{


			// BASIC: 基础混编
			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_ROOM_BASIC, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_BASIC)
					.add(of(LARGE_3, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
			);

			// LARGE: 大型兵+远程护卫
			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_ROOM_LARGE, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_LARGE)
					.add(of(LARGE_3, 2), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_3, 2), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_3, 2), of(LARGE_3, 1), of(HUMANOID_RANGED, 2), of(HUMANOID_ROCKET, 1))
			);

			// RANGED: 远程火力网+火箭兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_ROOM_RANGED, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_RANGED)
					.add(of(LARGE_3, 1), of(HUMANOID_RANGED, 2))
					.add(of(LARGE_3, 1), of(LARGE_SHOULDER, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
					.add(of(LARGE_3, 1), of(LARGE_SHOULDER, 2), of(LARGE_SHOULDER, 1), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
			);

			// RIDER: 火箭骑兵+药水兵
			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_ROOM_RIDER, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_RIDER)
					.add(of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 1))
					.add(of(HUMANOID_RIDER_MELEE, 2), of(HUMANOID_RIDER_RANGED, 2))
					.add(of(HUMANOID_RIDER_MELEE, 3), of(HUMANOID_RIDER_RANGED, 3))
			);

			// MIXED: 全兵种混合
			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_ROOM_MIXED, new TrialConfig().setReward(MazeRoomLootGen.SCULK_ROOM_MIXED)
					.add(of(LARGE_3, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1))
					.add(of(LARGE_3, 1), of(LARGE_SHOULDER, 1), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_3, 2), of(LARGE_SHOULDER, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 2), of(HUMANOID_TIPPED, 1))
			);

			map.add(GolemDungeons.TRIAL, GolemSpawnData.DEEPEST_QUAD, new TrialConfig().setReward(MazeRoomLootGen.SCULK_QUAD)
					.add(of(LARGE_3, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RANGED, 1), of(HUMANOID_ROCKET, 1))
					.add(of(LARGE_3, 2), of(LARGE_SHOULDER, 2), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 1), of(HUMANOID_RIDER_RANGED, 2), of(HUMANOID_TIPPED, 1))
					.add(of(LARGE_3, 3), of(LARGE_SHOULDER, 3), of(HUMANOID_MELEE, 1), of(HUMANOID_RIDER_MELEE, 2), of(HUMANOID_RIDER_RANGED, 1), of(HUMANOID_ROCKET, 1), of(HUMANOID_TIPPED, 1))
			);

		}
	}

	private static SpawnConfig createBase() {
		return new SpawnConfig(DungeonFactionRegistry.REMNANT)
				.mat(ModularGolems.loc("sculk"), 100)
				.upgrade(GolemItems.GOLD.asItem(), 200)
				.upgrade(GolemItems.SPEED.asItem(), 100)
				.upgrade(GolemItems.NETHERITE.asItem(), 100);
	}

	private static SpawnConfig createBaseHumanoid() {
		return createBase()
				.upgradeChance(1f, 1f, 1f)
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
				.upgradeChance(1f, 1f, 1f)
				.type(GolemTypes.TYPE_HUMANOID.get(), new SpawnConfig.GolemTypeEntry(40, 1)
						.addMount(EntityType.HORSE, 50)
						.addMount(EntityType.SKELETON_HORSE, 50)
						.addMount(GolemTypes.ENTITY_DOG.get(), 100)
				)
				.type(GolemTypes.TYPE_DOG.get(), new SpawnConfig.GolemTypeEntry(0, 0)
						.add(GolemItems.SIZE_UPGRADE.get(), 1f)
						.add(GolemItems.SPEED.get(), 0.5f)
				)
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.HORSE)
						.add(20, ITEM_HORSE_ARMOR))
				.equipments(new SpawnConfig.EquipmentGroup(EntityType.SKELETON_HORSE)
						.add(20, ITEM_HORSE_ARMOR))
				.equipments(new SpawnConfig.EquipmentGroup(GolemTypes.ENTITY_DOG.get())
						.add(20, ITEM_DOG_ARMOR));
	}

}
