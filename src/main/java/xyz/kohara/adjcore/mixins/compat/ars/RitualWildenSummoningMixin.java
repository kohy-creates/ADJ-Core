package xyz.kohara.adjcore.mixins.compat.ars;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.common.ritual.RitualWildenSummoning;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RitualWildenSummoning.class)
public class RitualWildenSummoningMixin extends AbstractRitual {

    @ModifyReturnValue(method = "isBossSpawn", at = @At("RETURN"))
    private boolean isBossSpawnNew(boolean original) {
        return didConsumeItem(Items.ENCHANTED_GOLDEN_APPLE);
    }

    @Override
    protected void tick() {
    }

    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }
}
