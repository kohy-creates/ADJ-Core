package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.kohara.adjcore.ADJConfig;
import xyz.kohara.adjcore.registry.ADJAttributes;
import xyz.kohara.adjcore.registry.ADJEffects;
import xyz.kohara.adjcore.registry.ADJTags;

public class EffectsHandler {

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		var entity = event.getEntity();
		AttributeInstance instance = entity.getAttribute(ADJAttributes.HEALTH_REGEN.get());
		if (instance == null) return;
		float regenAmount = (float) (instance.getValue() / 20f);
		if (regenAmount > 0
				&& entity.getHealth() != entity.getMaxHealth()) {
			entity.heal(regenAmount);
		} else if (regenAmount < 0) {
			entity.setHealth(entity.getHealth() + regenAmount);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMobEffectApplicableEvent(MobEffectEvent.Applicable event) {
		var entity = event.getEntity();
		var effect = event.getEffectInstance().getEffect();
		if (entity.hasEffect(ADJEffects.POTION_SICKNESS.get()) && PotionSicknessEffect.isHealingEffect(effect)) {
			event.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLivingHurt(LivingHurtEvent event) {
		var source = event.getSource();
		var attacker = source.getEntity();
		var victim = event.getEntity();
		if (attacker instanceof LivingEntity le) {
			if (!source.is(ADJTags.DamageTypes.THORNS_BLACKLIST) && victim.hasEffect(ADJEffects.THORNS.get())) {
				le.hurt(attacker.damageSources().thorns(victim), (float) (event.getAmount() * ADJConfig.Combat.thornsDamageMultiplier));
			}
			if (le.hasEffect(ADJEffects.SHADOWMELTED.get())) {
				event.setAmount((float) (event.getAmount() * ADJConfig.Combat.shadowMeltedDamageMultiplier));
				le.removeEffect(ADJEffects.SHADOWMELTED.get());
			}
		}
	}
}
