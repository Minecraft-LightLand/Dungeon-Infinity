package dev.xkmc.dungeon_infinity.content.buff.core;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.dungeon_infinity.content.buff.special.*;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.List;

public class AllBuffs {

	public static final AttrBuff SPEED = new AttrBuff(DungeonInfinity.loc("speed"), 10, List.of(
			new AttrBuff.AttrEntry(Attributes.MOVEMENT_SPEED, 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
	));

	public static final AttrBuff REACH = new AttrBuff(DungeonInfinity.loc("reach"), 5, List.of(
			new AttrBuff.AttrEntry(Attributes.ENTITY_INTERACTION_RANGE, 1, AttributeModifier.Operation.ADD_VALUE)
	));

	public static final AttrBuff ARMOR = new AttrBuff(DungeonInfinity.loc("armor"), 5, List.of(
			new AttrBuff.AttrEntry(Attributes.ARMOR, 4, AttributeModifier.Operation.ADD_VALUE),
			new AttrBuff.AttrEntry(Attributes.ARMOR_TOUGHNESS, 2, AttributeModifier.Operation.ADD_VALUE)
	));

	public static final ItemBuff TREASURE = new ItemBuff(DungeonInfinity.loc("treasure"), 10, List.of(
			new ItemStackTemplate(GolemItems.RECYCLE),
			new ItemStackTemplate(Items.DIAMOND, 2),
			new ItemStackTemplate(Items.EMERALD, 16)
	));

	public static final PotionBuff BLESS = new PotionBuff(DungeonInfinity.loc("bless"), 10, List.of(
			new PotionBuff.PotionEntry(MobEffects.RESISTANCE, 2, 1200),
			new PotionBuff.PotionEntry(MobEffects.REGENERATION, 1, 1200),
			new PotionBuff.PotionEntry(MobEffects.SPEED, 3, 1200),
			new PotionBuff.PotionEntry(MobEffects.INVISIBILITY, 0, 1200)
	));

	public static final InsuranceBuff INSURANCE = new InsuranceBuff(DungeonInfinity.loc("insurance"), 10, 100, List.of(
			new ItemStackTemplate(DIItems.KEY_OF_TOMB),
			new ItemStackTemplate(Items.EMERALD, 16)
	));

	public static final ExtraRewardBuff REWARD = new ExtraRewardBuff(DungeonInfinity.loc("reward"), 12, 7, List.of(
			new ItemStackTemplate(Items.EMERALD, 1)
	));

	public static final NinjaBuff NINJA = new NinjaBuff(DungeonInfinity.loc("ninja"), 1,
			new PotionBuff.PotionEntry(MobEffects.INVISIBILITY, 0, 1200));

	public static final HealGolemBuff HEAL = new HealGolemBuff(DungeonInfinity.loc("heal"), 2);
	public static final ShieldBuff SHIELD = new ShieldBuff(DungeonInfinity.loc("shield"), 10);
	public static final ChorusBuff CHORUS = new ChorusBuff(DungeonInfinity.loc("chorus"), 20);

	public static final MazeBuff SIGHT = new MazeBuff(DungeonInfinity.loc("sight"), 1);
	public static final MazeBuff ENDER = new MazeBuff(DungeonInfinity.loc("ender"), 1);
	public static final MazeBuff HEARTHEART = new MazeBuff(DungeonInfinity.loc("heartheart"), 1);
	public static final MazeBuff CARD_MASTER = new MazeBuff(DungeonInfinity.loc("card_master"), 5);

	public static final List<Pair<MazeBuff, Integer>> LARGE_BUFFS = List.of(
			Pair.of(TREASURE, 3),
			Pair.of(BLESS, 5),
			Pair.of(INSURANCE, 5),
			Pair.of(HEAL, 1),
			Pair.of(REWARD, 4),
			Pair.of(SHIELD, 5),
			Pair.of(SIGHT, 1),
			Pair.of(ENDER, 1),
			Pair.of(NINJA, 1)
	);
	public static final List<Pair<MazeBuff, Integer>> SMALL_BUFFS = List.of(
			Pair.of(TREASURE, 1),
			Pair.of(BLESS, 1),
			Pair.of(INSURANCE, 1),
			Pair.of(REWARD, 1),
			Pair.of(SHIELD, 1),
			Pair.of(SPEED, 1),
			Pair.of(REACH, 1),
			Pair.of(ARMOR, 1),
			Pair.of(CHORUS, 3),
			Pair.of(CARD_MASTER, 1),
			Pair.of(HEARTHEART, 1)
	);

	public static void genLang(RegistrateLangProvider pvd) {
		SPEED.genLang(pvd, "Talaria", "While player is in the maze, grant:");
		REACH.genLang(pvd, "Spring Arm", "While player is in the maze, grant:");
		ARMOR.genLang(pvd, "Bedrock Armor", "While player is in the maze, grant:");
		TREASURE.genLang(pvd, "Treasure of Sanctity", "Immediately receive items:");
		BLESS.genLang(pvd, "Holy Blessing", "Immediately gain effects:");
		INSURANCE.genLang(pvd, "Maze Insurance", "When player dies and revives in maze, grant player:");
		HEAL.genLang(pvd, "Medal of Victory", "Heal all your golems in maze or inventory by %s%%");
		REWARD.genLang(pvd, "Treasure Chest", "For every unit cell of battle room player defeats:");
		SHIELD.genLang(pvd, "Dungeon Forcefield", "Reduce damage taken by player by %s%%");
		SIGHT.genLang(pvd, "Vision of Truth", "Double room finder rewards. +1 sight.");
		ENDER.genLang(pvd, "Ender Force", "You can teleport to other anchors anywhere in the maze when you are not in active battle");
		CARD_MASTER.genLang(pvd, "Card Master", "Gain %s reroll chance whenever you gain a new blessing");
		HEARTHEART.genLang(pvd, "Hearth heart", "All humanoid armors and tools you picked up will be smelted into 1 respective ingot");
		NINJA.genLang(pvd, "Shadow Ninja", "Gain %s when entering battle. Immune to all damage from mobs not targeting you.");
		CHORUS.genLang(pvd, "Chorus Power", "May teleport to target location after using room finder.");
	}

	public static void register() {

	}

}
