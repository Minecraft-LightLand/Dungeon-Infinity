package dev.xkmc.dungeon_infinity.content.map;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2core.util.TooltipHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

public interface MapUI {

	default void renderMap(Player player, GuiGraphicsExtractor g, long seed, MazePos pos, float x0, float y0, float rate, boolean renderPlayer) {

		var tex = MazeMapTextureManager.get().getDetail(seed, pos);
		var fog = MazeMapTextureManager.get().getFog(seed, pos);
		var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
		tex.update(visit);
		fog.update(visit);
		g.pose().pushMatrix();
		g.pose().translate(x0, y0);
		g.pose().scale(rate, rate);
		g.pose().translate(-63, -63);
		doCustomTransform(g, pos);
		g.blit(RenderPipelines.GUI_TEXTURED, tex.id, 0, 0, 0, 0, 125, 125, 128, 128);
		g.pose().pushMatrix();
		g.pose().scale(5, 5);
		if (!player.isCreative() || !TooltipHelper.hasShiftDown())
			g.blit(RenderPipelines.GUI_TEXTURED, fog.id, 0, 0, 0, 0, 25, 25, 32, 32);
		g.pose().popMatrix();
		if (renderPlayer)
			renderPlayer(player, g, pos);
		renderWaypoints(g, visit);
		g.pose().popMatrix();
	}

	default void doCustomTransform(GuiGraphicsExtractor g, MazePos pos){

	}

	default void renderPlayer(Player player, GuiGraphicsExtractor g, MazePos pos) {
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
	}

	default void renderWaypoints(GuiGraphicsExtractor g, MazeHistory.Visit visit) {
		for (int wp : visit.getAllWaypoints()) {
			int x = wp / 400 % 400;
			int z = wp % 400;
			x = x / 16 * 16 + 8;
			z = z / 16 * 16 + 8;
			int col = MazeMapColors.P;
			g.pose().pushMatrix();
			g.pose().translate(x / 16f * 5f, z / 16f * 5f);
			g.submitGuiElementRenderState(Waypoint.of(g, 1, col));
			g.pose().popMatrix();
		}
	}

	record Arrow(
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

	record Waypoint(
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
