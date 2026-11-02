package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class ShadowMeltedEffect extends MobEffect {

	public ShadowMeltedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x280530);
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
	}
}
