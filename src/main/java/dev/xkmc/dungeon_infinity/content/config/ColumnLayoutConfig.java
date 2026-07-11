package dev.xkmc.dungeon_infinity.content.config;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

@SerialClass
public class ColumnLayoutConfig extends BaseConfig {

	public static Layout getRandom(RandomSource rand) {
		var map = DungeonInfinity.COLUMN.getMerged();
		var keys = new ArrayList<>(new TreeSet<>(map.columnStyles.keySet()));
		var key = keys.get(rand.nextInt(keys.size()));
		var list = map.columnStyles.get(key);
		return new Layout(list.toArray(Entry[]::new), map.specialRooms.getOrDefault(key, new LinkedHashMap<>()));
	}

	@SerialField
	@ConfigCollect(CollectType.MAP_COLLECT)
	public final LinkedHashMap<String, ArrayList<Entry>> columnStyles = new LinkedHashMap<>();

	@SerialField
	@ConfigCollect(CollectType.MAP_COLLECT)
	public final LinkedHashMap<String, LinkedHashMap<String, Integer>> specialRooms = new LinkedHashMap<>();

	public record Entry(String style, int layer) {

	}

	public record Layout(Entry[] entries, Map<String, Integer> rooms) {

	}

	public ColumnBuilder column(String key) {
		return new ColumnBuilder(this, key);
	}

	public static class ColumnBuilder {

		private final ColumnLayoutConfig parent;
		private final String key;

		private final ArrayList<Entry> styles = new ArrayList<>();
		private final LinkedHashMap<String, Integer> rooms = new LinkedHashMap<>();

		public ColumnBuilder(ColumnLayoutConfig parent, String key) {
			this.parent = parent;
			this.key = key;
		}

		public ColumnBuilder style(String style, int layer) {
			styles.add(new Entry(style, layer));
			return this;
		}

		public ColumnBuilder room(String style, int layer) {
			rooms.put(style, layer);
			return this;
		}

		public ColumnLayoutConfig end() {
			parent.columnStyles.put(key, styles);
			parent.specialRooms.put(key, rooms);
			return parent;
		}

	}

}
