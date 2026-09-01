package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.dungeon_infinity.content.buff.core.AllBuffs;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import javax.annotation.Nullable;
import java.util.Locale;

public enum DILang {
	TOMB("tooltip.tomb", "Retrieve up to 9 items from previous death in the maze", 0, ChatFormatting.GRAY),
	TOMB_ITEM_COUNT("tooltip.tomb_item_count", "You have %s items to be retrieved", 1, ChatFormatting.GRAY),
	ACCESS("tooltip.access", "Teleport into or out of the maze", 0, ChatFormatting.GRAY),
	ACCESS_POS("tooltip.access_pos", "Bound to (%s,%s,%s) in maze", 3, ChatFormatting.GRAY),
	MAP("tooltip.map", "Right click to open map. Click on Respawn Anchor to teleport to other Anchors", 0, ChatFormatting.GRAY),
	WAYPOINT("tooltip.waypoint", "Teleport", 0, null),


	DEPTH("legend.depth", "Depth: %s", 1, null),
	FINDER("legend.finder", "Finders left: %s", 1, null),
	CHORUS("legend.chorus","Teleport to path finder target (%s)",1,null),
	UP("legend.up", "↑", 0, null),
	DOWN("legend.down", "↓", 0, null),
	MAGNIFIER("legend.magnifier", "\uD83D\uDD0E\uFE0E", 0, null),
	BATTLE("legend.battle", "Battle Room", 0, null),
	QUAD("legend.quad", "Large Battle Room", 0, null),
	BOSS("legend.boss", "Boss Room", 0, null),
	DOWN_STAIR("legend.down_stair", "Downward Stairs", 0, null),
	UP_STAIR("legend.up_stair", "Upward Stairs", 0, null),
	WORKSHOP("legend.workshop", "Workshop Room", 0, null),
	SHOP("legend.shop", "Merchant Room", 0, null),
	WAREHOUSE("legend.warehouse", "Warehouse Room", 0, null),

	REFRESH("info.refresh", "Reroll", 0, null),
	REMAIN("info.remain", "Remaining: %s", 1, null),
	CONFIRM("info.confirm", "Confirm", 0, null),
	SELECT_TITLE("info.select_title", "Select one reward", 0, null),
	EXP("info.exp", "%s Xp", 1, null),
	ROOM_TYPES("info.room_types", "Room Types:", 0, null),
	SEARCH("info.search", "Search", 0, null),
	BUFF_TITLE("info.buff_title", "Maze Blessings", 0, null),
	INFO_TITLE("info.info_title", "Maze Info", 0, null),
	MAP_SETTINGS("map_settings", "Map Settings", 0, null),
	SCREEN_MAP("screen_map", "Screen Map", 0, null),
	FOLLOW_PLAYER("follow_player", "Follow Player", 0, null),
	POSITION("position", "Position", 0, null),

	OVERLAY_SCALE("overlay_scale", "Scale: %s%%", 1, null),
	OVERLAY_SIZE("overlay_size", "Size: %s%%", 1, null),
	ON("on", "ON", 0, null),
	OFF("off", "OFF", 0, null),
	DONE("done", "Done", 0, null),
	SCREEN_MAP_OFF("screen_map.off", "OFF", 0, null),
	SCREEN_MAP_HOLD("screen_map.hold", "Hold", 0, null),
	SCREEN_MAP_HIDE("screen_map.hide", "Hide", 0, null),
	SCREEN_MAP_ALWAYS("screen_map.always", "Always", 0, null);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;

	DILang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = DungeonInfinity.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent getTranslate(String s) {
		return Component.translatable(ModularGolems.MODID + "." + s);
	}

	public static MutableComponent fromTrial(Identifier id) {
		return Component.translatable(Util.makeDescriptionId("trial", id));
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (DILang lang : DILang.values()) {
			pvd.add(lang.key, lang.def);
		}
		AllBuffs.genLang(pvd);
		pvd.add(DungeonInfinity.MODID + ".merchant.groceries", "Grocery Vendor");
		pvd.add(DungeonInfinity.MODID + ".merchant.blacksmith", "Black Smith");
		pvd.add(DungeonInfinity.MODID + ".merchant.recycler", "Metal Scrapper");
		String[] styles = new String[]{"early", "stone", "mineshaft", "copper", "deepslate", "sculk", "deepest"};
		String[] styleName = new String[]{"Dungeon", "Dungeon", "Mineshaft", "Trial", "Deep", "Sculk", "Abyss"};
		String[] rooms = new String[]{"room.basic", "room.large", "room.ranged", "room.rider", "room.mixed", "quad", "boss"};
		String[] roomName = new String[]{"Soldiers", "Crushers", "Archers", "Riders", "Army", "Elites", "Gatekeepers"};
		for (int i = 0; i < styles.length; i++) {
			for (int j = 0; j < rooms.length; j++) {
				pvd.add("trial." + DungeonInfinity.MODID + "." + styles[i] + "." + rooms[j], styleName[i] + " " + roomName[j]);
			}
		}
	}
}
