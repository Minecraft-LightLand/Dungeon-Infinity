package dev.xkmc.dungeon_infinity.content.map;

import dev.xkmc.dungeon_infinity.content.buff.BuffSelScreen;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.init.data.DIDimensionGen;
import dev.xkmc.dungeon_infinity.init.reg.DIItems;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

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
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.dimension().identifier().equals(DIDimensionGen.LEVEL_MAZE.identifier())) {
			var seed = player.getItemInHand(hand).get(DIItems.SEED);
			if (seed == null) return InteractionResult.FAIL;
			if (level.isClientSide()) {
				ClientHandler.openScreen(seed);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	public static class ClientHandler {

		public static void openScreen(long seed) {
			Minecraft.getInstance().setScreen(new MazeMapScreen(seed));
		}

		public static void checkBuffScreen() {
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
