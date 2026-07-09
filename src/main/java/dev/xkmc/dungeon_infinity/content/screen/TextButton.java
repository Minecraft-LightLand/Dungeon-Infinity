package dev.xkmc.dungeon_infinity.content.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class TextButton {

	private boolean enabled;

	private int x, y, w, h;

	public void set(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		enabled = true;
	}

	public void disable() {
		enabled = false;
	}

	public boolean contains(double mx, double my) {
		return enabled && mx >= x && my >= y && mx <= x + w && my <= y + h;
	}

	public void update(GuiGraphicsExtractor g, boolean enable, int x, int y, Font font, Component s, int mx, int my) {
		if (enable) {
			set(x, y, font.width(s), font.lineHeight);
			g.text(font, s, x, y, contains(mx, my) ? 0xFFFFAA00 : 0xFFFFFFFF);
		} else disable();
	}
}
