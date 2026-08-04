package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.init.data.DITagGen;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class LostAndFoundData extends PlayerCapabilityTemplate<LostAndFoundData> {

	@SerialField
	public final List<ItemStack> list = new ArrayList<>();

	@SerialField
	public final List<ItemStack> important = new ArrayList<>();

	public List<ItemStack> poll(int count) {
		List<ItemStack> ans = new ArrayList<>();
		List<ItemStack> rem = new ArrayList<>();
		for (var e : list) {
			if (ans.size() < count) {
				ans.add(e);
			} else {
				rem.add(e);
			}
		}
		list.clear();
		list.addAll(rem);
		return ans;
	}

	public void add(ItemStack e) {
		if (e.isEmpty()) return;
		if (e.is(DITagGen.ALWAYS_KEEP)) {
			important.add(e);
		} else {
			list.add(e);
		}
	}

	@Override
	public void tick(Player player) {
		if (MazeHistory.inMazeDim(player)) {
			if (player instanceof ServerPlayer sp) {
				if (!important.isEmpty()) {
					sp.getInventory().placeItemBackInInventory(important.removeFirst());
				}
			}
		}
	}

	public void release(ServerPlayer sp) {
		for (var e : important)
			sp.getInventory().placeItemBackInInventory(e);
		for (var e : list)
			sp.getInventory().placeItemBackInInventory(e);
		important.clear();
		list.clear();
	}

}
