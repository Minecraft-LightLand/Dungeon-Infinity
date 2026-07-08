package dev.xkmc.dungeon_infinity.content.buff;

import dev.xkmc.dungeon_infinity.init.data.DILang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public class InsuranceBuff extends MazeBuff {

	private final List<ItemStackTemplate> items;
	private final int exp;

	public InsuranceBuff(Identifier id, int max, int exp, List<ItemStackTemplate> items) {
		super(id, max);
		this.exp = exp;
		this.items = items;
	}

	public void onRevive(ServerPlayer sp, int lv) {
		removeOne(sp);
		sp.giveExperiencePoints(exp);
		for (var e : items) {
			sp.getInventory().add(e.create());
		}
	}

	@Override
	public List<Component> getDetail(int lv) {
		List<Component> ans = new ArrayList<>(super.getDetail(lv));
		for (ItemStackTemplate item : items)
			ans.add(Component.literal("- ").append(Component.translatable("item.container.item_count",
					item.create().getHoverName(), item.count())));
		ans.add(Component.literal("- ").append(DILang.EXP.get(exp)));
		return ans;
	}

}
