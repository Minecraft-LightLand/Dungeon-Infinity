package dev.xkmc.dungeon_infinity.compat;

import dev.xkmc.dungeon_infinity.content.block.positioner.PositionerBlockEntity;
import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.events.MazeRoomBattleStartEvent;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
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

@EventBusSubscriber(modid = DungeonInfinity.MODID)
public class DIGolemEventHandlers {


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onInventoryDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && MazeHistory.inMazeDim(player) && player.isDeadOrDying()) {
			var data = DIMeta.LOST.type().getOrCreate(player);
			var list = player.level().getEntities(player, player.getBoundingBox().inflate(48, 16, 48));
			for (var e : list) {
				if (e instanceof AbstractGolemEntity<?, ?> golem && golem.getOwnerPlayer() == player) {
					data.add(golem.toItem(player));
				}
			}
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

	@SubscribeEvent
	public static void onBattleStart(MazeRoomBattleStartEvent event) {
		var pl = event.getEntity();
		for (var e : pl.level().getEntities(pl, pl.getBoundingBox().inflate(35, 16, 35))) {
			if (e instanceof AbstractGolemEntity<?, ?> g) {
				if (g.getOwnerPlayer() == pl) {
					LivingEntity root = null;
					if (g.isPassenger()) {
						if (g.getVehicle() instanceof AbstractGolemEntity<?, ?>) {
							continue;
						} else if (g.getVehicle() instanceof LivingEntity le)
							root = le;
					} else root = g;
					if (root != null) {
						var r = pl.getRandom();
						var pos = pl.position().add(r.nextFloat() * 2 - 1, 0, r.nextFloat() * 2 - 1);
						root.teleportTo(pos.x, pos.y, pos.z);
					}
				}
			}
		}
	}

}
