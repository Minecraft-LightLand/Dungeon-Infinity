package dev.xkmc.dungeon_infinity.mixin;

import dev.xkmc.dungeon_infinity.content.shulker.InvTooltip;
import dev.xkmc.dungeon_infinity.events.DIEventHandlers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
	public void dungeonInfinity$addImage(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.is(ItemTags.SHULKER_BOXES)) {
			var cont = self.get(DataComponents.CONTAINER);
			if (cont != null && cont.getSlots() > 0) {
				cir.setReturnValue(Optional.of(new InvTooltip(cont, 9, 3)));
			}
		}
	}

	@Inject(method = "canPlaceOnBlockInAdventureMode", at = @At("HEAD"), cancellable = true)
	public void dungeonInfinity$whitelistAdventureItem(BlockInWorld ctx, CallbackInfoReturnable<Boolean> cir) {
		if (DIEventHandlers.canUseOn((ItemStack) (Object) this, ctx.getState())) {
			cir.setReturnValue(true);
		}
	}

}
