package xyz.kohara.adjcore.mixins.compat.ars;

import com.hollingsworth.arsnouveau.common.event.EventHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EventHandler.class)
public class EventHandlerMixin {

	@ModifyExpressionValue(
			method = "entityHurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z",
					ordinal = 3
			)
	)
	private static boolean ignoreThisLogic(boolean original) {
		return false;
	}
}
