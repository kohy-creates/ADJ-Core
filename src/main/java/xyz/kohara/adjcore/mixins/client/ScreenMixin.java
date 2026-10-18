package xyz.kohara.adjcore.mixins.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.kohara.adjcore.ADJData;

import java.util.Random;

@Mixin(Screen.class)
public class ScreenMixin {

	@Shadow
	@Final
	@Mutable
	public static ResourceLocation BACKGROUND_LOCATION;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void randomizeOptionsBackground(CallbackInfo ci) {
		var bg = ADJData.optionsBackgrounds;
		if (!bg.isEmpty())
			BACKGROUND_LOCATION = bg.get(new Random().nextInt(bg.size()));
	}
}
