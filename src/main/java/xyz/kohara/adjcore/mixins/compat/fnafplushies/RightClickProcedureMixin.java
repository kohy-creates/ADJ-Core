package xyz.kohara.adjcore.mixins.compat.fnafplushies;

import net.mcreator.fnafplushieremastered.procedures.RightClickProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = RightClickProcedure.class, remap = false)
public class RightClickProcedureMixin {

    /**
     * @author me
     * @reason this played twice
     */
    @Overwrite
    public static void execute(LevelAccessor world, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState blockState = world.getBlockState(pos);
        var var11 = blockState.getBlock().getStateDefinition().getProperty("animation");
        if (var11 instanceof IntegerProperty _integerProp) {
            if (_integerProp.getPossibleValues().contains(1)) {
                world.setBlock(pos, blockState.setValue(_integerProp, 1), 3);
            }
        }

        if (world instanceof Level level) {
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("fnaf_plushie_remastered:honk"))), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

    }
}
