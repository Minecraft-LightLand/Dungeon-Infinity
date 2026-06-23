package dev.xkmc.dungeon_infinity.content.block.wall;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.generators.RegistrateBlockModelGenerator;
import com.tterrag.registrate.providers.generators.RegistrateItemModelGenerator;
import dev.xkmc.dungeon_infinity.init.data.DITagGen;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.MirrorRotateBlockMethod;
import dev.xkmc.l2modularblock.one.SpecialDropBlockMethod;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class MazeWallBlock {

	public static final Neighbor NEIGHBOR = new Neighbor();
	public static final Drop DROP = new Drop();
	public static final AllDireState ALL_DIRE_STATE = new AllDireState();

	public static final int DELAY = 4;
	public static final BooleanProperty[] PROPS = {BlockStateProperties.DOWN, BlockStateProperties.UP,
			BlockStateProperties.NORTH, BlockStateProperties.SOUTH,
			BlockStateProperties.WEST, BlockStateProperties.EAST};
	public static final Map<Direction, BooleanProperty> MAP = Map.of(
			Direction.DOWN, BlockStateProperties.DOWN,
			Direction.UP, BlockStateProperties.UP,
			Direction.NORTH, BlockStateProperties.NORTH,
			Direction.SOUTH, BlockStateProperties.SOUTH,
			Direction.WEST, BlockStateProperties.WEST,
			Direction.EAST, BlockStateProperties.EAST);

	public static boolean isAffinitive(BlockState neighbor) {
		return neighbor.is(DITagGen.MAZE_STONE_AFFINITIVE);
	}

	public static class Neighbor implements NeighborUpdateBlockMethod {

		@Override
		public void neighborChanged(Block self, BlockState state, Level level, BlockPos pos, Block nei_block, @Nullable Orientation orientation, boolean moving) {
			if (level.isClientSide())
				return;
			level.scheduleTick(pos, self, DELAY);
		}

	}

	public static class Drop implements UseItemOnBlockMethod, SpecialDropBlockMethod {

		@Override
		public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
			if (stack.is(Items.NETHER_STAR)) {
				if (!level.isClientSide()) {
					for (Direction dire : Direction.values()) {
						BlockPos next = pos.relative(dire);
						if (level.isOutsideBuildHeight(next))
							continue;
						BlockState nei = level.getBlockState(next);
						if (nei.getBlock() == state.getBlock()) {
							level.setBlock(next, nei.setValue(MAP.get(dire.getOpposite()), false), 18);
						}
					}
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					if (!pl.getAbilities().instabuild)
						stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			if (stack.is(Items.ECHO_SHARD) && pl.getAbilities().instabuild) {
				if (level instanceof ServerLevel sl) {
					Queue<BlockPos> queue = new ArrayDeque<>();
					Set<BlockPos> visited = new LinkedHashSet<>();
					List<BlockPos> ans = new ArrayList<>();
					queue.add(pos);
					visited.add(pos);
					while (!queue.isEmpty()) {
						var e = queue.poll();
						ans.add(e);
						for (var dir : Direction.values()) {
							var rel = e.relative(dir);
							if (visited.contains(rel)) continue;
							visited.add(rel);
							if (level.getBlockState(rel).getBlock() == state.getBlock()) {
								queue.add(rel);
							}
						}
					}
					int count = 0;
					for (var e : ans) {
						if (update(level.getBlockState(e), sl, e)) {
							count++;
						}
					}
					pl.sendSystemMessage(Component.literal(count + " blocks updated"));
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}

		private boolean update(BlockState state, ServerLevel level, BlockPos pos) {
			BlockState self = state;
			for (Direction dire : Direction.values()) {
				BlockPos next = pos.relative(dire);
				if (level.isOutsideBuildHeight(next))
					continue;
				BlockState nei = level.getBlockState(next);
				if (!self.getValue(MAP.get(dire)) && nei.getBlock() == self.getBlock()) {
					self = self.setValue(MAP.get(dire), true);
				}
			}
			if (self != state) {
				level.setBlock(pos, self, 18);
				return true;
			}
			return false;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			for (Direction dire : Direction.values()) {
				if (state.getValue(MAP.get(dire))) {
					return List.of();
				}
			}
			return List.of(new ItemStack(state.getBlock()));
		}

	}

	public static class AllDireState implements
			CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
			ScheduleTickBlockMethod, MirrorRotateBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(PROPS);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			for (BooleanProperty bp : PROPS)
				state = state.setValue(bp, false);
			return state;
		}

		@Override
		public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			BlockState rep = getDefaultState(state);
			BlockState self = state;
			for (Direction dire : Direction.values()) {
				BlockPos next = pos.relative(dire);
				if (level.isOutsideBuildHeight(next))
					continue;
				BlockState nei = level.getBlockState(next);
				if (self.getValue(MAP.get(dire))) {
					if (!isAffinitive(nei)) {
						nei = getStateForPlacement(rep, level, next);
						level.setBlock(next, nei, 18);
					}
				} else if (isAffinitive(nei)) {
					self = self.setValue(MAP.get(dire), true);
				}
			}
			if (self != state) {
				level.setBlock(pos, self, 18);
			}
		}

		@Override
		public BlockState mirror(BlockState state, Mirror mirrorIn) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = mirrorIn.mirror(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState rotate(BlockState state, Rotation rot) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = rot.rotate(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
			Level level = context.getLevel();
			BlockPos pos = context.getClickedPos();
			return getStateForPlacement(def, level, pos);
		}

		private BlockState getStateForPlacement(BlockState def, Level level, BlockPos pos) {
			for (Direction dire : Direction.values()) {
				BlockState nei = level.getBlockState(pos.relative(dire));
				if (nei.getBlock() == def.getBlock()) {
					def = def.setValue(MAP.get(dire), true);
				}
			}
			return def;
		}

	}

	public static class Model {

		public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockModelGenerator pvd) {
			MultiVariant skin = plainVariant(ModelTemplates.SINGLE_FACE.create(ctx.get(),
					TextureMapping.defaultTexture(ctx.get()), pvd.modelOutput));
			MultiVariant skinless = plainVariant(ModelTemplates.SINGLE_FACE.create(pvd.modLoc("block/mazestone_in"),
					TextureMapping.defaultTexture(new Material(pvd.modLoc("block/mazestone_in"))), pvd.modelOutput));
			pvd.blockStateOutput.accept(MultiPartGenerator.multiPart(ctx.get())
					.with(condition().term(BlockStateProperties.NORTH, false), skin)
					.with(condition().term(BlockStateProperties.EAST, false), skin.with(Y_ROT_90).with(UV_LOCK))
					.with(condition().term(BlockStateProperties.SOUTH, false), skin.with(Y_ROT_180).with(UV_LOCK))
					.with(condition().term(BlockStateProperties.WEST, false), skin.with(Y_ROT_270).with(UV_LOCK))
					.with(condition().term(BlockStateProperties.UP, false), skin.with(X_ROT_270).with(UV_LOCK))
					.with(condition().term(BlockStateProperties.DOWN, false), skin.with(X_ROT_90).with(UV_LOCK))
					.with(condition().term(BlockStateProperties.NORTH, true), skinless)
					.with(condition().term(BlockStateProperties.EAST, true), skinless.with(Y_ROT_90))
					.with(condition().term(BlockStateProperties.SOUTH, true), skinless.with(Y_ROT_180))
					.with(condition().term(BlockStateProperties.WEST, true), skinless.with(Y_ROT_270))
					.with(condition().term(BlockStateProperties.UP, true), skinless.with(X_ROT_270))
					.with(condition().term(BlockStateProperties.DOWN, true), skinless.with(X_ROT_90)));
		}

		public static void buildItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelGenerator pvd) {
			pvd.itemModelOutput.accept(ctx.get(), ItemModelUtils.plainModel(TexturedModel.CUBE.createWithSuffix(ctx.get().getBlock(), "_inventory", pvd.modelOutput)));
		}
	}

}
