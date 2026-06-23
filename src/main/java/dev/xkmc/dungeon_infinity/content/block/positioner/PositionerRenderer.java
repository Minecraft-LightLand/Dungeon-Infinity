//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.dungeon_infinity.content.block.positioner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.modulargolems.content.client.outline.BlockOutliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class PositionerRenderer implements BlockEntityRenderer<PositionerBlockEntity, PositionerRenderState> {

	public PositionerRenderer(BlockEntityRendererProvider.Context context) {
	}

	public boolean shouldRenderOffScreen() {
		return true;
	}

	public AABB getRenderBoundingBox(PositionerBlockEntity blockEntity) {
		LocalPlayer player = Minecraft.getInstance().player;
		return player != null && player.getAbilities().instabuild ? (new AABB(blockEntity.getBlockPos())).inflate((double) 48.0F) : new AABB(blockEntity.getBlockPos());
	}

	public int getViewDistance() {
		return 48;
	}

	public boolean shouldRender(PositionerBlockEntity be, Vec3 pos) {
		LocalPlayer player = Minecraft.getInstance().player;
		return player != null && player.getAbilities().instabuild;
	}

	public PositionerRenderState createRenderState() {
		return new PositionerRenderState();
	}

	public void extractRenderState(PositionerBlockEntity be, PositionerRenderState state, float pt, Vec3 cam, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(be, state, pt, cam, breakProgress);
		state.targets = be.getTargets();
	}

	public void submit(PositionerRenderState be, PoseStack pose, SubmitNodeCollector col, CameraRenderState cam) {
		col.submitCustomGeometry(pose, RenderTypes.lines(), (p, vc) -> this.render(be, p, vc));
	}

	private void render(PositionerRenderState be, PoseStack.Pose pose, VertexConsumer vc) {
		Vector3f zero = Vec3.atLowerCornerOf(be.blockPos).toVector3f();

		for (BlockPos e : be.targets) {
			AABB aabb = new AABB( (e.getX() - 1),  e.getY(),  (e.getZ() - 1),  (e.getX() + 2),  (e.getY() + 3),  (e.getZ() + 2));
			renderBox(pose, vc, aabb, zero, -8401440);
		}
	}

	private static void renderBox(PoseStack.Pose pose, VertexConsumer vc, AABB box, Vector3f pos, int color) {
		float offset = 0.03125F;
		BlockOutliner.renderCube(pose, vc, (float) box.minX + offset, (float) box.minY + offset, (float) box.minZ + offset, (float) box.maxX - offset, (float) box.maxY - offset, (float) box.maxZ - offset, pos, color);
	}
}
