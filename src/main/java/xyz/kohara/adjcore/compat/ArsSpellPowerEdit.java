package xyz.kohara.adjcore.compat;

import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArsSpellPowerEdit {

    @SubscribeEvent
    public static void onArsSpellDamagePreEvent(SpellDamageEvent.Pre event) {
        float amount = event.damage;
        LivingEntity entity = event.caster;
        AttributeInstance spellPower = entity.getAttribute(PerkAttributes.SPELL_DAMAGE_BONUS.get());
        if (spellPower != null) {
            float power = (float) spellPower.getValue();
            amount -= power;

            amount = (amount * (100f + power)) / 100f;
        }
        event.damage = amount;

    }
}
