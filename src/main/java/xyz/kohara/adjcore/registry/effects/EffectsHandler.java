package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.kohara.adjcore.registry.ADJAttributes;

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
}
