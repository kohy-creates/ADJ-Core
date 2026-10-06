//package xyz.kohara.adjcore.client.renderer.entity;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import xyz.kohara.adjcore.registry.entities.ChakramEntity;
//
//public class ChakramRenderer<T extends ChakramEntity> extends EntityRenderer<T> {
//
//
//	public ChakramRenderer(EntityRendererProvider.Context context) {
//		super(context);
//		this.model = new ModelTerribleChakram<>(context.bakeLayer(AquamiraeModelLayers.TERRIBLE_CHAKRAM));
//	}
//
//	@Override
//	public void render(
//			T entity, float entityYaw, float partialTicks,
//			PoseStack pose, MultiBufferSource buffer, int packedLight
//	) {
//		pose.pushPose();
//		float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
//		float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
//		pose.mulPose(Axis.YP.rotationDegrees(yaw + 180.0F));
//		pose.mulPose(Axis.XP.rotationDegrees(pitch));
//		pose.scale(-1.0F, -1.0F, 1.0F);
//		pose.translate(0.0F, -1.501F, 0.0F);
//
//		var ageInTicks = entity.tickCount + partialTicks;
//		this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, yaw, pitch);
//
//		var rendertype = this.model.renderType(getTextureLocation(entity));
//		var consumer = buffer.getBuffer(rendertype);
//		this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
//		pose.popPose();
//		super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
//	}
//
//	@Override
//	public ResourceLocation getTextureLocation(T entity) {
//		return TEXTURE;
//	}
//}