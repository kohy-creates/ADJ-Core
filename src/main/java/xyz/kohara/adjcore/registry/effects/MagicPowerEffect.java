package xyz.kohara.adjcore.registry.effects;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class MagicPowerEffect extends MobEffect {

	public MagicPowerEffect() {
		super(MobEffectCategory.BENEFICIAL, 37111);
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		this.addAttributeModifier(
				PerkAttributes.SPELL_DAMAGE_BONUS.get(),
				"b423d950-69fa-4a9e-adc7-c976108ff0e0",
				15.0D,
				AttributeModifier.Operation.ADDITION
		);
		super.addAttributeModifiers(entity, attributeMap, amplifier);
	}
}