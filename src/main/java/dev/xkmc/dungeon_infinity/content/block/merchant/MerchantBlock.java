package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazeRoomData;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.config.ShopConfig;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class MerchantBlock implements UseItemOnBlockMethod {

	public static final BlockEntityBlockMethodImpl<MerchantBlockEntity> TE = new BlockEntityBlockMethodImpl<>(DIItems.BE_MERCHANT, MerchantBlockEntity.class);

	@Nullable
	public static WanderingTrader summonMerchant(ServerLevel level, ServerPlayer sp, BlockPos pos, String type) {
		if (!MazeHistory.inMazeDim(sp)) return null;
		var maze = MazeRoomData.get(level, SectionPos.of(pos));
		if (maze == null) return null;
		String style = TemplateConfig.get().styleName(CellInterpreter.getStyle(maze.getCell()));
		var config = ShopConfig.build(Identifier.fromNamespaceAndPath(style, type), sp.getRandom());
		if (config.isEmpty()) return null;
		WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(level, pos, EntitySpawnReason.MOB_SUMMONED);
		if (trader == null) return null;
		trader.setDespawnDelay(48000);
		trader.setWanderTarget(pos);
		trader.setHomeTo(pos, 8);
		MerchantOffers offers = new MerchantOffers();
		for (var e : config) {
			offers.add(new MerchantOffer(new ItemCost(e.cost(), e.count()), e.result().create(), e.limit(), 0, 0));
		}
		trader.offers = offers;
		trader.setHomeTo(pos, 1);
		trader.setCustomName(Component.translatable(DungeonInfinity.MODID + ".merchant." + type));
		trader.setCustomNameVisible(true);
		trader.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
		return trader;
	}

	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof MerchantBlockEntity be) {
			if (level.isClientSide())
				if (!TypeSelWheelHandler.enableWheel(player, be))
					return InteractionResult.FAIL;
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
