package dev.xkmc.dungeon_infinity.content.item;

import dev.xkmc.dungeon_infinity.init.data.DILang;
import dev.xkmc.dungeon_infinity.init.reg.DIMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class KeyOfTomb extends Item {

	public KeyOfTomb(Properties properties) {
		super(properties.stacksTo(16));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		var data = DIMeta.LOST.type().getOrCreate(player);
		if (!data.list.isEmpty()) {
			if (player instanceof ServerPlayer sp) {
				player.getCooldowns().addCooldown(player.getItemInHand(hand), 10);
				player.getItemInHand(hand).shrink(1);
				var list = data.poll(9);
				for (var e : list) {
					sp.getInventory().placeItemBackInInventory(e);
				}
				DIMeta.LOST.type().network.toClient(sp);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(DILang.TOMB.get());
		if (context.level() != null && context.level().isClientSide()) {
			ClientHandler.getDesc(builder);
		}
	}

	public static class ClientHandler {

		public static void getDesc(Consumer<Component> builder) {
			var player = Minecraft.getInstance().player;
			if (player == null) return;
			int size = DIMeta.LOST.type().getOrCreate(player).list.size();
			if (size == 0) return;
			builder.accept(DILang.TOMB_ITEM_COUNT.get(size));
		}

	}
}
