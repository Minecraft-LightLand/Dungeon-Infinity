package dev.xkmc.dungeon_infinity.content.config;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.*;

@SerialClass
public class ShopConfig extends BaseConfig {

	public static Map<String, Item> getAllTypes() {
		return DungeonInfinity.SHOPS.getMerged().icon;
	}

	public static List<Entry> build(Identifier key, RandomSource rand) {
		var config = DungeonInfinity.SHOPS.getMerged();
		List<Entry> ans = new ArrayList<>();
		var offerList = config.shops.get(key);
		if (offerList == null) return ans;
		for (var offer : offerList) {
			var list = config.offers.get(offer.offer());
			if (list == null || list.isEmpty()) continue;
			var set = new LinkedHashSet<>(list);
			for (int i = 0; i < offer.count; i++) {
				var builder = new WeightedList.Builder<Entry>();
				for (var e : set) {
					builder.add(e, e.weight());
				}
				var pool = builder.build();
				var opt = pool.getRandom(rand);
				if (opt.isPresent()) {
					set.remove(opt.get());
					ans.add(opt.get());
				}
			}
		}
		return ans;
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final LinkedHashMap<Identifier, ArrayList<OfferPool>> shops = new LinkedHashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final LinkedHashMap<Identifier, ArrayList<Entry>> offers = new LinkedHashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialField
	public final LinkedHashMap<String, Item> icon = new LinkedHashMap<>();

	public ShopBuilder start(String style) {
		return new ShopBuilder(this, style);
	}

	public record OfferPool(Identifier offer, int count) {

	}

	public record Entry(Item cost, int count, ItemStackTemplate result, int weight, int limit) {

	}

	public ShopConfig setIcon(Map<String, Item> map) {
		icon.putAll(new TreeMap<>(map));
		return this;
	}

	public static class ShopBuilder {

		private final ShopConfig parent;
		private final String style;

		public ShopBuilder(ShopConfig parent, String style) {
			this.parent = parent;
			this.style = style;
		}

		public PoolBuilder addPool(String id) {
			return new PoolBuilder(this, Identifier.fromNamespaceAndPath(style, id));
		}

		public ShopBuilder shop(String type, Map<String, Integer> map) {
			//map = new TreeMap<>(map);
			ArrayList<OfferPool> list = new ArrayList<>();
			for (var ent : map.entrySet()) {
				list.add(new OfferPool(Identifier.fromNamespaceAndPath(style, ent.getKey()), ent.getValue()));
			}
			parent.shops.put(Identifier.fromNamespaceAndPath(style, type), list);
			return this;
		}

		public ShopConfig end() {
			return parent;
		}

		public ShopBuilder shop(String type, String k1, int v1, String k2, int v2) {
			var map = new LinkedHashMap<String, Integer>();
			map.put(k1, v1);
			map.put(k2, v2);
			return shop(type, map);
		}

		public ShopBuilder shop(String type, String k1, int v1, String k2, int v2, String k3, int v3) {
			var map = new LinkedHashMap<String, Integer>();
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			return shop(type, map);
		}

		public ShopBuilder shop(String type, String k1, int v1, String k2, int v2, String k3, int v3, String k4, int v4) {
			var map = new LinkedHashMap<String, Integer>();
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			map.put(k4, v4);
			return shop(type, map);
		}

		public ShopBuilder shop(String type, String k1, int v1, String k2, int v2, String k3, int v3, String k4, int v4, String k5, int v5) {
			var map = new LinkedHashMap<String, Integer>();
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			map.put(k4, v4);
			map.put(k5, v5);
			return shop(type, map);
		}

	}

	public static class PoolBuilder {

		private final ShopBuilder parent;
		private final Identifier id;

		private Item currency = Items.EMERALD;

		private final ArrayList<Entry> list = new ArrayList<>();

		public PoolBuilder(ShopBuilder parent, Identifier id) {
			this.parent = parent;
			this.id = id;
		}

		public PoolBuilder setCurrency(Item item) {
			currency = item;
			return this;
		}

		public PoolBuilder add(Entry entry) {
			list.add(entry);
			return this;
		}

		public PoolBuilder add(int cost, Item result, int count, int weight, int limit) {
			return add(new Entry(currency, cost, new ItemStackTemplate(result, count), weight, limit));
		}

		public PoolBuilder add(int cost, ItemStackTemplate stack, int weight, int limit) {
			return add(new Entry(currency, cost, stack, weight, limit));
		}

		public PoolBuilder buy(Item input, int count, int price, int weight, int limit) {
			return add(new Entry(input, count, new ItemStackTemplate(currency, price), weight, limit));
		}

		public ShopBuilder end() {
			parent.parent.offers.put(id, list);
			return parent;
		}

	}

}
