package dev.xkmc.dungeon_infinity.content.buff.core;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class AttrBuff extends MazeBuff {

	private static final Identifier DUMMY = Identifier.withDefaultNamespace("dummy");

	public record AttrEntry(Holder<Attribute> attr, double amount, AttributeModifier.Operation op) {

		public Component getDesc(int lv) {
			return attr.value().toComponent(new AttributeModifier(DUMMY, amount * lv, op), TooltipFlag.NORMAL);
		}
	}

	private final List<AttrEntry> attrs;

	public AttrBuff(Identifier id, int max, List<AttrEntry> attrs) {
		super(id, max);
		this.attrs = attrs;
	}

	@Override
	public void onUpdate(ServerPlayer sp, int lv) {
		for (var e : attrs) {
			var ins = sp.getAttribute(e.attr);
			if (ins == null) continue;
			if (ins.hasModifier(id)) {
				ins.removeModifier(id);
			}
			if (lv == 0) continue;
			ins.addPermanentModifier(new AttributeModifier(id, e.amount * lv, e.op));
		}
	}

	@Override
	public List<Component> getDetail(int lv) {
		List<Component> ans = new ArrayList<>(super.getDetail(lv));
		for (var e : attrs)
			ans.add(Component.literal("- ").append(e.getDesc(lv)));
		return ans;
	}

}
