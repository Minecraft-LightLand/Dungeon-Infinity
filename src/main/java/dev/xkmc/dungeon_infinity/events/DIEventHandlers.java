package dev.xkmc.dungeon_infinity.events;

import dev.xkmc.dungeon_infinity.content.cap.MazeHistory;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import dev.xkmc.dungeon_infinity.init.data.DITagGen;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
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
		if (var2 instanceof ServerPlayer player && MazeHistory.inMazeDim(player) && player.isDeadOrDying()) {
			var data = DIMeta.LOST.type().getOrCreate(player);
			for (var e : event.getDrops())
				data.add(e.getItem());
			event.getDrops().clear();
		}
	}

	public static boolean canUseOn(ItemStack stack, BlockState state) {
		if (stack.is(DITagGen.ALWAYS_KEEP)) return true;
		if (stack.is(Items.GLOWSTONE) && state.getBlock() == Blocks.RESPAWN_ANCHOR) return true;
		return false;
	}
}
