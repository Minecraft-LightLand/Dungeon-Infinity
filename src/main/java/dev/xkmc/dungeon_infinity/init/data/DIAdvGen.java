package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.dungeon_infinity.content.trigger.DefeatRoomTrigger;
import dev.xkmc.dungeon_infinity.content.trigger.EnterRoomTrigger;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2core.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2core.serial.advancements.CriterionBuilder;
import net.minecraft.world.item.Items;

import static dev.xkmc.l2core.serial.advancements.CriterionBuilder.Provider.one;

public class DIAdvGen {

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		var gen = new AdvancementGenerator(pvd, DungeonInfinity.MODID);
		var tab = gen.new TabBuilder("main");
		var helper = new CriterionBuilder.Provider(pvd.getProvider());
		var root = tab.root("visit", DIItems.KEY_OF_ACCESS.asItem(), one(EnterRoomTrigger.none().build()),
				"Welcome to Dungeon Infinity", "Visit the maze").add(new RewardBuilder(0, DILootProvider.ENTRY));
		root.create("visit_100", Items.STONE, one(EnterRoomTrigger.total(100).build()),
				"Explorer", "Explorer 100 maze cells");
		var defeat = root.create("defeat_room", Items.CRACKED_STONE_BRICKS, one(DefeatRoomTrigger.none().build()),
				"First Encounter", "Defeat the first room");
		defeat.create("defeat_100", Items.SMOOTH_STONE, one(DefeatRoomTrigger.total(100).build()),
				"Experienced Adventurer", "Defeat 100 rooms");
		var quad = defeat.create("defeat_quad", Items.STONE_BRICKS, one(DefeatRoomTrigger.quad().build()),
				"Map Room", "Defeat a large cell and unlock the whole map");
		defeat.create("clear", Items.STONE_SWORD, one(DefeatRoomTrigger.whole().build()),
				"The Collector", "Defeat all rooms in a layer");
		var boss = quad.create("defeat_boss", Items.CHISELED_STONE_BRICKS, one(DefeatRoomTrigger.boss().build()),
				"The Central Command", "Defeat a boss room and unlock the next layer");
		var last = boss.create("last", Items.SCULK, one(EnterRoomTrigger.height(0).build()),
				"The Bottom", "Enter the bottom layer of the maze");
		last.create("conquerer", Items.SCULK_SHRIEKER, one(DefeatRoomTrigger.quad(0).build()),
				"The Conquerer", "Defeat a large cell of the bottom layer");
		root.build();
	}

}
