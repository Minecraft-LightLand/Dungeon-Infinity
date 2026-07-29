package dev.xkmc.dungeon_infinity.content.cap;

import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerBlockEntity;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeDimHolder;
import dev.xkmc.dungeon_infinity.content.chunkgen.RoomProcessorStrategy;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.content.spawn.SpawnHelper;
import dev.xkmc.dungeon_infinity.init.data.DITagGen;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SectionRoom {

	private ServerLevel sl;
	private LevelChunk lc;
	private SectionPos pos;
	MazeDimHolder dim;
	private TemplateConfig.TemplateData info;
	private List<BlockPos> positions;
	private int[][] maze;
	private int x, z;

	public void update(ServerLevel sl, LevelChunk lc, SectionPos pos) {
		if (this.sl == sl && this.lc == lc) return;
		this.sl = sl;
		this.lc = lc;
		this.pos = pos;
		if (sl.getChunkSource().getGenerator() instanceof MazeChunkGenerator gen) {
			dim = gen.getMaze(sl.getChunkSource().randomState());
		} else throw new IllegalStateException("Illegal Dimension for Section");
		int rx = Math.floorDiv(pos.x(), 25);
		int rz = Math.floorDiv(pos.z(), 25);
		maze = dim.getRegion(rx, pos.y(), rz);
		x = pos.x() - rx * 25;
		z = pos.z() - rz * 25;
		info = TemplateConfig.getEntry(maze[x][z]);
		positions = new ArrayList<>();
		for (var ent : lc.getBlockEntities().entrySet()) {
			if (ent.getKey().getY() >> 4 == pos.y() && ent.getValue() instanceof PositionerBlockEntity be) {
				positions.addAll(be.getTargets());
			}
		}
	}

	public int getCell() {
		return maze[x][z];
	}

	@SerialField
	public boolean walled = false;

	@SerialField
	public @Nullable MobRoomTicker data = null;

	@Nullable MobRoomHolder ins = null;

	public ServerLevel level() {
		return sl;
	}

	public BlockPos getBlockPos() {
		return pos.origin();
	}

	public List<BlockPos> getSpawns() {
		return positions;
	}

	public boolean isActive() {
		return walled;
	}

	public MobRoomHolder getOrCreateActiveMobRoomInstance() {
		if (ins != null) return ins;
		var room = findRoom();
		ins = new MobRoomHolder(this, room);
		return ins;
	}

	public void markUnsaved() {
		lc.markUnsaved();
	}

	public void setWall(Direction dir, boolean gen) {
		var origin = pos.origin();
		var src = origin.offset(dir.getStepX() > 0 ? 15 : 0, dir.getStepY() > 0 ? 15 : 0, dir.getStepZ() > 0 ? 15 : 0);
		var dst = src.offset(dir.getStepX() == 0 ? 15 : 0, dir.getStepY() == 0 ? 15 : 0, dir.getStepZ() == 0 ? 15 : 0);
		var mpos = new BlockPos.MutableBlockPos();
		var block = gen ? DIItems.FORCEFIELD_BLOCK.getDefaultState() : DIItems.BROKEN_FORCEFIELD.getDefaultState();
		var wall = gen ? DIItems.FORCEFIELD.getDefaultState().setValue(BlockStateProperties.FACING, dir.getOpposite()) : Blocks.AIR.defaultBlockState();
		for (int x = src.getX(); x <= dst.getX(); x++) {
			for (int y = src.getY(); y <= dst.getY(); y++) {
				for (int z = src.getZ(); z <= dst.getZ(); z++) {
					mpos.set(x, y, z);
					var old = lc.getBlockState(mpos);
					if (gen) {
						if (!old.isAir() && !old.canBeReplaced()) continue;//TODO
						if (old.is(DITagGen.FORCEFIELD_CANNOT_REPLACE)) continue;
						level().setBlockAndUpdate(mpos, old.isSolid() ? block : wall);
					} else {
						if (old.is(DIItems.FORCEFIELD_BLOCK))
							level().setBlockAndUpdate(mpos, block);
						else if (old.is(DIItems.FORCEFIELD))
							level().setBlockAndUpdate(mpos, wall);
					}
				}
			}
		}
	}

	public boolean isBoss() {
		int cell = maze[x][z];
		return CellInterpreter.isBossRoom(cell);
	}

	public boolean isQuad() {
		int cell = maze[x][z];
		return CellInterpreter.isQuadRoom(cell);
	}

	public boolean isLarge() {
		int cell = maze[x][z];
		return CellInterpreter.isBossRoom(cell) || CellInterpreter.isQuadRoom(cell);
	}

	public @Nullable SectionRoom[][][] findRoom() {
		int cell = maze[x][z];
		if (CellInterpreter.isBossRoom(cell)) {
			int data = CellInterpreter.getBossRoom(cell);
			int layer = data / 9;
			int cx = data % 9 / 3;
			int cz = data % 3;
			var ans = new SectionRoom[3][2][3];
			for (int ix = 0; ix < 3; ix++) {
				for (int iz = 0; iz < 3; iz++) {
					for (int iy = 0; iy < 2; iy++) {
						ans[ix][iy][iz] = MazeRoomData.get(sl, pos.offset(ix - cx, iy - layer, iz - cz));
					}
				}
			}
			return ans;
		}
		if (CellInterpreter.isQuadRoom(cell)) {
			int data = CellInterpreter.getQuadRoom(cell);
			int cx = data / 2;
			int cz = data % 2;
			var ans = new SectionRoom[2][1][2];
			for (int ix = 0; ix < 2; ix++) {
				for (int iz = 0; iz < 2; iz++) {
					ans[ix][0][iz] = MazeRoomData.get(sl, pos.offset(ix - cx, 0, iz - cz));

				}
			}
			return ans;
		}
		if (CellInterpreter.isHallway(cell))
			return new SectionRoom[0][0][0];
		List<int[]> rel = RoomProcessorStrategy.findRooms(maze, x, z);
		int x0 = 25, z0 = 25, x1 = 0, z1 = 0;
		for (var p : rel) {
			x0 = Math.min(x0, p[0]);
			x1 = Math.max(x1, p[0]);
			z0 = Math.min(z0, p[1]);
			z1 = Math.max(z1, p[1]);
		}
		@Nullable SectionRoom[][][] ans = new SectionRoom[x1 - x0 + 1][1][z1 - z0 + 1];
		for (var p : rel) {
			ans[p[0] - x0][0][p[1] - z0] = MazeRoomData.get(sl, pos.offset(p[0] - x, 0, p[1] - z));
		}
		return ans;
	}

	public void tick(MazeHistory.Visit visit, MazePos pos, ServerPlayer sp) {
		int cell = maze[x][z];
		if (!visit.isDefeated(pos) && (CellInterpreter.isBossRoom(cell) ||
				CellInterpreter.isQuadRoom(cell) ||
				!CellInterpreter.isHallway(cell))) {
			var origin = new Vec3(this.pos.origin());
			double r = 1.2; // TODO
			var box = new AABB(origin.add(r, r, r), origin.add(16 - r, 16 - r, 16 - r));
			if (box.contains(sp.position().add(sp.getBbHeight() / 2))) {
				var ins = getOrCreateActiveMobRoomInstance();
				ins.tick(sp);
			}
		}
		roomTick();
	}

	public MobRoomTicker createSpawner(@Nullable SectionRoom[][][] rooms, List<BlockPos> spawns) {
		var ans = new MobRoomTicker();
		ans.spawner = SpawnHelper.createTickerFromTemplate(info, rooms, spawns, level().getRandom());
		return ans;
	}

	private long lastTick = -1;

	private void roomTick() {
		if (lastTick >= level().getGameTime()) return;
		lastTick = level().getGameTime();
		if (CellInterpreter.isSpecial(getCell())) {
			if (lastTick % 20 == 0) {
				var origin = new Vec3(getBlockPos());
				var box = new AABB(origin, origin.add(16, 16, 16));
				var list = level().getEntitiesOfClass(LivingEntity.class, box);
				for (var e : list) {
					e.heal(2);
				}
			}
		}
	}

	public MazeAccess getAccess() {
		return new Access();
	}

	public class Access implements MazeAccess {

		@Override
		public int[][] getMaze() {
			return maze;
		}

		@Override
		public int getX() {
			return x;
		}

		@Override
		public int getZ() {
			return z;
		}

	}

}
