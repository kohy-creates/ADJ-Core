package xyz.kohara.adjcore.mixins.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.kohara.adjcore.ADJData;
import xyz.kohara.adjcore.registry.ADJAttributes;
import xyz.kohara.adjcore.registry.ADJEffects;
import xyz.kohara.adjcore.registry.effects.PotionSicknessEffect;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin {

	@Shadow
	public abstract void applyEffectTick(LivingEntity livingEntity, int amplifier);

	@Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
	public void adj$applyEffectTick(LivingEntity livingEntity, int amplifier, CallbackInfo ci) {
		ci.cancel();
		MobEffect effect = (MobEffect) (Object) this;
		if (effect == MobEffects.REGENERATION) {
//			if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
//				livingEntity.heal(1.0F);
//			}
		} else if (effect == MobEffects.POISON) {
			// 6 damage per tick on entities
			// and 4 on players
			float amount = (livingEntity instanceof Player) ? 4f : 6f;
			if (livingEntity.getHealth() > amount * 2) {
				livingEntity.hurt(livingEntity.damageSources().magic(), amount);
			}
		} else if (effect == MobEffects.WITHER) {
			// Wither does 3 + 0.3% of mob's health per tick
			float amount = 3F + livingEntity.getMaxHealth() * 0.003f;
			livingEntity.hurt(livingEntity.damageSources().wither(), amount);
		} else if (effect == MobEffects.HUNGER && livingEntity instanceof Player) {
			((Player) livingEntity).causeFoodExhaustion(0.005F * (amplifier + 1));
		} else if (effect == MobEffects.SATURATION && livingEntity instanceof Player) {
			if (!livingEntity.level().isClientSide()) {
				((Player) livingEntity).getFoodData().eat(amplifier + 1, 1.0F);
			}
		} else if ((effect != MobEffects.HEAL || livingEntity.isInvertedHealAndHarm()) && (effect != MobEffects.HARM || !livingEntity.isInvertedHealAndHarm())) {
			if (effect == MobEffects.HARM && !livingEntity.isInvertedHealAndHarm() || effect == MobEffects.HEAL && livingEntity.isInvertedHealAndHarm()) {
				int damage = 30 + 20 * amplifier;
				livingEntity.hurt(livingEntity.damageSources().magic(), damage);
			}
		} else {
			int heal = 50 * (amplifier + 1);
			livingEntity.adjcore$heal(Math.max(heal, 0), null, "healingPotion", true, false);
		}
	}

	@Inject(method = "applyInstantenousEffect", at = @At("HEAD"), cancellable = true)
	public void adj$applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity livingEntity, int amplifier, double health, CallbackInfo ci) {
		ci.cancel();
		MobEffect effect = (MobEffect) (Object) this;

		if ((effect != MobEffects.HEAL || livingEntity.isInvertedHealAndHarm()) && (effect != MobEffects.HARM || !livingEntity.isInvertedHealAndHarm())) {
			if ((effect == MobEffects.HARM && !livingEntity.isInvertedHealAndHarm()) || (effect == MobEffects.HEAL && livingEntity.isInvertedHealAndHarm())) {
				int damageAmount = 30 + 20 * amplifier;
				if (source == null) {
					livingEntity.hurt(livingEntity.damageSources().magic(), damageAmount);
				} else {
					livingEntity.hurt(livingEntity.damageSources().indirectMagic(source, indirectSource), damageAmount);
				}
			} else {
				this.applyEffectTick(livingEntity, amplifier);
			}
		} else {
			if (livingEntity instanceof Player player && PotionSicknessEffect.isHealingEffect(effect)) {
				if (player.hasEffect(ADJEffects.POTION_SICKNESS.get())) {
					return;
				} else {
					double cooldownReduction = 1d - player.getAttributeValue(ADJAttributes.POTION_SICKNESS_REDUCTION.get());
					player.addEffect(
							new MobEffectInstance(
									ADJEffects.POTION_SICKNESS.get(),
									(int) (20d * 60d * cooldownReduction),
									0
							)
					);
				}
			}
			int healAmount = 50 * (amplifier + 1);
			livingEntity.adjcore$heal(
					healAmount,
					(indirectSource instanceof LivingEntity le) ? le : null,
					"healingPotion",
					true, false
			);
		}
	}


	@Inject(method = "isDurationEffectTick", at = @At("HEAD"), cancellable = true)
	public void adj$isDurationEffectTick(int duration, int amplifier, CallbackInfoReturnable<Boolean> cir) {
		cir.cancel();
		MobEffect effect = (MobEffect) (Object) this;
		if (effect == MobEffects.REGENERATION) {
//			int k = (int) Math.round(10 * Math.pow(0.7, amplifier));
			cir.setReturnValue(false);
		} else if (effect == MobEffects.POISON) {
			int j = 30 >> amplifier;
			cir.setReturnValue(j <= 0 || duration % j == 0);
		} else if (effect == MobEffects.WITHER) {
			int i = 40 - (amplifier * 5);
			cir.setReturnValue(i <= 0 || duration % i == 0);
		} else {
			cir.setReturnValue(effect == MobEffects.HUNGER);
		}
	}
}
