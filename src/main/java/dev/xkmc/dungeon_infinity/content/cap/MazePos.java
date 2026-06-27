package dev.xkmc.dungeon_infinity.content.cap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;

public record MazePos(int x, int y, int z, int px, int pz) {

	public static MazePos map(BlockPos pos) {
		int x = Math.floorDiv(pos.getX(), 16 * 25);
		int z = Math.floorDiv(pos.getZ(), 16 * 25);
		int y = Mth.clamp(pos.getY() / 16, 0, 15);
		int px = pos.getX() - x * 16 * 25;
		int pz = pos.getZ() - z * 16 * 25;
		return new MazePos(x, y, z, px, pz);
	}

	public long key() {
		return BlockPos.asLong(x, y, z);
	}

	public Vec3i toVec3i() {
		return new Vec3i(x, y, z);
	}

	public MazePos at(int px, int pz) {
		return new MazePos(x, y, z, px, pz);
	}

	public BlockPos toPos(int py) {
		return new BlockPos(x * 400 + px, y * 16 + py, z * 400 + pz);
	}

}
