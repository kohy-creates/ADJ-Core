package xyz.kohara.adjcore.registry.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.kohara.adjcore.ADJData;

public class PotionSicknessEffect extends MobEffect {

	public PotionSicknessEffect() {
		super(MobEffectCategory.HARMFUL, 8388640);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	public static boolean hasHealingEffect(ItemStack itemStack) {
		return PotionUtils.getMobEffects(itemStack).stream().anyMatch(
				(inst) -> isHealingEffect(inst.getEffect())
		);
	}

	public static boolean isHealingEffect(MobEffect effect) {
		var loc = ForgeRegistries.MOB_EFFECTS.getKey(effect);
		return ADJData.healingEffects.contains(loc);
	}
}
