package xyz.kohara.adjcore.mixins.compat.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.shadowsoffire.attributeslib.impl.PercentBasedAttribute;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.client.ClientEventHandler;
import xyz.kohara.adjcore.ADJData;

import java.util.Map;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@Mixin(ClientEventHandler.class)
public class CurioTooltipMixin {

	@Unique
	private static boolean adj$mulBy100(Attribute attribute) {
		var rl = ForgeRegistries.ATTRIBUTES.getKey(attribute);
		return ADJData.x100Attributes.contains(rl);
	}

	@Unique
	private static int adj$getNewOperation(Attribute attribute, AttributeModifier modifier) {
		int operation = modifier.getOperation().toValue();
		if (attribute instanceof PercentBasedAttribute) operation = 1;
		return operation;
	}

	@Unique
	private static MutableComponent adj$getText(boolean isTake, Attribute attribute, AttributeModifier modifier, double value, String append) {
		return Component.translatable(
				"attribute.modifier."
						+ ((isTake) ? "take." : "plus.")
						+ adj$getNewOperation(attribute, modifier),
				ATTRIBUTE_MODIFIER_FORMAT.format(adj$mulBy100(attribute) ? value * 100d : value),
				Component.translatable(append).withStyle(isTake ? ChatFormatting.RED : ChatFormatting.BLUE)
		);
	}

	/*
	@Redirect(
			method = "onTooltip",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 2
			)
	)
	private MutableComponent modifyTooltip1(
			String key, Object[] args,
			@Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry,
			@Local(name = "attributemodifier") AttributeModifier attributemodifier,
			@Local(name = "d1") double d1,
			@Local(name = "slotAttribute") SlotAttribute slotAttribute) {
		return adj$getText(
				false,
				adj$getNewOperation(entry.getKey(), attributemodifier),
				d1,
				"curios.identifier." + slotAttribute.getIdentifier()
		);
	}

	@Redirect(
			method = "onTooltip",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 3
			)
	)
	private MutableComponent modifyTooltip2(
			String key, Object[] args,
			@Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry,
			@Local(name = "attributemodifier") AttributeModifier attributemodifier,
			@Local(name = "d1") double d1,
			@Local(name = "slotAttribute") SlotAttribute slotAttribute) {
		return adj$getText(
				true,
				adj$getNewOperation(entry.getKey(), attributemodifier),
				d1,
				"curios.identifier." + slotAttribute.getIdentifier()
		);
	}
	*/

	@Redirect(
			method = "onTooltip",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 5
			)
	)
	private MutableComponent modifyTooltip3(
			String key, Object[] args,
			@Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry,
			@Local(name = "attributemodifier") AttributeModifier attributemodifier,
			@Local(name = "d1") double d1) {
		var attr = entry.getKey();
		return adj$getText(
				false,
				attr, attributemodifier,
				d1,
				attr.getDescriptionId()
		);
	}

	@Redirect(
			method = "onTooltip",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 6
			)
	)
	private MutableComponent modifyTooltip4(
			String key, Object[] args,
			@Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry,
			@Local(name = "attributemodifier") AttributeModifier attributemodifier,
			@Local(name = "d1") double d1) {
		var attr = entry.getKey();
		return adj$getText(
				true,
				entry.getKey(), attributemodifier,
				d1,
				attr.getDescriptionId()
		);
	}
}
