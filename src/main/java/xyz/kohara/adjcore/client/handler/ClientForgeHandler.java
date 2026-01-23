package xyz.kohara.adjcore.client.handler;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.kohara.adjcore.ADJCore;
import xyz.kohara.adjcore.client.Keybindings;
import xyz.kohara.adjcore.client.networking.ADJMessages;
import xyz.kohara.adjcore.client.networking.packet.ChangeLoadOutC2SPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ADJCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        if (Keybindings.INSTANCE.LOADOUT_1.consumeClick()) {
            ADJMessages.sendToServer(new ChangeLoadOutC2SPacket(1));
        }
        if (Keybindings.INSTANCE.LOADOUT_2.consumeClick()) {
            ADJMessages.sendToServer(new ChangeLoadOutC2SPacket(2));
        }
        if (Keybindings.INSTANCE.LOADOUT_3.consumeClick()) {
            ADJMessages.sendToServer(new ChangeLoadOutC2SPacket(3));
        }
        if (Keybindings.INSTANCE.NEW_HIDE_GUI.consumeClick()) {
            client.options.hideGui = !client.options.hideGui;
        }
    }


}
