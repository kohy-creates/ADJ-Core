package xyz.kohara.adjcore.mixins.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.kohara.adjcore.registry.ADJEffects;
import xyz.kohara.adjcore.registry.effects.PotionSicknessEffect;

@Mixin(PotionItem.class)
public class PotionItemMixin {

	@Inject(
			method = "use",
			at = @At("HEAD"),
			cancellable = true
	)
	private void cancelPotionUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		ItemStack item = player.getItemInHand(hand);
		if (
				player.hasEffect(ADJEffects.POTION_SICKNESS.get())
						&& PotionSicknessEffect.hasHealingEffect(item)
		) {
			cir.setReturnValue(InteractionResultHolder.fail(item));
		}

	}
}
