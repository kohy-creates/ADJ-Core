package xyz.kohara.adjcore.mixins.compat.ars.perk;

import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.common.perk.FeatherPerk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.kohara.adjcore.registry.ADJAttributes;

import java.util.UUID;

@Mixin(FeatherPerk.class)
public class FeatherPerkMixin extends Perk {

	@Shadow
	@Final
	public static UUID PERK_UUID;

	public FeatherPerkMixin(ResourceLocation key) {
		super(key);
	}

	/**
	 * @author mee :3
	 * @reason ADJ
	 */
	@Overwrite
	public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack, int slotValue) {
		return attributeBuilder().put(ADJAttributes.FALL_DAMAGE_REDUCTION.get(), new AttributeModifier(PERK_UUID, "Feather", 0.15 * slotValue, AttributeModifier.Operation.ADDITION)).build();
	}
}
