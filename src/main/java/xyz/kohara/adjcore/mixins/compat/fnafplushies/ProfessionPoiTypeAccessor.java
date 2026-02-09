package xyz.kohara.adjcore.mixins.compat.fnafplushies;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(targets = "net.mcreator.fnafplushieremastered.init.FnafPlushieRemasteredModVillagerProfessions$ProfessionPoiType", remap = false)
public interface ProfessionPoiTypeAccessor {

    @Accessor("block")
    @Mutable
    void adjcore$setBlock(Supplier<Block> supplier);
}
