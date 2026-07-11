package dev.xkmc.dungeon_infinity.content.config;

import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeSet;

@SerialClass
public class ColumnLayoutConfig extends BaseConfig {

	public static Layout getRandom(RandomSource rand) {
		var map = DungeonInfinity.COLUMN.getMerged();
		var keys = new ArrayList<>(new TreeSet<>(map.layouts.keySet()));
		var key = keys.get(rand.nextInt(keys.size()));
		return map.layouts.get(key);
	}

	@SerialField
	@ConfigCollect(CollectType.MAP_OVERWRITE)
	public final LinkedHashMap<String, Layout> layouts = new LinkedHashMap<>();

	public record Entry(String style, int layer) {

	}

	public record CombatRoomConfig(
			float[] chanceByType, float largeEndChance, float largeHallChance
	) {

		public static final float[] DEF_CHANCE = {0, 0.9f, 0.3f, 0.3f, 0.5f, 1};
		public static final CombatRoomConfig DEF = new CombatRoomConfig(DEF_CHANCE, 0.8f, 0.8f);

	}

	public record QuadRoomConfig(int min, int max, float rate) {

		public static final QuadRoomConfig DEF = new QuadRoomConfig(5, 10, 0.8f);

		public int getMaxQuadRoom(int total) {
			return Math.clamp((int) (total * rate), Math.min(total, min), max);
		}

	}

	public record Layout(ArrayList<Entry> styles, LinkedHashMap<String, Integer> utilities, CombatRoomConfig room,
	                     QuadRoomConfig quad) {

		public float getRoomChance(int cell) {
			int type = CellInterpreter.getTemplateType(cell);
			if (type < room.chanceByType.length)
				return room.chanceByType[type];
			return 0;
		}

		public float getEndLargeRoomChance() {
			return room.largeEndChance();
		}

		public int getMaxQuadRoom(int max) {
			return quad.getMaxQuadRoom(max);
		}

		public float getHallLargeRoomChance() {
			return room.largeHallChance();
		}

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
			parent.layouts.put(key, new Layout(styles, rooms, CombatRoomConfig.DEF, QuadRoomConfig.DEF));
			return parent;
		}

		public ColumnLayoutConfig end(CombatRoomConfig room, QuadRoomConfig quad) {
			parent.layouts.put(key, new Layout(styles, rooms, room, quad));
			return parent;
		}

	}

}
