package xyz.kohara.adjcore.client.renderer.entity;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.mojang.blaze3d.vertex.PoseStack;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.registry.ADJItems;
import xyz.kohara.adjcore.registry.entities.TerraSlashEntity;

public class TerraSlashRenderer extends EntityRenderer<TerraSlashEntity> {

    public TerraSlashRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            TerraSlashEntity entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        poseStack.pushPose();

        float fadeProgress = 0f;
        if (entity.isFadingOut() && entity.tickCount >= entity.ageOnFadeOut) {
            fadeProgress = (entity.tickCount + partialTicks - entity.ageOnFadeOut) / TerraSlashEntity.FADE_OUT_DURATION;
            fadeProgress = Math.min(fadeProgress, 1.0f);
        }

        float spawnAlpha = (entity.tickCount == 0 ? partialTicks : 1.0f);
        spawnAlpha *= spawnAlpha;

        float alpha = spawnAlpha * (1.0f - fadeProgress);
        alpha *= alpha;

        poseStack.translate(0f, -0.05f, 0f);

        float baseScale = 7.0f;
        float scale = baseScale * (1.0f - fadeProgress * 0.75f) * alpha;
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.yDisplay));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.xDisplay));

        ItemStack slashItem = new ItemStack(ADJItems.TERRA_SLASH.get());
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (entity.tickCount != 0) {
            itemRenderer.renderStatic(
                    slashItem,
                    ItemDisplayContext.HEAD,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    0
            );
        }

        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TerraSlashEntity entity) {
        return ADJCore.of("textures/entity/terra_slash.png");
    }
}
