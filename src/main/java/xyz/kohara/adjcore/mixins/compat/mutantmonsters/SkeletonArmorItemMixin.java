package xyz.kohara.adjcore.mixins.compat.mutantmonsters;

import fuzs.mutantmonsters.world.item.SkeletonArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonArmorItem.class)
public class SkeletonArmorItemMixin {

	@Inject(method = "m_6883_", at = @At("HEAD"), cancellable = true, remap = false)
	private void dont(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci) {
		ci.cancel();
	}
}
