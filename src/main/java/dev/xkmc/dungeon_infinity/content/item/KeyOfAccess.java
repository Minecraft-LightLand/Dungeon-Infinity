package dev.xkmc.dungeon_infinity.content.item;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class KeyOfAccess extends Item {

	public KeyOfAccess(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (MazeHistory.inMazeDim(player)) {
			if (player instanceof ServerPlayer sp)
				MazeHistory.playerReturn(sp);
			return InteractionResult.SUCCESS;
		}

		if (!(level instanceof ServerLevel sl))
			return InteractionResult.SUCCESS;
		ItemStack stack = player.getItemInHand(hand);
		var target = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, DIDimensionGen.LEVEL_MAZE.identifier()));
		if (target == null) return InteractionResult.FAIL;
		var pos = DIItems.POS.get(stack);
		if (pos == null) {
			var r = player.getRandom();
			int x = r.nextInt(-2, 3);
			int z = r.nextInt(-2, 3);
			if (target.getChunkSource().getGenerator() instanceof MazeChunkGenerator gen) {
				var dim = gen.getMaze(target.getChunkSource().randomState());
				var maze = dim.getRegion(x, 15, z);

				List<Integer> candidates = new ArrayList<>();
				for (int cx = 0; cx < 25; cx++) {
					for (int cz = 0; cz < 25; cz++) {
						int cell = maze[cx][cz];
						if (!CellInterpreter.isSpecial(cell))
							continue;
						int style = CellInterpreter.getStyle(maze[0][0]);
						int variant = TemplateConfig.of(cell).variantIndex(style, "workshop");
						if (CellInterpreter.getVariant(cell) == variant)
							candidates.add(cx * 25 + cz);
					}
				}
				int p = candidates.get(r.nextInt(candidates.size()));
				int cx = p / 25;
				int cz = p % 25;
				pos = new BlockPos(x * 400 + cx * 16 + 8, 244, z * 400 + cz * 16 + 8);
			} else pos = new BlockPos(200 + x * 400, 244, 200 + z * 400);
			DIItems.POS.set(stack, pos);
		}
		var vec = pos.getCenter();
		if (player instanceof ServerPlayer sp) {
			MazeHistory.markEntry(sp);
			performTeleport(player, target, vec.x, vec.y, vec.z);
			var mp = MazePos.map(sp.blockPosition());
			var data = DIMeta.HISTORY.type().getOrCreate(sp);
			var ent = data.getOrCreate(mp);
			if (mp.y() == 15 && ent.getVisible() == 0) {
				data.buff.largeBuff++;
				data.buff.rerollChance += 3;
				data.buff.sync(sp);
			}
		}
		player.getCooldowns().addCooldown(stack, 20);
		return InteractionResult.SUCCESS;
	}

	public static void performTeleport(LivingEntity e, ServerLevel level, double x, double y, double z) {
		if (e.teleportTo(level, x, y, z, Set.of(), e.getYRot(), e.getXRot(), true)) {
			if (!e.isFallFlying()) {
				e.setDeltaMovement(e.getDeltaMovement().multiply(1.0, 0.0, 1.0));
				e.setOnGround(true);
			}
			if (e instanceof PathfinderMob mob) {
				mob.getNavigation().stop();
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(DILang.ACCESS.get());
	}

}
