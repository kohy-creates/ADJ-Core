package xyz.kohara.adjcore.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import xyz.kohara.adjcore.ADJCore;
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

        float fadeProgress = 0.0F;
        if (entity.isFadingOut()) {
            if (entity.tickCount >= entity.ageOnFadeOut) {
                fadeProgress = (entity.tickCount + partialTicks - entity.ageOnFadeOut) / TerraSlashEntity.FADE_OUT_DURATION;
                fadeProgress = Math.min(fadeProgress, 1.0F);
            }
        }

        poseStack.translate(0f, 0.12f, 0f);

        float baseScale = 7.0F;
        float scale = baseScale * (1.0F - fadeProgress * 0.75f);
        poseStack.scale(scale, 1.0F, scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.yDisplay));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.xDisplay));

        float alpha = 1.0F - fadeProgress;
        alpha *= alpha;

        VertexConsumer consumer = buffer.getBuffer(
                RenderType.entityTranslucentEmissive(getTextureLocation(entity))
        );

        drawQuad(
                poseStack,
                consumer,
                1.0F,
                alpha
        );

        poseStack.popPose();
    }


    private static void drawQuad(
            PoseStack poseStack,
            VertexConsumer consumer,
            float size,
            float alpha
    ) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f mat = pose.pose();
        Matrix3f normal = pose.normal();

        float half = size / 2f;

        consumer.vertex(mat, -half, -half, 0)
                .color(255, 255, 255, (int) (alpha * 255))
                .uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normal, 0, 0, 1)
                .endVertex();

        consumer.vertex(mat, half, -half, 0)
                .color(255, 255, 255, (int) (alpha * 255))
                .uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normal, 0, 0, 1)
                .endVertex();

        consumer.vertex(mat, half, half, 0)
                .color(255, 255, 255, (int) (alpha * 255))
                .uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normal, 0, 0, 1)
                .endVertex();

        consumer.vertex(mat, -half, half, 0)
                .color(255, 255, 255, (int) (alpha * 255))
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normal, 0, 0, 1)
                .endVertex();
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TerraSlashEntity entity) {
        return ADJCore.of("textures/entity/terra_slash.png");
    }
}