package xyz.kohara.adjcore.mixins.compat.mutantmonsters;

import fuzs.mutantmonsters.handler.PlayerEventsHandler;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEventsHandler.class, remap = false)
public class PlayerEventsHandlerMixin {

	@Inject(method = "onItemUseTick", at = @At("HEAD"), cancellable = true, remap = false)
	private static void onItemUseTick(LivingEntity entity, ItemStack useItem, MutableInt useItemRemaining, CallbackInfoReturnable<EventResult> cir) {
		cir.setReturnValue(EventResult.PASS);
	}

	@Inject(method = "onArrowLoose", at = @At("HEAD"), cancellable = true, remap = false)
	private static void onArrowLoose(Player player, ItemStack stack, Level level, MutableInt charge, boolean hasAmmo, CallbackInfoReturnable<EventResult> cir) {
		cir.setReturnValue(EventResult.PASS);
	}
}
