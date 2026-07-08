package dev.xkmc.dungeon_infinity.content.buff;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedHashMap;
import java.util.List;

public class MazeBuff {

	public static final LinkedHashMap<Identifier, MazeBuff> MAP = new LinkedHashMap<>();

	public static synchronized <T extends MazeBuff> T register(T val) {
		MAP.put(val.id, val);
		return val;
	}

	public static MazeBuff get(Identifier id) {
		return MAP.get(id);
	}

	public final Identifier id;
	public final int max;

	public MazeBuff(Identifier id, int max) {
		this.id = id;
		this.max = max;
	}

	public int getMaxLevel() {
		return max;
	}

	public void addLevel(ServerPlayer sp, int lv) {
		var data = DIMeta.HISTORY.type().getOrCreate(sp).buff;
		int old = data.buffs.getOrDefault(id, 0);
		data.buffs.put(id, old + lv);
		onUpdate(sp, old + lv);
		data.sync(sp);
	}

	public void removeOne(ServerPlayer sp) {
		var data = DIMeta.HISTORY.type().getOrCreate(sp).buff;
		int lv = data.buffs.getOrDefault(id, 0);
		if (lv == 0) return;
		if (lv == 1) data.buffs.remove(id);
		else data.buffs.put(id, lv - 1);
		onUpdate(sp, lv - 1);
		data.sync(sp);
	}

	public void onUpdate(ServerPlayer sp, int lv) {

	}

	public void onApply(ServerPlayer sp, int lv) {
		addLevel(sp, lv);
	}

	public Component getTitle() {
		return Component.translatable(id.getNamespace() + ".buff." + id.getPath());
	}

	public List<Component> getDetail(int lv) {
		return List.of(Component.translatable(id.getNamespace() + ".buff." + id.getPath() + ".desc"));
	}

	public void genLang(RegistrateLangProvider pvd, String title, String desc) {
		pvd.add(id.getNamespace() + ".buff." + id.getPath(), title);
		pvd.add(id.getNamespace() + ".buff." + id.getPath() + ".desc", desc);
	}

	public void onDefeat(ServerPlayer sp, int lv, int size) {

	}

	public void onRevive(ServerPlayer sp, int lv) {

	}

	public void onDamage(ServerPlayer sp, int lv, DamageData.Defence data) {
	}

}
