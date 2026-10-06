package xyz.kohara.adjcore.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

	@Unique
	Mob adj$this = (Mob) (Object) this;

	@WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
	private int makeAmbientSoundsPlayLessOften(RandomSource instance, int i, Operation<Integer> original) {
		return instance.nextInt(1500);
	}

	@Inject(method = "getAmbientSoundInterval", at = @At("HEAD"), cancellable = true)
	private void makeAmbientSoundsPlayLessOften2(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(85 + adj$this.random.nextInt(50));
	}
}
