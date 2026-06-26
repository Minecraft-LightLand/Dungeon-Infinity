package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;

import java.util.ArrayList;
import java.util.List;

public class MapLogger {

	private final long seed;
	private final int x, y, z;
	private boolean print;

	public final List<String> list = new ArrayList<>();

	public MapLogger(long seed, int x, int y, int z) {
		this.seed = seed;
		this.x = x;
		this.y = y;
		this.z = z;
		print = x == 0 && z == 0 && y == 15;
	}


	public void print(String msg, Object... obj) {
		if (!print) return;
		var text = msg.formatted(obj);
		list.add(text);
		DungeonInfinity.LOGGER.info(text);
	}

}
