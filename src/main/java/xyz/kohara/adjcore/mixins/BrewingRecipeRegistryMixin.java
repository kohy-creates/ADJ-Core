package xyz.kohara.adjcore.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BrewingRecipeRegistry.class, remap = false)
public class BrewingRecipeRegistryMixin {

	@WrapOperation(
			method = "getOutput",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraftforge/common/brewing/IBrewingRecipe;getOutput(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
			),
			remap = false
	)
	private static ItemStack preserveNBTForKubeJSToDoItsThingLater(
			IBrewingRecipe instance,
			ItemStack input, ItemStack ingredient,
			Operation<ItemStack> original
	) {
		var output = original.call(instance, input, ingredient);
		if (output.isEmpty()) return output;

		var outputNbt = input.getOrCreateTag().copy();
		outputNbt.putString("Potion", output.getOrCreateTag().getString("Potion"));

		output.setTag(outputNbt);

		return output;
	}
}
