package xyz.kohara.adjcore.mixins.compat.ars;

import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.kohara.adjcore.ADJCore;

@Mixin(value = EnchantingApparatusRecipeCategory.class, remap = false)
public class EMIMixin {

    @WrapOperation(
            method = "draw(Lcom/hollingsworth/arsnouveau/api/enchanting_apparatus/EnchantingApparatusRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"
            )
    )
    private int changeText(
            GuiGraphics instance,
            Font font,
            Component text,
            int x, int y,
            int color,
            boolean dropShadow,
            Operation<Integer> original,
            @Local(argsOnly = true) EnchantingApparatusRecipe recipe
    ) {

        int percent = (recipe.sourceCost / 100);

        text = Component.translatable("Mᴀɴᴀ ʀᴇᴏ̨ᴜɪʀᴇᴅ:" + percent + " ᴏғ ᴀ Mᴀɴᴀ Jᴀʀ");
        return original.call(instance, font, text, x, y, color, dropShadow);
    }
}
