package xyz.kohara.adjcore.mixins.compat.client;

import com.leclowndu93150.cushionbackport.client.CushionRenderer;
import com.leclowndu93150.cushionbackport.entity.Cushion;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = CushionRenderer.class, remap = false)
public class CushionBackportCushionRendererMixin {

	@Unique
	private static final Map<DyeColor, ResourceLocation> adj$TEXTURES_BY_COLOR = Util.make(new HashMap<>(), (textures) -> {
		for (DyeColor color : DyeColor.values()) {
			textures.put(color, ResourceLocation.fromNamespaceAndPath("cushionbackport", "textures/entity/cushion/" + color.getName() + "_cushion.png"));
		}
	});

	@Inject(method = "getTextureLocation(Lcom/leclowndu93150/cushionbackport/entity/Cushion;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
	private void addDyeDepotCompat1(Cushion cushion, CallbackInfoReturnable<ResourceLocation> cir) {
		cir.setReturnValue(
				adj$TEXTURES_BY_COLOR.getOrDefault(
						cushion.getColor(),
						adj$TEXTURES_BY_COLOR.get(DyeColor.BLACK)
				)
		);
	}

	@Inject(method = "texture", at = @At("HEAD"), cancellable = true)
	private static void addDyeDepotCompat2(DyeColor color, CallbackInfoReturnable<ResourceLocation> cir) {
		cir.setReturnValue(
				adj$TEXTURES_BY_COLOR.getOrDefault(
						color,
						adj$TEXTURES_BY_COLOR.get(DyeColor.BLACK)
				)
		);
	}
}
