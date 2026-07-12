package dev.xkmc.dungeon_infinity.content.buff.special;

import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public class ExtraRewardBuff extends MazeBuff {

	private final List<ItemStackTemplate> items;
	private final int exp;

	public ExtraRewardBuff(Identifier id, int max, int exp, List<ItemStackTemplate> items) {
		super(id, max);
		this.exp = exp;
		this.items = items;
	}

	@Override
	public void onDefeat(ServerPlayer sp, int lv, int size) {
		sp.giveExperiencePoints(exp * lv);
		for (int i = 0; i < lv * size; i++) {
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
		ans.add(Component.literal("- ").append(DILang.EXP.get(exp * lv)));
		return ans;
	}

}
