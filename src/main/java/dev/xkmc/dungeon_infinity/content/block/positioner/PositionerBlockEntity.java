//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.dungeon_infinity.content.block.positioner;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class PositionerBlockEntity extends BaseBlockEntity {
	@SerialField
	private final List<BlockPos> targets = new ArrayList<>();

	public PositionerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setSummonPos(List<BlockPos> list) {
		BlockPos self = this.getBlockPos();
		Direction dir = this.getBlockState().getValue(BlockTemplates.HORIZONTAL_FACING);

		Rotation rot = switch (dir) {
			case WEST -> Rotation.CLOCKWISE_90;
			case EAST -> Rotation.COUNTERCLOCKWISE_90;
			case SOUTH -> Rotation.CLOCKWISE_180;
			default -> Rotation.NONE;
		};
		this.targets.clear();

		for (BlockPos e : list) {
			if (e.distSqr(self) <= 32 * 32) {
				BlockPos diff = e.subtract(self).rotate(rot);
				this.targets.add(diff);
			}
		}

		this.sync();
		this.setChanged();
	}

	public List<BlockPos> getTargets() {
		BlockPos self = this.getBlockPos();
		Direction dir = this.getBlockState().getValue(BlockTemplates.HORIZONTAL_FACING);
		Rotation rot = switch (dir) {
			case WEST -> Rotation.COUNTERCLOCKWISE_90;
			case EAST -> Rotation.CLOCKWISE_90;
			case SOUTH -> Rotation.CLOCKWISE_180;
			default -> Rotation.NONE;
		};
		ArrayList<BlockPos> ans = new ArrayList<>();
		for (BlockPos e : this.targets) {
			ans.add(e.rotate(rot).offset(self));
		}
		return ans;
	}

}
