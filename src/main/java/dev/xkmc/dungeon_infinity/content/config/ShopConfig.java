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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.*;

@SerialClass
public class ShopConfig extends BaseConfig {

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final Map<String, ArrayList<OfferPool>> shops = new LinkedHashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public final Map<Identifier, ArrayList<Entry>> offers = new LinkedHashMap<>();

	public static List<Entry> build(String key, RandomSource rand) {
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

		private final ArrayList<OfferPool> list = new ArrayList<>();

		public ShopBuilder(ShopConfig parent, String style) {
			this.parent = parent;
			this.style = style;
		}

		public PoolBuilder addPool(String id) {
			return new PoolBuilder(this, Identifier.fromNamespaceAndPath(style, id));
		}

		public ShopConfig end() {
			parent.shops.put(style, list);
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

		public ShopBuilder end(int count) {
			parent.parent.offers.put(id, list);
			parent.list.add(new OfferPool(id, count));
			return parent;
		}

	}

}
