package dev.xkmc.dungeon_infinity.content.map;

public class MapOverlayConfig {

	public static int screenMapMode = 1;
	public static int position = 2;
	public static float scale = 1.0f;
	public static int size = 100;
	public static boolean followPlayer = true;

	public static int posX(int w) {
		int hPos = position % 3;
		return switch (hPos) {
			case 0 -> 0;
			case 1 -> w / 2;
			case 2 -> w;
			default -> 0;
		};
	}

	public static int posY(int h) {
		int vPos = position / 3;
		return switch (vPos) {
			case 0 -> 0;
			case 1 -> h / 2;
			case 2 -> h;
			default -> 0;
		};
	}
}
