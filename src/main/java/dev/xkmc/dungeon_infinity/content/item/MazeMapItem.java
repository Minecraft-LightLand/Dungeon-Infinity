package dev.xkmc.dungeon_infinity.content.item;

import dev.xkmc.dungeon_infinity.content.buff.core.AllBuffs;
import dev.xkmc.dungeon_infinity.content.cap.MazePos;
import dev.xkmc.dungeon_infinity.content.chunkgen.CellInterpreter;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeDimHolder;
import dev.xkmc.dungeon_infinity.content.screen.BuffSelScreen;
import dev.xkmc.dungeon_infinity.content.screen.MazeMapScreen;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class MazeMapItem extends Item {

	public MazeMapItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (owner instanceof Player && level.dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			var source = level.getChunkSource();
			var random = source.randomState();
			long seed = random.getOrCreateRandomFactory(MazeChunkGenerator.ID).at(0, 0, 0).nextLong();
			stack.set(DIItems.SEED, seed);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag tooltipFlag) {
		list.accept(DILang.MAP.get());
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		var level = context.getLevel();
		var stack = context.getItemInHand();
		boolean clickAnchor = level.getBlockState(context.getClickedPos()).is(Blocks.RESPAWN_ANCHOR);
		if (level.dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			var seed = stack.get(DIItems.SEED);
			if (seed == null) return InteractionResult.FAIL;
			if (level.isClientSide()) {
				ClientHandler.openScreen(seed, clickAnchor);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			var seed = player.getItemInHand(hand).get(DIItems.SEED);
			if (seed == null) return InteractionResult.FAIL;
			if (level.isClientSide()) {
				var data = DIMeta.HISTORY.type().getOrCreate(player);
				boolean canUse = false;
				if (data.buff.buffs.getOrDefault(AllBuffs.ENDER.id, 0) > 0) {
					var pos = MazePos.map(player.blockPosition());
					var cell = MazeDimHolder.get(seed).getCell(pos);
					var defeat = data.getOrCreate(pos).isDefeated(pos);
					canUse = CellInterpreter.isHallway(cell) || defeat;
				}
				ClientHandler.openScreen(seed, canUse);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	public static class ClientHandler {

		public static void openScreen(long seed, boolean canUse) {
			Minecraft.getInstance().setScreen(new MazeMapScreen(seed, canUse));
		}

		public static void checkBuffScreen() {
			if (Minecraft.getInstance().screen != null) return;
			var player = Minecraft.getInstance().player;
			if (player == null) return;
			var data = DIMeta.HISTORY.type().getOrCreate(player).buff;
			if (data.largeBuff > 0) {
				Minecraft.getInstance().setScreen(new BuffSelScreen(true));
			} else if (data.smallBuff > 0) {
				Minecraft.getInstance().setScreen(new BuffSelScreen(false));
			}
		}

	}


}
