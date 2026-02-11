package xyz.kohara.adjcore.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import xyz.kohara.adjcore.registry.entities.CollectibleEntity;

@OnlyIn(Dist.CLIENT)
public class CollectableItemRenderer extends EntityRenderer<CollectibleEntity> {
    private final ItemRenderer itemRenderer;
    private final RandomSource random = RandomSource.create();

    public CollectableItemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    public void render(CollectibleEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        ItemStack itemstack = entity.displayItem().getDefaultInstance();
        int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
        this.random.setSeed(i);
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), null, entity.getId());
        boolean flag = bakedmodel.isGui3d();
        int j = 1;
        float f1 = this.shouldBob() ? Mth.sin((entity.age + partialTicks) / 10.0F + entity.bobOffs) * 0.1F + 0.1F : 0.0F;
        float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        matrixStack.translate(0.0F, f1 + 0.25F * f2, 0.0F);
        float f3 = entity.getSpin(partialTicks);
        matrixStack.mulPose(Axis.YP.rotation(f3));

        for (int k = 0; k < j; k++) {
            matrixStack.pushPose();

            this.itemRenderer.render(itemstack, ItemDisplayContext.GROUND, false, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
            matrixStack.popPose();
            if (!flag) {
                matrixStack.translate(0.0, 0.0, 0.09375);
            }
        }

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CollectibleEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public boolean shouldBob() {
        return true;
    }
}
