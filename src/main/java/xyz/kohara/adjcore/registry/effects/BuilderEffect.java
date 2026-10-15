package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class BuilderEffect extends MobEffect {

	public BuilderEffect() {
		super(MobEffectCategory.BENEFICIAL, 11361536); // #AD5D00
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		this.addAttributeModifier(
				ForgeMod.BLOCK_REACH.get(),
				"c1e3f5b0-4d8a-4f2e-9c6b-1f2e3d4c5b6a",
				1.5D,
				AttributeModifier.Operation.ADDITION
		);
		super.addAttributeModifiers(entity, attributeMap, amplifier);
	}
}