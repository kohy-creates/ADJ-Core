    package xyz.kohara.adjcore.mixins.compat.fnafplushies;

import net.mcreator.fnafplushieremastered.init.FnafPlushieRemasteredModVillagerProfessions;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = FnafPlushieRemasteredModVillagerProfessions.class, remap = false)
public abstract class PlushieProfessionMixin {

    @Final
    @Shadow
    private static Map<String, Object> POI_TYPES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void adjustSupplier(CallbackInfo ci) {
        Object entry = POI_TYPES.get("plushie_maker");
        if (entry == null) return;

        ((ProfessionPoiTypeAccessor) entry).adjcore$setBlock(() ->
                ForgeRegistries.BLOCKS.getValue(
                        new ResourceLocation("accents:sewing_station")
                )
        );
    }
}
