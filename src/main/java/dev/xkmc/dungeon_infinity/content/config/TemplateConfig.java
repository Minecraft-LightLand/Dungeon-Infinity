package dev.xkmc.dungeon_infinity.content.config;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import org.jspecify.annotations.Nullable;

import java.util.*;

@SerialClass
public class TemplateConfig extends BaseConfig {

	public static TemplateConfig get() {
		return DungeonInfinity.TEMPLATES.getMerged();
	}

	public static CompiledSet of(String path) {
		return get().cache.get(path);
	}

	public static CompiledSet of(int cell) {
		return get().indexed[TemplateMapper.getTemplateIndex(cell)];
	}

	public static TemplateData getEntry(int cell) {
		return get().indexed[TemplateMapper.getTemplateIndex(cell)]
				.data[CellInterpreter.getStyle(cell)]
				.data[CellInterpreter.getVariant(cell)];
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final LinkedHashMap<String, LinkedHashMap<Identifier, TemplateData>> templates = new LinkedHashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialField
	public final LinkedHashMap<Identifier, SpawnPool> spawn = new LinkedHashMap<>();

	private final LinkedHashMap<String, CompiledSet> cache = new LinkedHashMap<>();
	private CompiledSet[] indexed;
	private String[] ids;
	private Map<String, Integer> revMap;

	@Override
	protected void postMerge() {
		Set<String> styles = new LinkedHashSet<>();
		for (var sub : templates.values())
			for (var key : sub.keySet())
				styles.add(key.getNamespace());
		List<String> keys = new ArrayList<>(styles);
		keys.sort(Comparator.comparing(e -> e));
		int n = keys.size();
		ids = new String[n];
		revMap = new LinkedHashMap<>();
		for (int i = 0; i < n; i++) {
			ids[i] = keys.get(i);
			revMap.put(ids[i], i);
		}
		for (var ent : templates.entrySet()) {
			cache.put(ent.getKey(), new CompiledSet(ids, ent.getKey(), ent.getValue()));
		}
		indexed = new CompiledSet[TemplateMapper.ROOMS.length];
		for (int i = 0; i < indexed.length; i++) {
			indexed[i] = cache.get(TemplateMapper.ROOMS[i]);
		}
	}

	public int styleCount() {
		return ids.length;
	}

	public String styleName(int index) {
		return ids[index];
	}

	public int styleIndex(String style) {
		return revMap.getOrDefault(style, 0);
	}

	public StyleBuilder start(String style) {
		return new StyleBuilder(this, style);
	}

	public record TemplateData(int weight, String path, @Nullable Identifier spawn) {

		public TemplateData() {
			this(100, "", null);
		}

	}

	public record SpawnContext(int size, int y) {

	}

	public record SpawnPool(int sizeScale, int depthScale, ArrayList<Entry> list) {

		public record Entry(SpawnContext point, Identifier id, int weight) {
		}

		public SpawnPool() {
			this(0, 0, new ArrayList<>());
		}

		public SpawnPool(Identifier id) {
			this(0, 0, new ArrayList<>(List.of(new Entry(new SpawnContext(0, 0), id, 100))));
		}

		private int dist(SpawnContext ctx, SpawnContext point) {
			int ds = ctx.size - point.size;
			int dy = ctx.y - point.y;
			return ds * ds * sizeScale + dy * dy * depthScale;
		}

		public @Nullable Identifier fetch(SpawnContext ctx, RandomSource rand) {
			List<Entry> candidates = new ArrayList<>();
			int score = Integer.MAX_VALUE;
			for (var e : list) {
				int sc = dist(ctx, e.point);
				if (sc < score) {
					candidates.clear();
					score = sc;
				}
				if (sc == score) {
					candidates.add(e);
				}
			}
			if (candidates.isEmpty()) return null;
			WeightedList.Builder<Entry> builder = new WeightedList.Builder<>();
			for (var e : candidates) {
				builder.add(e, e.weight);
			}
			var opt = builder.build().getRandom(rand);
			if (opt.isEmpty()) return null;
			return opt.get().id();
		}

	}

	public static class CompiledSet {

		private final String room;
		private final String[] ids;
		private final CompiledRoom[] data;

		private CompiledSet(String[] ids, String room, Map<Identifier, TemplateData> map) {
			this.room = room;
			Map<String, List<Pair<String, TemplateData>>> split = new LinkedHashMap<>();
			for (var ent : map.entrySet()) {
				var id = ent.getKey();
				split.computeIfAbsent(id.getNamespace(), _ -> new ArrayList<>())
						.add(Pair.of(id.getPath(), ent.getValue()));
			}
			int n = ids.length;
			this.ids = ids;
			data = new CompiledRoom[n];
			for (int i = 0; i < n; i++) {
				var x = split.get(ids[i]);
				data[i] = new CompiledRoom(room, x == null ? new ArrayList<>() : x);
			}
		}

		public int variantCount(int i) {
			return data[i].ids.length;
		}

		public TemplateData variant(int i, int j) {
			return data[i].data[j];
		}

		public int variantIndex(int i, String variant) {
			if (!data[i].revMap.containsKey(variant)) {
				DungeonInfinity.LOGGER.throwing(new IllegalStateException("Set " + room + " at style " + ids[i] + " has no variant " + variant));
				return 0;
			}
			return data[i].revMap.get(variant);
		}

		public String path(String room, int i, int j) {
			return data[i].path(j);
		}

		public int getRandom(int i, RandomSource rand) {
			return data[i].weighted.getRandomOrThrow(rand);
		}

	}

	private static class CompiledRoom {

		private final String room;
		private final String[] ids;
		private final TemplateData[] data;
		private final WeightedList<Integer> weighted;
		private final Map<String, Integer> revMap = new LinkedHashMap<>();

		private CompiledRoom(String room, List<Pair<String, TemplateData>> list) {
			this.room = room;
			list.sort(Comparator.comparing(Pair::getFirst));
			int n = list.size();
			ids = new String[n];
			data = new TemplateData[n];
			var builder = WeightedList.<Integer>builder();
			for (int i = 0; i < n; i++) {
				ids[i] = list.get(i).getFirst();
				data[i] = list.get(i).getSecond();
				builder.add(i, data[i].weight());
				revMap.put(ids[i], i);
			}
			weighted = builder.build();
		}

		private String path(int i) {
			return data[i].path();
		}

	}

	public static class StyleBuilder {

		private final TemplateConfig config;
		private final String style;

		private String root;

		private @Nullable Identifier defaultSpawn = null;

		private StyleBuilder(TemplateConfig config, String style) {
			this.config = config;
			this.style = style;
			root = style + "/";
		}

		public VariantBuilder room(String room) {
			return new VariantBuilder(this, room);
		}

		public TemplateConfig end() {
			return config;
		}

		public StyleBuilder root(String folder) {
			root = folder + "/";
			return this;
		}

		public StyleBuilder spawn(String id, SpawnPool pool) {
			defaultSpawn = Identifier.fromNamespaceAndPath(style, id);
			config.spawn.put(defaultSpawn, pool);
			return this;
		}

		public StyleBuilder endSpawn() {
			defaultSpawn = null;
			return this;
		}

	}

	public static class VariantBuilder {

		private final StyleBuilder parent;
		private final String room;

		private VariantBuilder(StyleBuilder parent, String room) {
			this.parent = parent;
			this.room = room;
		}

		public VariantBuilder variant(String suffix, int weight) {
			return variant(suffix, weight, room + suffix, parent.defaultSpawn);
		}

		public VariantBuilder variant(String suffix, int weight, @Nullable Identifier id) {
			return variant(suffix, weight, room + suffix, id);
		}

		public VariantBuilder variant(String suffix, int weight, String path) {
			return variant(suffix, weight, path + suffix, null);
		}

		public VariantBuilder variants(String... names) {
			for (var name : names)
				variant(name, 100, name, null);
			return this;
		}

		public VariantBuilder variant(String suffix, int weight, String path, @Nullable Identifier id) {
			parent.config.templates.computeIfAbsent(room, k -> new LinkedHashMap<>())
					.put(Identifier.fromNamespaceAndPath(parent.style, suffix), new TemplateData(weight, parent.root + path, id));
			return this;
		}

		public StyleBuilder end() {
			return parent;
		}

	}

}
