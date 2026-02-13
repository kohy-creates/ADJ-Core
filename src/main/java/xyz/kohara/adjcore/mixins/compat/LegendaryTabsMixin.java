package xyz.kohara.adjcore.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sfiomn.legendarytabs.LegendaryTabs;

@Mixin(value = LegendaryTabs.class, remap = false)
public class LegendaryTabsMixin {

    @ModifyExpressionValue(method = "modIntegration", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/ModList;isLoaded(Ljava/lang/String;)Z", ordinal = 0))
    private boolean removeBackpackedCompat(boolean original) {
        return false;
    }
}
