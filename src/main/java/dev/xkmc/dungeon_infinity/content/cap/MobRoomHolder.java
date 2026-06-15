package dev.xkmc.dungeon_infinity.content.cap;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MobRoomHolder {

	private final @Nullable SectionRoom[][][] rooms;

	public final MobRoomTicker data;
	public final List<SectionRoom> list = new ArrayList<>();
	public final SectionRoom holder;

	public MobRoomHolder(@Nullable SectionRoom[][][] rooms) {
		this.rooms = rooms;
		for (SectionRoom[][] ess : rooms) {
			for (SectionRoom[] es : ess) {
				for (SectionRoom room : es) {
					if (room == null) continue;
					list.add(room);
					room.ins = this;
				}
			}
		}
		var spawner = rooms[0].length > 1 ? rooms[1][0][1] : list.getFirst();
		holder = rooms[0].length > 1 ? rooms[1][1][1] : list.getFirst();
		if (holder.data == null) {
			holder.data = spawner.createSpawner(rooms);
		}
		data = holder.data;
	}

	public void setWall(boolean gen) {
		int xn = rooms.length;
		for (int x = 0; x < xn; x++) {
			int yn = rooms[x].length;
			for (int y = 0; y < yn; y++) {
				int zn = rooms[x][y].length;
				for (int z = 0; z < zn; z++) {
					var room = rooms[x][y][z];
					if (room == null) continue;
					room.walled = gen;
					int cell = room.getCell();
					if ((cell & 1) != 0 && (x == 0 || rooms[x - 1][y][z] == null))
						room.setWall(Direction.WEST, gen);
					if ((cell & 2) != 0 && (x >= xn - 1 || rooms[x + 1][y][z] == null))
						room.setWall(Direction.EAST, gen);
					if ((cell & 4) != 0 && (z == 0 || rooms[x][y][z - 1] == null))
						room.setWall(Direction.NORTH, gen);
					if ((cell & 8) != 0 && (z >= zn - 1 || rooms[x][y][z + 1] == null))
						room.setWall(Direction.SOUTH, gen);
					if ((cell & 32) != 0)
						room.setWall(Direction.DOWN, gen);
				}
			}
		}
	}

	public void tick(ServerPlayer sp) {
		if (data.isDefeated()) return;
		data.track(sp);
		data.tick(this);
	}

	public boolean contains(LivingEntity sp) {
		var p = sp.position().add(sp.getBbHeight() / 2);
		for (var r : list) {
			var origin = new Vec3(r.getBlockPos());
			var box = new AABB(origin, origin.add(16, 16, 16));
			if (box.contains(p)) return true;
		}
		return false;
	}
}
