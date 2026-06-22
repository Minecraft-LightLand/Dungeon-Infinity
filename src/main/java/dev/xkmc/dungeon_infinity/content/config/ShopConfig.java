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

	public static Set<String> getAllTypes() {
		return DungeonInfinity.SHOPS.getMerged().types;
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

	private final Set<String> types = new LinkedHashSet<>();

	@Override
	protected void postMerge() {
		for (var e : shops.keySet())
			types.add(e.getPath());
	}


	public ShopBuilder start(String style) {
		return new ShopBuilder(this, style);
	}

	public record OfferPool(Identifier offer, int count) {

	}

	public record Entry(Item cost, int count, ItemStackTemplate result, int weight, int limit) {

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

	}

	public static class PoolBuilder {

		private final ShopBuilder parent;
		private final Identifier id;

		private final ArrayList<Entry> list = new ArrayList<>();

		public PoolBuilder(ShopBuilder parent, Identifier id) {
			this.parent = parent;
			this.id = id;
		}

		public PoolBuilder add(Entry entry) {
			list.add(entry);
			return this;
		}

		public PoolBuilder add(int cost, Item result, int count, int weight, int limit) {
			return add(new Entry(Items.EMERALD, cost, new ItemStackTemplate(result, count), weight, limit));
		}

		public ShopBuilder end() {
			parent.parent.offers.put(id, list);
			return parent;
		}

	}

}
