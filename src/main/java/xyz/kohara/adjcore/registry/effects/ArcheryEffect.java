package xyz.kohara.adjcore.registry.effects;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class ArcheryEffect extends MobEffect {

	public ArcheryEffect() {
		super(MobEffectCategory.BENEFICIAL, 16226560);
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		this.addAttributeModifier(
				ALObjects.Attributes.ARROW_DAMAGE.get(),
				"c1e3f5b0-4d8a-4f2e-9c6b-1f2e3d4c5b6a",
				0.1D,
				AttributeModifier.Operation.ADDITION
		);
		this.addAttributeModifier(
				ALObjects.Attributes.ARROW_VELOCITY.get(),
				"c1e3f5b0-4d8a-4f2e-9c6b-1f2e3d4c5b6a",
				0.2D,
				AttributeModifier.Operation.ADDITION
		);
		super.addAttributeModifiers(entity, attributeMap, amplifier);
	}
}