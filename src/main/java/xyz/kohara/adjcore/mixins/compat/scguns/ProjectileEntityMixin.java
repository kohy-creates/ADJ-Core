package xyz.kohara.adjcore.mixins.compat.scguns;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.ProjectileEntity;

@Mixin(value = ProjectileEntity.class, remap = false)
public class ProjectileEntityMixin {

	@Shadow
	protected Gun.Projectile projectile;

	@ModifyReturnValue(method = "getaFloat", remap = false, at = @At("RETURN"))
	private float dontReduceDamageByCount(float original) {
		return original * this.projectile.getProjectileAmount();
	}
}
