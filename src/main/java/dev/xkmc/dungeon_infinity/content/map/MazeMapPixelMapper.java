package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;

public class MazeMapPixelMapper {

	private static final Int2ObjectMap<int[][]> CACHE = new Int2ObjectOpenHashMap<>();

	public static void clear() {
		CACHE.clear();
	}

	private static void fillBossRoom(int[][] ans, int x, int z, int col, int b) {
		for (int i = 0; i < 5; i++)
			Arrays.fill(ans[i], col);
		if (x == 0 || x == 2) {
			int j = x * 2;
			if (z != 1) {
				for (int i = 0; i < 5; i++)
					ans[j][i] = b;
			} else {
				ans[j][0] = ans[j][4] = b;
			}
		}
		if (z == 0 || z == 2) {
			int j = z * 2;
			if (x != 1) {
				for (int i = 0; i < 5; i++)
					ans[i][j] = b;
			} else {
				ans[0][j] = ans[4][j] = b;
			}
		}
	}

	private static void fillQuadRoom(int[][] ans, int open, int col, int b) {
		for (int i = 0; i < 5; i++)
			Arrays.fill(ans[i], col);
		if ((open & 1) == 0) for (int i = 0; i <= 4; i++) ans[0][i] = b;
		if ((open & 2) == 0) for (int i = 0; i <= 4; i++) ans[4][i] = b;
		if ((open & 4) == 0) for (int i = 0; i <= 4; i++) ans[i][0] = b;
		if ((open & 8) == 0) for (int i = 0; i <= 4; i++) ans[i][4] = b;
	}

	private static void fillWalled(int[][] ans, int open, int c, int w) {
		for (int i = 0; i < 5; i++)
			Arrays.fill(ans[i], w);
		if ((open & 1) != 0) for (int i = 0; i <= 2; i++) ans[i][2] = c;
		if ((open & 2) != 0) for (int i = 2; i <= 4; i++) ans[i][2] = c;
		if ((open & 4) != 0) for (int i = 0; i <= 2; i++) ans[2][i] = c;
		if ((open & 8) != 0) for (int i = 2; i <= 4; i++) ans[2][i] = c;
	}

	public static int[][] getPixels(int cell, boolean defeated) {
		int flag = cell & 0x1FFF;
		defeated |= CellInterpreter.isHallway(cell);
		if (!defeated) flag |= 0x2000;
		if (CACHE.containsKey(flag))
			return CACHE.get(flag);

		int b = 0xff000000; // black, cell border color
		int w = 0xff5f5f5f; // path wall color
		int a = 0xffd6d6d6; // walkable room color
		int g = 0xff3042ec; // downward stairs
		int y = 0xff30a7ec; // upward stairs
		int f = 0xfff5342b; // mob room color
		int q = 0xffce3cff; // quad color
		int r = 0xff5704c6; // boss color
		int k = 0xff4edf4a; // workshop color
		int s = 0xff4edf4a; // shop color
		int h = 0xfff3b829; // warehouse color


		int[][] ans = new int[5][5];

		if (CellInterpreter.isBossRoom(flag)) {
			int boss = CellInterpreter.getBossRoom(flag);
			int col = defeated ? boss >= 9 ? g : y : r;
			int c = boss % 9;
			int x = c / 3;
			int z = c % 3;
			fillBossRoom(ans, x, z, col, b);
		} else if (CellInterpreter.isQuadRoom(flag)) {
			int open = CellInterpreter.getOpenings(flag);
			int col = defeated ? a : q;
			fillQuadRoom(ans, open, col, b);
		} else {
			int open = CellInterpreter.getOpenings(flag);
			int c = defeated ? (open & 16) != 0 ? y : (open & 32) != 0 ? g : a : f;
			if (CellInterpreter.isHallway(flag) && CellInterpreter.getTemplateType(flag) == 1) {
				int variant = CellInterpreter.getVariant(cell);
				int style = CellInterpreter.getStyle(cell);
				int warehouse = TemplateConfig.of(flag).variantIndex(style, "warehouse");
				int workshop = TemplateConfig.of(flag).variantIndex(style, "workshop");
				int shop = TemplateConfig.of(flag).variantIndex(style, "shop");
				c = variant == warehouse ? h : variant == workshop ? k : variant == shop ? s : c;
				fillWalled(ans, open, a, c);
			} else if (CellInterpreter.isHallway(flag)) {
				fillWalled(ans, open, c, w);
			} else if (!defeated && (open & 32) != 0) {
				fillWalled(ans, open, g, f);
			} else {
				for (int i = 0; i < 5; i++)
					Arrays.fill(ans[i], c);
			}
			ans[0][0] = ans[0][4] = ans[4][0] = ans[4][4] = b;
			if ((open & 1) == 0) for (int i = 1; i <= 3; i++) ans[0][i] = b;
			if ((open & 2) == 0) for (int i = 1; i <= 3; i++) ans[4][i] = b;
			if ((open & 4) == 0) for (int i = 1; i <= 3; i++) ans[i][0] = b;
			if ((open & 8) == 0) for (int i = 1; i <= 3; i++) ans[i][4] = b;
		}
		CACHE.put(flag, ans);
		return ans;
	}

}
