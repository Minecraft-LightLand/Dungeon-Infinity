package dev.xkmc.dungeon_infinity.content.map;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

public class MazeMapScreen extends Screen {

	private final long seed;

	protected MazeMapScreen(long seed) {
		super(Component.literal("Maze Map"));
		this.seed = seed;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
		super.extractRenderState(g, mx, my, pt);
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		var pos = MazePos.map(player.blockPosition());
		var tex = MazeMapTextureManager.get().getDetail(seed, pos);
		var fog = MazeMapTextureManager.get().getFog(seed, pos);
		var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
		tex.update(visit);
		fog.update(visit);
		int x0 = g.guiWidth() / 2, y0 = g.guiHeight() / 2;
		float rate = Math.min(x0 / 64f, y0 / 64f) / 1.5f;
		g.pose().pushMatrix();
		g.pose().translate(x0, y0);
		g.pose().scale(rate, rate);
		g.pose().translate(-63, -63);
		g.blit(RenderPipelines.GUI_TEXTURED, tex.id, 0, 0, 0, 0, 125, 125, 128, 128);
		g.pose().pushMatrix();
		g.pose().scale(5, 5);
		if (!player.isCreative() || !player.isShiftKeyDown())
			g.blit(RenderPipelines.GUI_TEXTURED, fog.id, 0, 0, 0, 0, 25, 25, 32, 32);
		g.pose().popMatrix();
		g.pose().translate(pos.px() / 16f * 5f, pos.pz() / 16f * 5f);
		g.pose().scale(2, 2);
		var yrot = player.getYRot();
		g.pose().rotate(yrot * Mth.DEG_TO_RAD);

		float r = (Mth.sin(((int) (System.currentTimeMillis() % 2000)) / 2000f * Math.PI * 2) + 1) / 2;
		float pulse = 1 + r * 0.2f;
		g.pose().scale(pulse, pulse);
		int col = 0xffffffff;

		g.pose().pushMatrix();
		g.pose().scale(1.5f, 1.5f);
		g.submitGuiElementRenderState(Arrow.of(g, -1, -1, 1, 1, 0xff000000));
		g.pose().popMatrix();
		g.submitGuiElementRenderState(Arrow.of(g, -1, -1, 1, 1, col));
		g.pose().popMatrix();
		int x1 = (int) (x0 + rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int h = font.lineHeight + 3;
		y1 -= h - 5;

		g.text(font, DILang.DEPTH.get(16 - pos.y()), x1, y1 += h, -1);
		y1 += h;
		g.text(font, DILang.BATTLE.get(), x1, y1 += h, MazeMapPixelMapper.F);
		g.text(font, DILang.QUAD.get(), x1, y1 += h, MazeMapPixelMapper.Q);
		g.text(font, DILang.BOSS.get(), x1, y1 += h, MazeMapPixelMapper.R);
		g.text(font, DILang.DOWN.get(), x1, y1 += h, MazeMapPixelMapper.G);
		g.text(font, DILang.UP.get(), x1, y1 += h, MazeMapPixelMapper.Y);
		g.text(font, DILang.WORKSHOP.get(), x1, y1 += h, MazeMapPixelMapper.K);
		g.text(font, DILang.SHOP.get(), x1, y1 += h, MazeMapPixelMapper.S);
		g.text(font, DILang.WAREHOUSE.get(), x1, y1 += h, MazeMapPixelMapper.H);
	}

	public record Arrow(
			RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose,
			int x0, int y0, int x1, int y1, int c,
			@Nullable ScreenRectangle scissorArea,
			@Nullable ScreenRectangle bounds
	) implements GuiElementRenderState {

		public static Arrow of(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int col) {
			var scissorArea = g.peekScissorStack();
			ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)
					.transformMaxBounds(g.pose());
			return new Arrow(RenderPipelines.GUI, TextureSetup.noTexture(),
					new Matrix3x2f(g.pose()), x0, y0, x1, y1, col, scissorArea,
					scissorArea != null ? scissorArea.intersection(bounds) : bounds);
		}

		@Override
		public void buildVertices(VertexConsumer vc) {
			vc.addVertexWith2DPose(pose, 0, 1).setColor(c);
			vc.addVertexWith2DPose(pose, 1, -1f).setColor(c);
			vc.addVertexWith2DPose(pose, 0, -0.5f).setColor(c);
			vc.addVertexWith2DPose(pose, -1, -1f).setColor(c);
		}

	}

}
