package xyz.kohara.adjcore.mixins.compat.botania;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;

@Mixin(value = TerraBladeItem.class, remap = false)
public abstract class TerraSwordItemMixin {

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private static void attackEntity(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.PASS);
    }
}
