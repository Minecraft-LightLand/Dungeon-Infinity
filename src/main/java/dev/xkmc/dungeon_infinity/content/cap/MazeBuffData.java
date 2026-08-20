package dev.xkmc.dungeon_infinity.content.cap;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.dungeon_infinity.content.buff.core.AllBuffs;
import dev.xkmc.dungeon_infinity.content.buff.core.MazeBuff;
import dev.xkmc.dungeon_infinity.content.packet.SyncBuffToClient;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SerialClass
public class MazeBuffData {

	@SerialField
	public final LinkedHashMap<Identifier, Integer> buffs = new LinkedHashMap<>();

	@SerialField
	public int largeBuff, smallBuff, rerollChance;

	@SerialField
	public long seed;

	private boolean isInDim = false;

	public void whileInMaze(ServerPlayer sp) {
		if (seed == 0) {
			seed = sp.getRandom().nextLong();
			sync(sp);
		}
		if (isInDim) return;
		isInDim = true;
		for (var e : buffs.entrySet()) {
			MazeBuff.get(e.getKey()).onUpdate(sp, e.getValue());
		}
	}

	public void whileOutOfMaze(ServerPlayer sp) {
		if (!isInDim) return;
		isInDim = false;
		for (var e : buffs.entrySet()) {
			MazeBuff.get(e.getKey()).onUpdate(sp, 0);
		}
		sp.removeAllEffects();
	}

	public void sync(ServerPlayer sp) {
		DungeonInfinity.HANDLER.toClientPlayer(new SyncBuffToClient(this), sp);
	}

	public void onRevive(ServerPlayer sp) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onRevive(sp, buffs.getOrDefault(e, 0));
		}
	}

	public void onDefeat(ServerPlayer sp, int size, SectionRoom holder) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onDefeat(sp, buffs.getOrDefault(e, 0), size);
		}
		rerollChance += 1 + buffs.getOrDefault(AllBuffs.CARD_MASTER.id, 0);
		if (holder.isBoss()) {
			largeBuff++;
			sync(sp);
		} else if (holder.isQuad()) {
			smallBuff++;
			sync(sp);
		}
	}

	public boolean onAttacked(ServerPlayer sp, DamageData.Attack data) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			if (MazeBuff.get(e).onAttacked(sp, buffs.getOrDefault(e, 0), data))
				return true;
		}
		return false;
	}

	public void onDamage(ServerPlayer sp, DamageData.Defence data) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onDamage(sp, buffs.getOrDefault(e, 0), data);
		}
	}

	public void onEnterBattle(ServerPlayer sp) {
		for (var e : new ArrayList<>(buffs.keySet())) {
			MazeBuff.get(e).onEnterBattle(sp, buffs.getOrDefault(e, 0));
		}
	}

	public List<Pair<MazeBuff, Integer>> getLargeBuffList() {
		var rand = RandomSource.create(seed);
		List<Pair<MazeBuff, Integer>> candidates = new ArrayList<>(AllBuffs.LARGE_BUFFS);
		candidates.removeIf(e -> !e.getFirst().fitsOn(this, e.getSecond()));
		if (candidates.size() <= 3) return candidates;
		List<Pair<MazeBuff, Integer>> ans = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			int sel = rand.nextInt(candidates.size());
			ans.add(candidates.remove(sel));
		}
		return ans;
	}

	public List<Pair<MazeBuff, Integer>> getSmallBuffList() {
		var rand = RandomSource.create(seed);
		List<Pair<MazeBuff, Integer>> candidates = new ArrayList<>(AllBuffs.SMALL_BUFFS);
		candidates.removeIf(e -> !e.getFirst().fitsOn(this, e.getSecond()));
		if (candidates.size() <= 3) return candidates;
		List<Pair<MazeBuff, Integer>> ans = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			int sel = rand.nextInt(candidates.size());
			ans.add(candidates.remove(sel));
		}
		return ans;
	}

	public void selLargeBuff(ServerPlayer sp, int index) {
		if (largeBuff <= 0) return;
		var list = getLargeBuffList();
		if (index < 0 || index >= list.size()) return;
		largeBuff--;
		seed = RandomSource.create(seed).nextLong();
		var pair = list.get(index);
		pair.getFirst().onApply(sp, pair.getSecond());
	}

	public void selSmallBuff(ServerPlayer sp, int index) {
		if (smallBuff <= 0) return;
		var list = getSmallBuffList();
		if (index < 0 || index >= list.size()) return;
		smallBuff--;
		seed = RandomSource.create(seed).nextLong();
		var pair = list.get(index);
		pair.getFirst().onApply(sp, pair.getSecond());
	}

	public void reroll() {
		if (rerollChance <= 0) return;
		rerollChance--;
		seed = RandomSource.create(seed).nextLong();
	}

}
