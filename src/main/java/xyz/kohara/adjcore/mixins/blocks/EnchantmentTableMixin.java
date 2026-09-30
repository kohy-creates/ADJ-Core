package xyz.kohara.adjcore.mixins.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentTableBlock.class)
public class EnchantmentTableMixin {

    @Shadow
    @Final
    @Mutable
    public static List<BlockPos> BOOKSHELF_OFFSETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceBookshelfOffsets(CallbackInfo ci) {
        BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-3, 0, -3, 3, 2, 3)
                .filter(pos -> Math.abs(pos.getX()) >= 1 || Math.abs(pos.getZ()) >= 1)
                .map(BlockPos::immutable)
                .toList();
    }
}
