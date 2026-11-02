package xyz.kohara.adjcore.client.misc.events;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import xyz.kohara.adjcore.compat.kubejs.ClientEvents;
import xyz.kohara.adjcore.compat.kubejs.clientevents.ItemIsLockedRenderCheckEventJS;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Cancelable
public class ItemIsLockedRenderCheckEvent extends Event {

	private static final Map<Item, Boolean> cache = new HashMap<>();

	public static void tick(Minecraft minecraft) {
		ClientLevel level = minecraft.level;
		if (level != null) {
			var gameTime = minecraft.level.getGameTime();
			if (gameTime % 200 == 0) {
				cache.clear();
			}
		}
	}

	public static boolean shouldHide(ItemStack itemStack, LocalPlayer localPlayer) {
		var item = itemStack.getItem();
		if (cache.containsKey(item)) return cache.get(item);
		var eventHook = new ItemIsLockedRenderCheckEvent(
				itemStack,
				localPlayer
		);
		if (MinecraftForge.EVENT_BUS.post(eventHook)) {
			cache.put(item, true);
			return true;
		}
		cache.put(item, false);
		return false;
	}

	public static Component getItemName(ItemStack stack, Component original) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level != null && shouldHide(stack, mc.player)) {
			return original.copy().withStyle(
					Style.EMPTY
							.withObfuscated(true)
							.withColor(ChatFormatting.GRAY)
			);
		}

		return original;
	}

	private final ItemStack stack;
	private final LocalPlayer player;

	public ItemIsLockedRenderCheckEvent(ItemStack stack, LocalPlayer player) {
		this.stack = stack;
		this.player = player;

		if (ClientEvents.IS_LOCKED_RENDER_CHECK.hasListeners()) {
			var result = ClientEvents.IS_LOCKED_RENDER_CHECK.post(new ItemIsLockedRenderCheckEventJS(this));

			if (result.interruptFalse()) {
				this.setCanceled(true);
			}
		}
	}

	public ItemStack getItemStack() {
		return this.stack;
	}

	public LocalPlayer getPlayer() {
		return this.player;
	}
}
