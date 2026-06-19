package dev.xkmc.dungeon_infinity.content.block;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.content.cap.MazeRoomData;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.config.ShopConfig;
import dev.xkmc.dungeon_infinity.content.config.TemplateConfig;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.jspecify.annotations.Nullable;

public class MerchantBlock {

	public static final BlockEntityBlockMethodImpl<MerchantBlockEntity> TE = new BlockEntityBlockMethodImpl<>(DIItems.BE_MERCHANT, MerchantBlockEntity.class);

	@Nullable
	public static WanderingTrader summonMerchant(ServerLevel level, ServerPlayer sp, BlockPos pos) {
		if (!MazeHistory.inMazeDim(sp)) return null;
		var maze = MazeRoomData.get(level, SectionPos.of(pos));
		if (maze == null) return null;
		String style = TemplateConfig.get().styleName(CellInterpreter.getStyle(maze.getCell()));
		var config = ShopConfig.build(style, sp.getRandom());
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
		trader.overrideOffers(offers);
		return trader;
	}

}
