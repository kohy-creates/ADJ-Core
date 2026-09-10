package xyz.kohara.adjcore.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.kohara.adjcore.misc.events.ADJArrowHurtEvent;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

	@WrapOperation(
			method = "onHitEntity",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
			)
	)
	private boolean makeEntitiesNotImmune(Entity entity, DamageSource source, float amount, Operation<Boolean> original) {
		var projectile = (AbstractArrow) (Object) this;
		Entity owner = projectile.getOwner();

		var event = new ADJArrowHurtEvent(owner, projectile, entity, amount);
		MinecraftForge.EVENT_BUS.post(event);

		return entity.hurt(
				(owner instanceof Player player)
						? entity.damageSources().playerAttack(player)
						: entity.damageSources().arrow(projectile, owner),
				event.getAmount()
		);
	}
}
