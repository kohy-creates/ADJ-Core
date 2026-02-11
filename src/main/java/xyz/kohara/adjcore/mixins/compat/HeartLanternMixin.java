package xyz.kohara.adjcore.mixins.compat;

import com.rosemods.heart_crystals.common.block_entity.HeartLanternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.kohara.adjcore.registry.ADJEffects;

import static xyz.kohara.adjcore.campfire.CozyCampfire.isPassiveMob;

@Mixin(value = HeartLanternBlockEntity.class, remap = false)
public class HeartLanternMixin {

    /**
     * @author mee :3
     * @reason ADJ
     */
    @Overwrite
    public void tick(Level level, BlockPos pos, BlockState state, HeartLanternBlockEntity blockEntity) {
        double radius = 32d;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, (new AABB(pos)).inflate(32d), livingEntity -> isPassiveMob(livingEntity) && livingEntity.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= (radius * radius))) {
            MobEffectInstance heartLanternEffect = new MobEffectInstance(ADJEffects.HEART_LANTERN.get(), 16, 0, true, false, true);
            entity.addEffect(heartLanternEffect);
        }
    }
}
