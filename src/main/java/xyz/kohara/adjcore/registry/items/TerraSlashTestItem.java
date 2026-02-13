package xyz.kohara.adjcore.registry.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.kohara.adjcore.registry.ADJEntities;
import xyz.kohara.adjcore.registry.entities.TerraSlashEntity;

public class TerraSlashTestItem extends Item {

    public TerraSlashTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            TerraSlashEntity slash = new TerraSlashEntity(ADJEntities.TERRA_SLASH.get(), level);
            slash.setOwner(player);

            slash.setPos(
                    player.getX() + player.getLookAngle().x,
                    player.getY() + player.getEyeHeight() - 0.35,
                    player.getZ() + player.getLookAngle().z
            );

            slash.setDisplayAngles(player.getXRot(), player.getYRot());
            slash.setDeltaMovement(player.getLookAngle().scale(3d));

            level.addFreshEntity(slash);
        }

        player.swing(hand, true);

        return InteractionResultHolder.success(stack);
    }
}