package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import xyz.kohara.adjcore.registry.ADJAttributes;

public class CozyCampfireEffect extends MobEffect {


    public CozyCampfireEffect() {
        super(MobEffectCategory.BENEFICIAL, 13458545);
        this.addAttributeModifier(ADJAttributes.HEALTH_REGEN.get(), "69fede9c-1964-4dcc-9caa-66d8eb2b8510", 0.5d, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(ADJAttributes.HEALTH_REGEN.get(), "0961d17e-1558-48a8-9ff9-53da1cb3bdb8", 0.1d, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int pAmplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
