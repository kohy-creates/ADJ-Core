package xyz.kohara.adjcore.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
	@ModifyReturnValue(method = "canStartSprinting", at = @At("RETURN"))
	private boolean canStartSprinting(boolean original) {
		var player = (LocalPlayer) (Object) this;
		return original && !player.hasEffect(MobEffects.HUNGER);
	}
}
