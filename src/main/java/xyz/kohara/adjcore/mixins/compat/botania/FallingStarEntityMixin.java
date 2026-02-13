package xyz.kohara.adjcore.mixins.compat.botania;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.botania.common.entity.FallingStarEntity;

@Mixin(value = FallingStarEntity.class, remap = false)
public class FallingStarEntityMixin {

    @WrapOperation(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", ordinal = 0)
    )
    private boolean overwriteDamageType(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (instance instanceof FallingStarEntity fallingStar) {
            source = fallingStar.level().damageSources().mobProjectile(fallingStar, (LivingEntity) fallingStar.getOwner());
        }
        return original.call(instance, source, amount);
    }
}
