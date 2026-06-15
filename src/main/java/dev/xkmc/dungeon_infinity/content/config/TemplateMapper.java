package dev.xkmc.dungeon_infinity.content.config;

import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;

public class TemplateMapper {

	public static final String[] ROOMS = {
			"boss", "quad", "stairs", "cross_stairs",
			"room/end", "room/straight", "room/corner", "room/t_way", "room/cross",
			"path/end", "path/straight", "path/corner", "path/t_way", "path/cross"
	};

	public static int getTemplateIndex(int cell) {
		if (CellInterpreter.isBossRoom(cell)) {
			return 0;
		} else if (CellInterpreter.isQuadRoom(cell)) {
			return 1;
		} else {
			int open = CellInterpreter.getOpenings(cell);
			var ans = switch (open) {
				case 1, 2, 4, 8 -> 4;
				case 3, 12 -> 5;
				case 5, 6, 9, 10 -> 6;
				case 7, 11, 13, 14 -> 7;
				case 15 -> 8;
				case 17, 18, 20, 24, 33, 34, 36, 40 -> 2;
				case 19, 28, 35, 44 -> 3;
				default -> -1;
			};
			if (open < 16 && CellInterpreter.isHallway(cell)) {
				ans += 5;
			}
			return ans;
		}
	}

}
