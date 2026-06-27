package dev.xkmc.dungeon_infinity.content.map;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2core.util.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
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
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int mx = (int) event.x();
		int my = (int) event.y();
		int x0 = width/2, y0 = height/2;
		float rate = Math.min(x0 / 64f, y0 / 64f) / 1.5f;
		int cmx = (int) (((mx - x0) / rate + 63) / 5);
		int cmz = (int) (((my - y0) / rate + 63) / 5);
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			var pos = MazePos.map(player.blockPosition());
			var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
			for (int wp : visit.getAllWaypoints()) {
				int x = wp / 400 % 400;
				int z = wp % 400;
				if (x / 16 == cmx && z / 16 == cmz) {
					DungeonInfinity.HANDLER.toServer(new UseWaypointPacket(pos.at(x, z)));
					return true;
				}
			}
		}
		return super.mouseClicked(event, doubleClick);
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
		int cmx = (int) (((mx - x0) / rate + 63) / 5);
		int cmz = (int) (((my - y0) / rate + 63) / 5);
		g.pose().pushMatrix();
		g.pose().translate(x0, y0);
		g.pose().scale(rate, rate);
		g.pose().translate(-63, -63);
		g.blit(RenderPipelines.GUI_TEXTURED, tex.id, 0, 0, 0, 0, 125, 125, 128, 128);
		g.pose().pushMatrix();
		g.pose().scale(5, 5);
		if (!player.isCreative() || !TooltipHelper.hasShiftDown())
			g.blit(RenderPipelines.GUI_TEXTURED, fog.id, 0, 0, 0, 0, 25, 25, 32, 32);
		g.pose().popMatrix();

		g.pose().pushMatrix();
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
		g.submitGuiElementRenderState(Arrow.of(g, 1, 0xff000000));
		g.pose().popMatrix();
		g.submitGuiElementRenderState(Arrow.of(g, 1, col));
		g.pose().popMatrix();

		for (int wp : visit.getAllWaypoints()) {
			int x = wp / 400 % 400;
			int z = wp % 400;
			x = x & -0xFF | 0x80;
			z = z & -0xFF | 0x80;
			col = MazeMapColors.P;
			g.pose().pushMatrix();
			g.pose().translate(x / 16f * 5f, z / 16f * 5f);
			if (x / 16 == cmx && z / 16 == cmz) {
				g.pose().scale(2, 2);
			}
			g.submitGuiElementRenderState(Waypoint.of(g, 1, col));
			g.pose().popMatrix();
		}
		g.pose().popMatrix();

		int x1 = (int) (x0 + rate * 64);
		int y1 = (int) (y0 - rate * 64);
		var font = getFont();
		int h = font.lineHeight + 3;
		y1 -= h - 5;

		g.text(font, DILang.DEPTH.get(16 - pos.y()), x1, y1 += h, -1);
		y1 += h;
		g.text(font, DILang.BATTLE.get(), x1, y1 += h, MazeMapColors.F);
		g.text(font, DILang.QUAD.get(), x1, y1 += h, MazeMapColors.Q);
		g.text(font, DILang.BOSS.get(), x1, y1 += h, MazeMapColors.R);
		g.text(font, DILang.DOWN.get(), x1, y1 += h, MazeMapColors.G);
		g.text(font, DILang.UP.get(), x1, y1 += h, MazeMapColors.Y);
		g.text(font, DILang.WORKSHOP.get(), x1, y1 += h, MazeMapColors.K);
		g.text(font, DILang.SHOP.get(), x1, y1 += h, MazeMapColors.S);
		g.text(font, DILang.WAREHOUSE.get(), x1, y1 += h, MazeMapColors.H);
	}

	public record Arrow(
			RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose,
			int c,
			@Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds
	) implements GuiElementRenderState {

		public static Arrow of(GuiGraphicsExtractor g, int r, int col) {
			var scissorArea = g.peekScissorStack();
			ScreenRectangle bounds = new ScreenRectangle(-r, -r, r * 2, r * 2)
					.transformMaxBounds(g.pose());
			return new Arrow(RenderPipelines.GUI, TextureSetup.noTexture(),
					new Matrix3x2f(g.pose()), col, scissorArea,
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

	public record Waypoint(
			RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose,
			int c,
			@Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds
	) implements GuiElementRenderState {

		public static Waypoint of(GuiGraphicsExtractor g, int r, int col) {
			var scissorArea = g.peekScissorStack();
			ScreenRectangle bounds = new ScreenRectangle(-r, -r, r * 2, r * 2)
					.transformMaxBounds(g.pose());
			return new Waypoint(RenderPipelines.GUI, TextureSetup.noTexture(),
					new Matrix3x2f(g.pose()), col, scissorArea,
					scissorArea != null ? scissorArea.intersection(bounds) : bounds);
		}

		@Override
		public void buildVertices(VertexConsumer vc) {
			vc.addVertexWith2DPose(pose, 0, 1).setColor(c);
			vc.addVertexWith2DPose(pose, 1, 0).setColor(c);
			vc.addVertexWith2DPose(pose, 0, -1).setColor(c);
			vc.addVertexWith2DPose(pose, -1, 0).setColor(c);
		}

	}

}
