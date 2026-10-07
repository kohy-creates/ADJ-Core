package xyz.kohara.adjcore.mixins.blocks;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

	@ModifyConstant(method = "serverTick", constant = @Constant(intValue = 400))
	private static int reduceBrewingTime(int constant) {
		return 100;
	}
}
