package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.content.cap.packet.DefeatRoomToClient;
import dev.xkmc.dungeon_infinity.content.spawn.MobSpawnTicker;
import dev.xkmc.dungeon_infinity.events.MazeRoomBattleStartEvent;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@SerialClass
public class MobRoomTicker {

	public final Set<ServerPlayer> players = new LinkedHashSet<>();

	@SerialField
	public final Set<UUID> playerIds = new LinkedHashSet<>();
	@SerialField
	public boolean started = false;
	@SerialField
	public long lastTick = -1;
	@SerialField
	public MobSpawnTicker spawner;

	public boolean isDefeated() {
		return !players.isEmpty() && started && !spawner.isActive();
	}

	public void track(ServerPlayer sp) {
		if (sp.isSpectator()) return;
		players.add(sp);
		playerIds.add(sp.getUUID());
	}

	private void sanitize(ServerLevel level, MobRoomHolder ins) {
		players.clear();
		for (var id : playerIds) {
			var p = level.getPlayerByUUID(id);
			if (!(p instanceof ServerPlayer sp)) continue;
			if (p.isSpectator()) continue;
			if (sp.level() != level || !sp.isAlive()) continue;
			if (!ins.contains(sp)) {
				if (sp.isCreative())
					continue;
				DIMeta.HISTORY.type().getOrCreate(sp).teleportToRoom(sp, ins.holder.getBlockPos().offset(8, 3, 8));
			}
			players.add(sp);
		}
		playerIds.clear();
		for (var e : players) {
			playerIds.add(e.getUUID());
		}

	}

	public boolean mayStart() {
		for (var e : players) {
			if (!e.isCreative()) return true;
		}
		return false;
	}

	private void stop(MobRoomHolder ins) {
		if (isDefeated()) {
			for (var e : players) {
				var data = DIMeta.HISTORY.type().getOrCreate(e);
				ArrayList<MazePos> points = new ArrayList<>();
				for (var r : ins.list) {
					var mp = MazePos.map(r.getBlockPos());

					points.add(mp);
				}
				data.defeat(e, points, ins.holder);
				if (ins.holder.isLarge()) {
					data.getOrCreate(MazePos.map(ins.holder.getBlockPos())).markVisible(0, 0, 25, 25);
				}
				data.activeMobRoom = null;
				DungeonInfinity.HANDLER.toClientPlayer(new DefeatRoomToClient(points, ins.holder.isLarge()), e);
			}
			for (var r : ins.list) {
				r.ins = null;
				r.data = null;
			}
		}
		started = false;
		ins.setWall(false);
		players.clear();
		playerIds.clear();
		spawner.stop(ins.holder.level(), ins);
	}

	public void tick(MobRoomHolder ins) {
		ins.markUnsaved();
		var level = ins.holder.level();
		var time = level.getGameTime();
		if (time <= lastTick) return;
		lastTick = time;
		sanitize(level, ins);
		if (!started && mayStart()) {
			started = true;
			ins.setWall(true);
			spawner.start(level, ins);
			DIMeta.ACTIVE.type().getOrCreate(level).activeRooms.add(ins.holder.getBlockPos());
			for (var e : players) {
				NeoForge.EVENT_BUS.post(new MazeRoomBattleStartEvent(e, ins));
			}
		}
		if (started) spawner.tick(level, ins);
		if (started && (!spawner.isActive() || players.isEmpty())) {
			stop(ins);
		}
	}

}
