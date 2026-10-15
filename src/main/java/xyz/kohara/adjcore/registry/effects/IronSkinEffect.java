package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class IronSkinEffect extends MobEffect {

	public IronSkinEffect() {
		super(MobEffectCategory.BENEFICIAL, 9211020);
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		this.addAttributeModifier(
				Attributes.ARMOR,
				"b074a3d4-dff0-44e9-b72d-f31d8488668f",
				6.0D,
				AttributeModifier.Operation.ADDITION
		);
		super.addAttributeModifiers(entity, attributeMap, amplifier);
	}
}