package dev.xkmc.dungeon_infinity.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.dungeon_infinity.content.item.MazeMapItem;
import dev.xkmc.dungeon_infinity.content.map.MazeMapRenderer;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

	@Shadow
	private ItemStack offHandItem;

	@Shadow
	protected abstract void renderTwoHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float xRot, float inverseArmHeight, float attackValue);

	@Shadow
	protected abstract void renderOneHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, HumanoidArm arm, float attackValue, ItemStack map);

	@Shadow
	@Final
	private static RenderType MAP_BACKGROUND;

	@Shadow
	@Final
	private static RenderType MAP_BACKGROUND_CHECKERBOARD;

	@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	public void dungeon_infinity$renderArmWithMap(AbstractClientPlayer player, float pt, float xRot, InteractionHand hand, float attack, ItemStack stack, float ah, PoseStack pose, SubmitNodeCollector col, int light, CallbackInfo ci) {
		if (player.isScoping()) return;
		boolean isMainHand = hand == InteractionHand.MAIN_HAND;
		HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
		pose.pushPose();
		if (!(stack.getItem() instanceof MazeMapItem))
			return;
		if (isMainHand && offHandItem.isEmpty()) {
			renderTwoHandedMap(pose, col, light, xRot, ah, attack);
		} else {
			renderOneHandedMap(pose, col, light, ah, arm, attack, stack);
		}
		ci.cancel();
	}

	@Inject(method = "renderMap", at = @At("HEAD"), cancellable = true)
	public void dungeon_infinity$renderMap(PoseStack pose, SubmitNodeCollector col, int light, ItemStack stack, CallbackInfo ci) {
		if (!(stack.getItem() instanceof MazeMapItem)) return;
		pose.mulPose(Axis.YP.rotationDegrees(180.0F));
		pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
		pose.scale(0.38F, 0.38F, 0.38F);
		pose.translate(-0.5F, -0.5F, 0.0F);
		pose.scale(0.0078125F, 0.0078125F, 0.0078125F);
		Long seed = null;
		var level = Minecraft.getInstance().level;
		var player = Minecraft.getInstance().player;
		if (level != null && player != null && level.dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			seed = stack.get(DIItems.SEED.get());
		}
		RenderType renderType = seed == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD;
		col.submitCustomGeometry(pose, renderType, (mat, buffer) -> {
			buffer.addVertex(mat, -7.0F, 135.0F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setLight(light);
			buffer.addVertex(mat, 135.0F, 135.0F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setLight(light);
			buffer.addVertex(mat, 135.0F, -7.0F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setLight(light);
			buffer.addVertex(mat, -7.0F, -7.0F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setLight(light);
		});
		if (seed != null) {
			MazeMapRenderer.renderMap(seed, player, pose, col, light);
		}
		ci.cancel();
	}

}
