package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import xyz.kohara.adjcore.registry.ADJAttributes;

public class HeartLanternEffect extends MobEffect {

	public HeartLanternEffect() {
		super(MobEffectCategory.BENEFICIAL, 16262179);
		this.addAttributeModifier(ADJAttributes.HEALTH_REGEN.get(), "0992a380-2aa0-40ec-8b1a-9777e564e86d", 1d, AttributeModifier.Operation.ADDITION);
	}
}
