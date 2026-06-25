package dev.xkmc.dungeon_infinity.content.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class MazeMapRenderer {

	public static void renderMap(long seed, Player player, PoseStack pose, SubmitNodeCollector col, int light) {
		var pos = MazePos.map(player.blockPosition());
		var tex = MazeMapTextureManager.get().getDetail(seed, pos);
		var fog = MazeMapTextureManager.get().getFog(seed, pos);
		var visit = DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(pos);
		tex.update(visit);
		fog.update(visit);
		pose.pushPose();
		pose.translate(1.5f, 1.5f, 0);
		col.submitCustomGeometry(pose, RenderTypes.text(tex.id), (mat, buffer) -> {
			float m = 125 / 128f;
			buffer.addVertex(mat, 0, 125, -0.01F).setColor(-1).setUv(0, m).setLight(light);
			buffer.addVertex(mat, 125, 125, -0.01F).setColor(-1).setUv(m, m).setLight(light);
			buffer.addVertex(mat, 125, 0, -0.01F).setColor(-1).setUv(m, 0).setLight(light);
			buffer.addVertex(mat, 0, 0, -0.01F).setColor(-1).setUv(0, 0).setLight(light);
		});
		pose.pushPose();
		pose.scale(5, 5, 1);
		if (!player.isCreative() || !player.isShiftKeyDown())
			col.submitCustomGeometry(pose, RenderTypes.text(fog.id), (mat, buffer) -> {
				float m = 25 / 32f;
				buffer.addVertex(mat, 0, 25, -0.02F).setColor(-1).setUv(0, m).setLight(light);
				buffer.addVertex(mat, 25, 25, -0.02F).setColor(-1).setUv(m, m).setLight(light);
				buffer.addVertex(mat, 25, 0, -0.02F).setColor(-1).setUv(m, 0).setLight(light);
				buffer.addVertex(mat, 0, 0, -0.02F).setColor(-1).setUv(0, 0).setLight(light);
			});
		pose.popPose();
		pose.translate(pos.px() / 16f * 5f, pos.pz() / 16f * 5f, 0);
		pose.scale(2, 2, 1);
		var yrot = player.getYRot();
		pose.mulPose(Axis.ZP.rotationDegrees(yrot));
		col.submitCustomGeometry(pose, RenderTypes.text(tex.id), (mat, buffer) -> {
			float s = 127 / 128f;
			int c = 0xaf3f7fff;
			buffer.addVertex(mat, 0, 1, -0.03F).setColor(c).setUv(s, 1).setLight(light);
			buffer.addVertex(mat, 1, -1f, -0.03F).setColor(c).setUv(1, 1).setLight(light);
			buffer.addVertex(mat, 0, -0.5f, -0.03F).setColor(c).setUv(1, s).setLight(light);
			buffer.addVertex(mat, -1, -1f, -0.03F).setColor(c).setUv(s, s).setLight(light);
		});
		pose.popPose();
	}

}
