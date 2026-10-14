package xyz.kohara.adjcore.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.kohara.adjcore.registry.ADJTags;

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

	@Inject(
			method = "canBrew",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void makeMoreItemsBrewableAndPreventOverwriting(NonNullList<ItemStack> inputs, ItemStack ingredient, int[] inputIndexes, CallbackInfoReturnable<Boolean> cir) {
		if (inputs.isEmpty()) cir.setReturnValue(false);

		for (var i : inputs) {
			if (!PotionUtils.getMobEffects(i).isEmpty()) cir.setReturnValue(false);
		}

		if (!ingredient.isEmpty())
			cir.setReturnValue(ingredient.is(ADJTags.Items.BREWING_INGREDIENTS));
	}

	@Inject(
			method = "isValidIngredient",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void makeMoreItemsBrewable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!stack.isEmpty())
			cir.setReturnValue(stack.is(ADJTags.Items.BREWING_INGREDIENTS));
	}
}
