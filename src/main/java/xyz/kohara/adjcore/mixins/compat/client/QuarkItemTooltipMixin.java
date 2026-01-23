package xyz.kohara.adjcore.mixins.compat.client;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.client.resources.AttributeSlot;
import org.violetmoon.quark.content.client.tooltip.AttributeTooltips;
import xyz.kohara.adjcore.ADJData;

@Mixin(value = AttributeTooltips.class, remap = false)
public class QuarkItemTooltipMixin {

    @Inject(
            method = "getModifiers",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void sortAttributes(
            ItemStack stack,
            AttributeSlot slot,
            CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir
    ) {
        Multimap<Attribute, AttributeModifier> original = cir.getReturnValue();

        if (original.isEmpty())
            return;

        Multimap<Attribute, AttributeModifier> sorted =
                MultimapBuilder
                        .treeKeys(ADJData.attributeComparator())
                        .arrayListValues()
                        .build();

        sorted.putAll(original);

        cir.setReturnValue(sorted);
    }

    @ModifyReturnValue(
            method = "format",
            at = @At(
                    value = "RETURN",
                    ordinal = 3
            )
    )
    private static MutableComponent formatAttributeNumber(
            MutableComponent original,
            @Local(name = "attribute") Attribute attribute,
            @Local(name = "value") double value
    ) {
        if (attribute != Attributes.ATTACK_SPEED) {
            return original;
        }

        String speed = adj$getSpeedTier(value);
        return Component.literal(speed).withStyle(ChatFormatting.WHITE);
    }

    @Unique
    private static String adj$getSpeedTier(double attackSpeed) {
        if (attackSpeed >= 4.0) return "Insanely fast";
        if (attackSpeed >= 3.0) return "Very fast";
        if (attackSpeed >= 2.0) return "Fast";
        if (attackSpeed >= 1.5) return "Average";
        if (attackSpeed >= 1.2) return "Slow";
        if (attackSpeed >= 0.8) return "Very slow";
        if (attackSpeed >= 0.5) return "Extremely slow";
        return "Snail speed";
    }

    // This does nothing for some reason
//    @ModifyConstant(
//            method = "extractAttributeValues",
//            constant = @Constant(stringValue = "[+]")
//    )
//    private static String makeTextFancier(String constant, @Local(name = "slotAttributes") Multimap<Attribute, AttributeModifier> slotAttributes) {
//        System.out.println("makeText");
//        return "[+" + slotAttributes.size() + " more...]";
//    }
}
