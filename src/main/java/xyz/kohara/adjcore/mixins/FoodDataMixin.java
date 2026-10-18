package xyz.kohara.adjcore.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.kohara.adjcore.ADJConfig;

@Mixin(FoodData.class)
public class FoodDataMixin {

	@ModifyVariable(
			method = {"addExhaustion(F)V"},
			at = @At("HEAD"),
			argsOnly = true
	)
	private float modifyExhaustionAmount(float amountToAdd) {
		return (float) (amountToAdd * ADJConfig.Exhaustion.globalMul);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void cancelTick(Player player, CallbackInfo ci) {
		ci.cancel();

		// In case some mod edits this forcibly
		var foodData = (FoodData) (Object) this;
		foodData.setFoodLevel(20);
		foodData.setSaturation(20);
	}
}
