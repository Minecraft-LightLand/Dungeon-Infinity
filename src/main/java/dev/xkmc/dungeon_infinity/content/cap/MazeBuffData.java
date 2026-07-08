package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.content.buff.MazeBuff;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@SerialClass
public class MazeBuffData {

	@SerialField
	public final LinkedHashMap<Identifier, Integer> buffs = new LinkedHashMap<>();

	private boolean isInDim = false;

	public void intoDim(ServerPlayer sp) {
		if (isInDim) return;
		isInDim = true;
		for (var e : buffs.entrySet()) {
			MazeBuff.get(e.getKey()).onUpdate(sp, e.getValue());
		}
	}

	public void outOfDim(ServerPlayer sp) {
		if (!isInDim) return;
		isInDim = false;
		for (var e : buffs.entrySet()) {
			MazeBuff.get(e.getKey()).onUpdate(sp, 0);
		}
		//buffs.clear();
		sync(sp);
	}

	public void sync(ServerPlayer sp) {
		//TODO
	}

	public void onRevive(ServerPlayer sp) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onRevive(sp, buffs.getOrDefault(e, 0));
		}
	}

	public void onDefeat(ServerPlayer sp, int size) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onDefeat(sp, buffs.getOrDefault(e, 0), size);
		}
	}

	public void onDamage(ServerPlayer sp, DamageData.Defence data) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onDamage(sp, buffs.getOrDefault(e, 0), data);
		}
	}
}
