package xyz.kohara.adjcore.mixins.death;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.registry.ADJTags;

@Mixin(TamableAnimal.class)
public class DeathMessageTameableAnimal {

	@Redirect(
			method = "die",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/damagesource/CombatTracker;getDeathMessage()Lnet/minecraft/network/chat/Component;"
			)
	)
	private Component changeDeathMessageColor(CombatTracker instance) {
		return ADJCore.formatDeathMessage(instance.getDeathMessage(), true);
	}

	@WrapWithCondition(
			method = "die",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"
			)
	)
	private boolean cancelDeathMessageForIgnoredMobs(LivingEntity instance, Component component) {
		var tamedAnimal = (TamableAnimal) (Object) this;
		return !tamedAnimal.getType().is(ADJTags.EntityTypes.PETS_THAT_IGNORE_DEATH_MESSAGES);
	}
}
