package xyz.kohara.adjcore.potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.mixins.effect.MobEffectInstanceAccessor;

import java.util.Map;

public class PotionsEditor {

	public static void edit() {
		editDurations(PotionConfigReader.loadPotionConfig());
	}

	private static void forPotion(String potionId, PotionConfigReader.PotionConfig config) {
		var effectId = ResourceLocation.parse(config.effectID);
		MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);

		if (effect == null) {
			ADJCore.LOGGER.log(Level.ERROR, "Effect with id " + config.effectID + " not found!");
			return;
		}

		ForgeRegistries.POTIONS.getValue(ResourceLocation.parse(potionId))
				.getEffects().forEach(inst -> {
					if (inst.getEffect().equals(effect)) {
                        var eff = ((MobEffectInstanceAccessor) inst);
                        eff.setDuration((config.minutes * 60 + config.seconds) * 20);
                        eff.setAmplifier(config.level - 1);
					}
				});
	}

	public static void editDurations(Map<String, PotionConfigReader.PotionConfig> potionConfigs) {
		potionConfigs.forEach(PotionsEditor::forPotion);
	}
}