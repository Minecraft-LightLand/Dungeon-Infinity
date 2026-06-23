package dev.xkmc.dungeon_infinity.content.chunkgen;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

/**
 * bit 0-5: directional data
 * bit 6-10: boss room data
 * bit 11-12: room type
 * bit 13-20: style
 * bit 21-28: variant
 */
public class CellInterpreter {

	public static int getOpenings(int cell) {
		return cell & 63;
	}

	public static boolean isBossRoom(int cell) {
		var data = ((cell >> 6) & 31) - 5;
		return data >= 0;
	}

	public static int getBossRoom(int cell) {
		return ((cell >> 6) & 31) - 5;
	}

	public static int setBossRoom(int offset, int dx, int dz) {
		int data = offset * 9 + dx * 3 + dz + 5;
		return data << 6;
	}

	public static boolean isQuadRoom(int cell) {
		var data = ((cell >> 6) & 31) - 1;
		return data >= 0 && data < 4;
	}

	public static int getQuadRoom(int cell) {
		return ((cell >> 6) & 31) - 1;
	}

	public static int setQuadRoom(int dx, int dz) {
		int data = dx * 2 + dz + 1;
		return data << 6;
	}

	static int setRoomTypeMask(int cell, int marker) {
		if (marker > ROOM) marker = ROOM;
		return (marker - 1) << 11;
	}

	private static int getRoomType(int cell) {
		return (cell >> 11) & 3;
	}

	public static int getStyle(int cell) {
		return (cell >> 13) & 0xFF;
	}

	public static int getVariant(int cell) {
		return (cell >> 21) & 0xFF;
	}

	public static int setStyleAndVariant(int style, int variant) {
		return (style << 13) | (variant << 21);
	}


	public static final int SPECIAL = 1, HALLWAY = 2, ROOM = 3;

	/**
	 * 1: special
	 * 2: hallway
	 * 3: room
	 */
	public static int getRoomMarker(int cell, int flag) {
		return switch (flag) {
			case 1 -> ROOM + 1;
			case 2 -> HALLWAY;
			default -> SPECIAL;
		};
	}

	/**
	 * 0: illegal
	 * 1: room only
	 * 2: hallway only
	 * 3: both
	 */
	public static int getCellFlags(int cell) {
		if (isBossRoom(cell)) return 0;
		if (isQuadRoom(cell)) return 0;
		return switch (getTemplateType(cell)) {
			case 1 -> 1;
			case 6, 7, 8, 9 -> 2;
			case 2, 3, 4, 5 -> 3;
			default -> 0;
		};
	}

	/**
	 * 0: illegal
	 * 1: end
	 * 2: straight
	 * 3: corner
	 * 4: t way
	 * 5: cross
	 * 6: lower stairs
	 * 7: upper stairs
	 * 8: lower cross stairs
	 * 9: upper cross stairs
	 */
	public static int getTemplateType(int cell) {
		int open = getOpenings(cell);
		return switch (open) {
			case 1, 2, 4, 8 -> 1;
			case 3, 12 -> 2;
			case 5, 6, 9, 10 -> 3;
			case 7, 11, 13, 14 -> 4;
			case 15 -> 5;
			case 17, 18, 20, 24 -> 6;
			case 33, 34, 36, 40 -> 7;
			case 19, 28 -> 8;
			case 35, 44 -> 9;
			default -> 0;
		};
	}

	public static final CellInstance SKIP = new CellInstance("skip");
	public static final CellInstance MISSING = new CellInstance("missing");
	private static final CellInstance BOSS = new CellInstance("boss", 16, 16);
	private static final CellInstance END = new CellInstance("end");
	private static final CellInstance STRAIGHT = new CellInstance("straight");
	private static final CellInstance CORNER = new CellInstance("corner");
	private static final CellInstance T_WAY = new CellInstance("t_way");
	private static final CellInstance CROSS = new CellInstance("cross");
	private static final CellInstance STAIRS = new CellInstance("stairs");
	private static final CellInstance CROSS_STAIRS = new CellInstance("cross_stairs");
	private static final CellInstance QUAD = new CellInstance("quad", 16, 0);

	public static boolean isHallway(int cell) {
		return getRoomType(cell) == 1;
	}

	public static String getRoomName(int cell) {
		return switch (getRoomType(cell)) {
			case 1 -> "path/";
			case 2 -> "room/";
			default -> "";
		};
	}

	public static CellInstance getTemplateBase(int cell) {
		if (isBossRoom(cell)) {
			int room = getBossRoom(cell);
			return room == 4 ? BOSS : SKIP;
		} else if (isQuadRoom(cell)) {
			int quad = getQuadRoom(cell);
			int open = getOpenings(cell);
			return switch (open) {
				case 9, 5, 6, 10 -> SKIP;
				case 13 -> QUAD.mirror(quad == 2, Mirror.LEFT_RIGHT);
				case 7 -> QUAD.with(Rotation.CLOCKWISE_90).mirror(quad == 3, Mirror.LEFT_RIGHT);
				case 14 -> QUAD.with(Rotation.CLOCKWISE_180).mirror(quad == 1, Mirror.LEFT_RIGHT);
				case 11 -> QUAD.with(Rotation.COUNTERCLOCKWISE_90).mirror(quad == 0, Mirror.LEFT_RIGHT);
				default -> MISSING;
			};
		} else {
			int open = getOpenings(cell);
			var room = getRoomName(cell);
			var ans = switch (open) {
				case 1 -> END;
				case 2 -> END.with(Rotation.CLOCKWISE_180);
				case 3 -> STRAIGHT;
				case 4 -> END.with(Rotation.CLOCKWISE_90);
				case 5 -> CORNER.with(Rotation.CLOCKWISE_90);
				case 6 -> CORNER.with(Rotation.CLOCKWISE_180);
				case 7 -> T_WAY.with(Rotation.CLOCKWISE_90);
				case 8 -> END.with(Rotation.COUNTERCLOCKWISE_90);
				case 9 -> CORNER;
				case 10 -> CORNER.with(Rotation.COUNTERCLOCKWISE_90);
				case 11 -> T_WAY.with(Rotation.COUNTERCLOCKWISE_90);
				case 12 -> STRAIGHT.with(Rotation.CLOCKWISE_90);
				case 13 -> T_WAY;
				case 14 -> T_WAY.with(Rotation.CLOCKWISE_180);
				case 15 -> CROSS;

				case 17 -> STAIRS;
				case 18 -> STAIRS.with(Rotation.CLOCKWISE_180);
				case 19 -> CROSS_STAIRS;
				case 20 -> STAIRS.with(Rotation.CLOCKWISE_90);
				case 24 -> STAIRS.with(Rotation.COUNTERCLOCKWISE_90);
				case 28 -> CROSS_STAIRS.with(Rotation.CLOCKWISE_90);

				case 33, 34, 36, 40, 35, 44 -> SKIP;
				default -> MISSING;
			};
			if (open < 16) {
				ans = ans.room(room);
			}
			return ans;
		}
	}

	public static CellInstance getTemplate(int cell) {
		var ans = getTemplateBase(cell);
		if (ans == SKIP || ans == MISSING) return ans;
		int style = getStyle(cell);
		int variant = getVariant(cell);
		return ans.variant(style, variant);
	}

}
