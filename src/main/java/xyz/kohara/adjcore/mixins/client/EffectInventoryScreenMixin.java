package xyz.kohara.adjcore.mixins.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.INVENTORY_LOCATION;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectInventoryScreenMixin {

    @Unique
    private static final ResourceLocation AMBIENT_TEXTURES = ResourceLocation.fromNamespaceAndPath("adjcore", "textures/gui/ambient_effects.png");

    @Inject(method = "renderBackgrounds", at = @At("HEAD"), cancellable = true)
    private void renderBackgrounds(GuiGraphics guiGraphics, int renderX, int yOffset, Iterable<MobEffectInstance> effects, boolean isSmall, CallbackInfo ci) {
        ci.cancel();
        EffectRenderingInventoryScreen<?> effectRenderingInventoryScreen = (EffectRenderingInventoryScreen<?>) (Object) this;
        int i = effectRenderingInventoryScreen.topPos;

        for (MobEffectInstance mobeffectinstance : effects) {
            if (mobeffectinstance.isAmbient()) {
                if (isSmall) {
                    guiGraphics.blit(AMBIENT_TEXTURES, renderX, i, 0, 0, 120, 32);
                } else {
                    guiGraphics.blit(AMBIENT_TEXTURES, renderX, i, 0, 32, 32, 32);
                }
            } else {
                if (isSmall) {
                    guiGraphics.blit(INVENTORY_LOCATION, renderX, i, 0, 166, 120, 32);
                } else {
                    guiGraphics.blit(INVENTORY_LOCATION, renderX, i, 0, 198, 32, 32);
                }
            }

            i += yOffset;
        }
    }
}

