package dev.xkmc.dungeon_infinity.content.block.wall;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.generators.RegistrateBlockModelGenerator;
import com.tterrag.registrate.providers.generators.RegistrateItemModelGenerator;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.one.PathFindBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import static net.minecraft.client.data.models.BlockModelGenerators.ROTATION_FACING;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class ForceFieldBlock  implements PlacementBlockMethod, ShapeBlockMethod, PathFindBlockMethod {

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
		return state.setValue(BlockTemplates.FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public @Nullable VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		var entity = ctx instanceof EntityCollisionContext ecc ? ecc.getEntity() : null;
		if (entity instanceof LivingEntity e) {
			if (e instanceof Player player) {
				var mp = MazePos.map(player.blockPosition());
				if (DIMeta.HISTORY.type().getOrCreate(player).getOrCreate(mp).isDefeated(mp)) {
					return Shapes.empty();
				}
			}
			var cen = e.position().subtract(pos.getCenter()).multiply(1, 0, 1);
			var dir = state.getValue(BlockStateProperties.FACING).getUnitVec3();
			return cen.dot(dir) > 0 ? Shapes.block() : Shapes.empty();
		}
		return Shapes.block();
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return Shapes.block();
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return true;
	}

	public static class Model {

		public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockModelGenerator pvd) {
			MultiVariant skinless = plainVariant(ModelTemplates.SINGLE_FACE.create(ctx.get(),
					TextureMapping.defaultTexture(ctx.get()), pvd.modelOutput));
			pvd.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get(), skinless)
					.with(ROTATION_FACING));
		}

		public static void buildItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelGenerator pvd) {
			pvd.itemModelOutput.accept(ctx.get(), ItemModelUtils.plainModel(TexturedModel.CUBE.createWithSuffix(ctx.get().getBlock(), "_inventory", pvd.modelOutput)));
		}
	}


}
