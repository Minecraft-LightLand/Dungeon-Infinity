package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerBlockEntity;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class DIEventHandlers {

	@SubscribeEvent
	public static void levelTick(LevelTickEvent.Post event) {
		if (DIMeta.ACTIVE.type().isProper(event.getLevel())) {
			var active = DIMeta.ACTIVE.type().getExisting(event.getLevel());
			if (active.isPresent()) {
				active.get().tick(event.getLevel());
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onInventoryDrop(LivingDropsEvent event) {
		LivingEntity var2 = event.getEntity();
		if (var2 instanceof ServerPlayer player && MazeHistory.inMazeDim(player)) {
			var data = DIMeta.LOST.type().getOrCreate(player);
			for (var e : event.getDrops())
				data.add(e.getItem());
			event.getDrops().clear();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getItemStack().is(GolemItems.CARD_PATH.get())) {
			BlockEntity var2 = event.getLevel().getBlockEntity(event.getPos());
			if (var2 instanceof PositionerBlockEntity be) {
				if (event.getEntity().getAbilities().instabuild) {
					if (!event.getLevel().isClientSide()) {
						PathRecordCard.Pos pos = PathRecordCard.getList(event.getItemStack());
						if (pos != null) {
							be.setSummonPos(pos.pos());
						}
					}
					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.SUCCESS);
				}
			}
		}
	}

}
