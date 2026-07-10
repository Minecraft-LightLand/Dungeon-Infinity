package dev.xkmc.dungeon_infinity.content.buff;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public class ItemBuff extends MazeBuff {

	private final List<ItemStackTemplate> items;

	public ItemBuff(Identifier id, int max, List<ItemStackTemplate> items) {
		super(id, max);
		this.items = items;
	}

	@Override
	public void onApply(ServerPlayer sp, int lv) {
		for (int i = 0; i < lv; i++) {
			for (var e : items) {
				sp.getInventory().placeItemBackInInventory(e.create());
			}
		}
	}

	@Override
	public List<Component> getDetail(int lv) {
		List<Component> ans = new ArrayList<>(super.getDetail(lv));
		for (ItemStackTemplate item : items)
			ans.add(Component.literal("- ").append(Component.translatable("item.container.item_count",
					item.create().getHoverName(), item.count() * lv)));
		return ans;
	}

}
