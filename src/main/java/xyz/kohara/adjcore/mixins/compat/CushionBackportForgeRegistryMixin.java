package xyz.kohara.adjcore.mixins.compat;

import com.leclowndu93150.cushionbackport.Cushionbackport;
import com.leclowndu93150.cushionbackport.entity.Cushion;
import com.leclowndu93150.cushionbackport.forge.CBRegistryForge;
import com.leclowndu93150.cushionbackport.item.CushionItem;
import com.leclowndu93150.cushionbackport.registry.CBEntities;
import com.leclowndu93150.cushionbackport.registry.CBItems;
import com.leclowndu93150.cushionbackport.registry.CBSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = CBRegistryForge.class, remap = false)
public abstract class CushionBackportForgeRegistryMixin {

	@Shadow(remap = false)
	@Final
	public static DeferredRegister<EntityType<?>> ENTITIES;

	@Shadow(remap = false)
	@Final
	public static DeferredRegister<Item> ITEMS;

	@Shadow(remap = false)
	@Final
	public static DeferredRegister<SoundEvent> SOUNDS;

	@Shadow(remap = false)
	@Final
	public static DeferredRegister<CreativeModeTab> TABS;

	@Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
	private static void addDyeDepotCompat(IEventBus modBus, CallbackInfo ci) {
		ci.cancel();

		CBEntities.CUSHION = ENTITIES.register("cushion", () ->
				EntityType.Builder.<Cushion>of(Cushion::new, MobCategory.MISC)
						.sized(1.0F, 0.25F)
						.clientTrackingRange(10)
						.updateInterval(Integer.MAX_VALUE)
						.build("cushion"));

		for (DyeColor color : DyeColor.values()) {
			RegistryObject<CushionItem> item = ITEMS.register(color.getName() + "_cushion", () -> new CushionItem(new Item.Properties(), color));
			CBItems.CUSHIONS.put(color, item);
		}

		CBSounds.CUSHION_BREAK = adj$registerSound("entity.cushion.break");
		CBSounds.CUSHION_PLACE = adj$registerSound("entity.cushion.place");
		CBSounds.CUSHION_SIT = adj$registerSound("entity.cushion.sit");
		CBSounds.CUSHION_GET_UP = adj$registerSound("entity.cushion.get_up");

		TABS.register("cushionbackport", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
				.title(Component.translatable("itemGroup.cushionbackport"))
				.icon(() -> new ItemStack(CBItems.cushion(DyeColor.RED)))
				.displayItems((params, output) -> {
					for (DyeColor color : DyeColor.values()) {
						output.accept(CBItems.cushion(color));
					}
				})
				.build());

		ITEMS.register(modBus);
		ENTITIES.register(modBus);
		SOUNDS.register(modBus);
		TABS.register(modBus);
	}

	@Unique
	private static Supplier<SoundEvent> adj$registerSound(String path) {
		return SOUNDS.register(path.replace('.', '_'), () -> SoundEvent.createVariableRangeEvent(
				ResourceLocation.fromNamespaceAndPath(Cushionbackport.MOD_ID, path))
		);
	}
}
