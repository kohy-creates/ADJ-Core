package xyz.kohara.adjcore.mixins.compat.ars;

import com.hollingsworth.arsnouveau.common.potions.ShockedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShockedEffect.class)
public abstract class ShockedEffectMixin {

    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void removeEffect(LivingEntity entity, int amp, CallbackInfo ci) {
        entity.removeEffect((MobEffect) (Object) this);
        ci.cancel();
    }
}
