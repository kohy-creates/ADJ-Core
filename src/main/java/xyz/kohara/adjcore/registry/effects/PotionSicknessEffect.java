package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.kohara.adjcore.ADJData;
import xyz.kohara.adjcore.registry.ADJEffects;

public class PotionSicknessEffect extends MobEffect {

	public PotionSicknessEffect() {
		super(MobEffectCategory.HARMFUL, 8388640);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	public static boolean hasHealingEffect(ItemStack itemStack) {
		return PotionUtils.getCustomEffects(itemStack).stream().anyMatch(
				(inst) -> isHealingEffect(inst.getEffect())
		);
	}

	public static boolean isHealingEffect(MobEffect effect) {
		var loc = ForgeRegistries.MOB_EFFECTS.getKey(effect);
		return ADJData.healingEffects.contains(loc);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMobEffectApplicableEvent(MobEffectEvent.Applicable event) {
		var entity = event.getEntity();
		var effect = event.getEffectInstance().getEffect();
		if (entity.hasEffect(ADJEffects.POTION_SICKNESS.get()) && PotionSicknessEffect.isHealingEffect(effect)) {
			event.setResult(Event.Result.DENY);
		}
	}
}
