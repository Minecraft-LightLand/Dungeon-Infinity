package dev.xkmc.dungeon_infinity.init.reg;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.dungeon_infinity.content.block.wall.ForceFieldBlock;
import dev.xkmc.dungeon_infinity.content.block.wall.MazeFillerBlock;
import dev.xkmc.dungeon_infinity.content.block.wall.MazeWallBlock;
import dev.xkmc.dungeon_infinity.content.block.merchant.MerchantBlock;
import dev.xkmc.dungeon_infinity.content.block.merchant.MerchantBlockEntity;
import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerBlock;
import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerBlockEntity;
import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerRenderer;
import dev.xkmc.dungeon_infinity.content.item.KeyOfAccess;
import dev.xkmc.dungeon_infinity.content.item.KeyOfTomb;
import dev.xkmc.dungeon_infinity.content.map.MazeMapItem;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DITagGen;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class DIItems {

	public static final SimpleEntry<CreativeModeTab> TAB = DungeonInfinity.REGISTRATE.buildL2CreativeTab(
			"dungeon_infinity", "Dungeon Infinity",
			b -> b.icon(DIItems::getIcon));

	public static final BlockEntry<DelegateBlock> MAZESTONE, FILLER;

	public static final BlockEntry<ForceFieldBlock> FORCEFIELD;
	public static final BlockEntry<Block> FORCEFIELD_BLOCK, BROKEN_FORCEFIELD;

	public static final BlockEntry<DelegateBlock> MERCHANT_BLOCK, POSITIONER;
	public static final BlockEntityEntry<MerchantBlockEntity> BE_MERCHANT;
	public static final BlockEntityEntry<PositionerBlockEntity> BE_POSITION;

	public static final ItemEntry<MazeMapItem> MAP;
	public static final ItemEntry<KeyOfTomb> KEY_OF_TOMB;
	public static final ItemEntry<KeyOfAccess> KEY_OF_ACCESS;

	public static final DCReg DC = DCReg.of(DungeonInfinity.REG);
	public static final DCVal<Long> SEED = DC.longVal("seed");
	public static final DCVal<BlockPos> POS = DC.reg("pos", BlockPos.class, true);

	static {
		MAZESTONE = DungeonInfinity.REGISTRATE.block("mazestone", p ->
						DelegateBlock.newBaseBlock(p, MazeWallBlock.ALL_DIRE_STATE, MazeWallBlock.NEIGHBOR, MazeWallBlock.DROP))
				.properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
						.strength(-1.0F, 3600000))
				.blockstate(() -> MazeWallBlock.Model::buildModel)
				.item().model(() -> MazeWallBlock.Model::buildItem).build()
				.tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, DITagGen.MAZE_STONE_AFFINITIVE, DITagGen.FORCEFIELD_CANNOT_REPLACE)
				.register();

		FILLER = DungeonInfinity.REGISTRATE.block("filler", p ->
						DelegateBlock.newBaseBlock(p, new MazeFillerBlock()))
				.properties(p -> p.noCollision().noOcclusion().noLootTable())
				.defaultBlockstate().simpleItem()
				.register();

		FORCEFIELD = DungeonInfinity.REGISTRATE.block("forcefield", ForceFieldBlock::new)
				.properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
						.strength(-1.0F, 3600000).noLootTable().forceSolidOff().noOcclusion())
				.blockstate(() -> ForceFieldBlock.Model::buildModel)
				.item().model(() -> ForceFieldBlock.Model::buildItem).build()
				.tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
				.register();

		FORCEFIELD_BLOCK = DungeonInfinity.REGISTRATE.block("forcefield_block", Block::new)
				.properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
						.strength(-1.0F, 3600000).noLootTable())
				.defaultBlockstate().simpleItem()
				.tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
				.register();

		BROKEN_FORCEFIELD = DungeonInfinity.REGISTRATE.block("broken_forcefield", Block::new)
				.properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
						.strength(1, 1).noLootTable())
				.defaultBlockstate().simpleItem()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.register();

		MERCHANT_BLOCK = DungeonInfinity.REGISTRATE.block("merchant_block", p ->
						DelegateBlock.newBaseBlock(p.noLootTable(), new MerchantBlock(), MerchantBlock.TE))
				.defaultBlockstate().simpleItem()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.register();

		BE_MERCHANT = DungeonInfinity.REGISTRATE.blockEntity("merchant_block", MerchantBlockEntity::new)
				.validBlock(MERCHANT_BLOCK)
				.register();

		POSITIONER = DungeonInfinity.REGISTRATE.block("positioner", p ->
						DelegateBlock.newBaseBlock(p.noLootTable(), BlockTemplates.HORIZONTAL, PositionerBlock.TE))
				.defaultBlockstate().simpleItem()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.register();

		BE_POSITION = DungeonInfinity.REGISTRATE.blockEntity("positioner", PositionerBlockEntity::new)
				.validBlock(POSITIONER)
				.renderer(() -> PositionerRenderer::new)
				.register();

		MAP = DungeonInfinity.REGISTRATE.item("maze_map", MazeMapItem::new)
				.tag(DITagGen.ALWAYS_KEEP)
				.defaultModel().register();

		KEY_OF_TOMB = DungeonInfinity.REGISTRATE.item("key_of_tomb", KeyOfTomb::new)
				.tag(DITagGen.ALWAYS_KEEP)
				.defaultModel().register();

		KEY_OF_ACCESS = DungeonInfinity.REGISTRATE.item("key_of_access", KeyOfAccess::new)
				.tag(DITagGen.ALWAYS_KEEP)
				.defaultModel().register();

	}

	public static ItemStack getIcon() {
		return MAZESTONE.asStack();
	}

	public static void register() {

	}

}
