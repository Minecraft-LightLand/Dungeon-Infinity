
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.dungeon_infinity.content.trigger;

import dev.xkmc.dungeon_infinity.init.data.DITriggers;
import dev.xkmc.l2core.serial.advancements.BaseCriterion;
import dev.xkmc.l2core.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;

public class EnterRoomTrigger extends BaseCriterion<EnterRoomTrigger.Ins, EnterRoomTrigger> {

	public static Ins height(int h) {
		Ins ans = new Ins();
		ans.height = h;
		return ans;
	}

	public static Ins total(int t) {
		Ins ans = new Ins();
		ans.total = t;
		return ans;
	}

	public static Ins boss() {
		Ins ans = new Ins();
		ans.isBoss = true;
		return ans;
	}

	public static Ins quad() {
		Ins ans = new Ins();
		ans.isQuad = true;
		return ans;
	}

	public static Ins whole() {
		Ins ans = new Ins();
		ans.wholeGraph = true;
		return ans;
	}

	public static Ins none() {
		return new Ins();
	}

	public EnterRoomTrigger() {
		super(Ins.class);
	}

	public void trigger(ServerPlayer player, int height, int total, boolean isBoss, boolean isQuad, boolean wholeGraph) {
		this.trigger(player, e -> e.test(height, total, isBoss, isQuad, wholeGraph));
	}

	@SerialClass
	public static class Ins extends BaseCriterionInstance<Ins, EnterRoomTrigger> {

		@SerialField
		private int height = -1;
		@SerialField
		private int total = 0;
		@SerialField
		private boolean isBoss = false;
		@SerialField
		private boolean isQuad = false;
		@SerialField
		private boolean wholeGraph = false;

		public Ins() {
			super(DITriggers.ENTER.get());
		}

		public boolean test(int h, int tot, boolean boss, boolean quad, boolean whole) {
			if (height >= 0 && h > height) return false;
			if (total > 0 && tot < total) return false;
			if (isBoss && !boss) return false;
			if (isQuad && !quad) return false;
			if (wholeGraph && !whole) return false;
			return true;
		}

	}
}
