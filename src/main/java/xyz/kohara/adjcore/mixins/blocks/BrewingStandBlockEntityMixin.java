package xyz.kohara.adjcore.mixins.blocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import xyz.kohara.adjcore.registry.ADJTags;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

	@ModifyConstant(method = "serverTick", constant = @Constant(intValue = 400))
	private static int reduceBrewingTime(int constant) {
		return 100;
	}

	@ModifyExpressionValue(
			method = "isBrewable",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraftforge/common/brewing/BrewingRecipeRegistry;canBrew(Lnet/minecraft/core/NonNullList;Lnet/minecraft/world/item/ItemStack;[I)Z"
			)
	)
	private static boolean makeMoreItemsBrewable(boolean original, @Local(name = "itemstack") ItemStack itemStack) {
		return original || itemStack.is(ADJTags.Items.BREWING_INGREDIENTS);
	}
}
