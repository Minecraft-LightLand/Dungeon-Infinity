package dev.xkmc.dungeon_infinity.content.block.wall;

import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2modularblock.mult.NeighborUpdateBlockMethod;
import dev.xkmc.l2modularblock.mult.ScheduleTickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

public class MazeFillerBlock implements NeighborUpdateBlockMethod, ScheduleTickBlockMethod {

	@Override
	public void neighborChanged(Block block, BlockState blockState, Level level, BlockPos blockPos, Block block1, @Nullable Orientation orientation, boolean b) {
		if (level.isClientSide())
			return;
		level.scheduleTick(blockPos, block, 4);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		BlockState self = DIItems.MAZESTONE.getDefaultState();
		for (Direction dire : Direction.values()) {
			BlockPos next = pos.relative(dire);
			if (level.isOutsideBuildHeight(next))
				continue;
			BlockState nei = level.getBlockState(next);
			if (nei.isAir()) {
				level.setBlock(next, Blocks.STRUCTURE_VOID.defaultBlockState(), 3);
			}
			if (nei.isAir() || MazeWallBlock.isAffinitive(nei)) {
				self = self.setValue(MazeWallBlock.MAP.get(dire), true);
			}
		}
		if (self != state) {
			level.setBlock(pos, self, 3);
		}
	}

}
