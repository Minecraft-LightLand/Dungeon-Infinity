package dev.xkmc.dungeon_infinity.content.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class TextButton {

	private boolean enabled;
	private int x, y, w, h;

	private int padX = 4, padY = 2;
	private boolean fixed = false;
	private int fixedW = 80;
	private float scale = 1f;

	private int bgNormal = 0xFF3D3D3D;
	private int bgHover = 0xFF5A5A5A;
	private int bgDisabled = 0xFF2A2A2A;
	private int border = 0xFF5A5A5A;
	private int borderHover = 0xFF7A7A7A;
	private int borderDisabled = 0xFF3D3D3D;
	private int borderBottom = 0xFF2A2A2A;
	private int borderRight = 0xFF2A2A2A;
	private int textNormal = 0xFFFFFFFF;
	private int textHover = 0xFFFFAA00;
	private int textDisabled = 0xFF808080;

	public TextButton pad(int px, int py) {
		padX = px; padY = py;
		return this;
	}

	public TextButton fixedWidth(int w) {
		fixed = true; fixedW = w;
		return this;
	}

	public TextButton autoWidth() {
		fixed = false;
		return this;
	}

	public TextButton setScale(float scale) {
		this.scale = scale;
		return this;
	}

	public TextButton bg(int n, int h, int d) {
		bgNormal = n; bgHover = h; bgDisabled = d;
		return this;
	}

	public TextButton border(int n, int h, int d) {
		border = n; borderHover = h; borderDisabled = d;
		return this;
	}

	public TextButton text(int n, int h, int d) {
		textNormal = n; textHover = h; textDisabled = d;
		return this;
	}

	public int visualWidth(Font font, Component s) {
		return fixed ? fixedW : (int) (font.width(s) * scale) + padX * 2;
	}

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

	public void update(GuiGraphicsExtractor g, boolean enable, int bx, int by, Font font, Component s, int mx, int my) {
		int tw = font.width(s);
		int scaledTw = (int) (tw * scale);
		int bw = fixed ? fixedW : scaledTw + padX * 2;
		int bh = font.lineHeight + padY * 2;
		set(bx, by, bw, bh);
		int tx = bx + (bw - scaledTw) / 2;
		int ty = by + padY + (int) ((font.lineHeight * (1 - scale)) / 2);
		if (!enable) {
			disable();
			g.fill(bx, by, bx + bw, by + bh, bgDisabled);
			g.fill(bx, by, bx + bw, by + 1, borderDisabled);
			g.fill(bx, by + bh - 1, bx + bw, by + bh, borderBottom);
			g.fill(bx, by, bx + 1, by + bh, borderDisabled);
			g.fill(bx + bw - 1, by, bx + bw, by + bh, borderRight);
			g.pose().pushMatrix();
			g.pose().translate(tx, ty);
			g.pose().scale(scale, scale);
			g.text(font, s, 0, 0, textDisabled);
			g.pose().popMatrix();
			return;
		}
		boolean hover = contains(mx, my);
		int bg = hover ? bgHover : bgNormal;
		int tb = hover ? borderHover : border;
		int lb = hover ? borderHover : border;
		int tc = hover ? textHover : textNormal;
		g.fill(bx, by, bx + bw, by + bh, bg);
		g.fill(bx, by, bx + bw, by + 1, tb);
		g.fill(bx, by + bh - 1, bx + bw, by + bh, borderBottom);
		g.fill(bx, by, bx + 1, by + bh, lb);
		g.fill(bx + bw - 1, by, bx + bw, by + bh, borderRight);
		g.pose().pushMatrix();
		g.pose().translate(tx, ty);
		g.pose().scale(scale, scale);
		g.text(font, s, 0, 0, tc);
		g.pose().popMatrix();
	}
}
