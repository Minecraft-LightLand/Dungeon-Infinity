package dev.xkmc.dungeon_infinity.content.block.merchant;

import dev.xkmc.dungeon_infinity.content.config.ShopConfig;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public record TypeSelToServer(BlockPos pos, String mode) implements SerialPacketBase<TypeSelToServer> {

	public static TypeSelToServer of(MerchantBlockEntity be, String mode) {
		return new TypeSelToServer(be.getBlockPos(), mode);
	}

	@Override
	public void handle(Player player) {
		if (!player.isCreative()) return;
		if (!player.level().isLoaded(pos)) return;
		var e = player.level().getBlockEntity(pos);
		if (!(e instanceof MerchantBlockEntity be)) return;
		if (!ShopConfig.getAllTypes().containsKey(mode)) return;
		be.setType(mode);
	}

}
