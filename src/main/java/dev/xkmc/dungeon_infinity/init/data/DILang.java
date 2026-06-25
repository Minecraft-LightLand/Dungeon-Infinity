package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import javax.annotation.Nullable;
import java.util.Locale;

public enum DILang {
	TOMB("tooltip.tomb", "Retrieve items from previous death in the maze", 0, ChatFormatting.GRAY),
	TOMB_ITEM_COUNT("tooltip.tomb_item_count", "You have %s items to be retrieved", 1, ChatFormatting.GRAY),
	ACCESS("tooltip.access", "Teleport into or out of the maze", 0, ChatFormatting.GRAY),

	DEPTH("legend.depth", "Depth: %s", 1, null),
	BATTLE("legend.battle", "Battle Room", 0, null),
	QUAD("legend.quad", "Large Battle Room", 0, null),
	BOSS("legend.boss", "Boss Room", 0, null),
	DOWN("legend.down", "Downward Stairs", 0, null),
	UP("legend.up", "Upward Stairs", 0, null),
	WORKSHOP("legend.workshop", "Workshop Room", 0, null),
	SHOP("legend.shop", "Merchant Room", 0, null),
	WAREHOUSE("legend.warehouse", "Warehouse Room", 0, null);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;

	DILang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = ModularGolems.MODID + "." + key;
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
	}
}
