package xyz.kohara.adjcore.mixins.client;

import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrewingStandScreen.class)
public class BrewingStandContainerMixin {

	@ModifyConstant(
			method = "renderBg",
			constant = @Constant(floatValue = 400.0f)
	)
	private float reduceBrewingTime(float constant) {
		return 100f;
	}
}
