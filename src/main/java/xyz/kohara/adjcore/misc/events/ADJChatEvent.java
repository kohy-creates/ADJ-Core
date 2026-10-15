package xyz.kohara.adjcore.misc.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;
import xyz.kohara.adjcore.compat.kubejs.ServerEvents;
import xyz.kohara.adjcore.compat.kubejs.serverevents.ADJChatEventJS;
import xyz.kohara.adjcore.compat.kubejs.serverevents.ADJExplosionDamageCalcEventJS;

public class ADJChatEvent extends Event {

	private Component message;
	private final ServerPlayer player;
	private final MinecraftServer server;

	public ADJChatEvent(Component message, ServerPlayer player, MinecraftServer server) {
		this.message = message;
		this.player = player;
		this.server = server;

		if (ServerEvents.CHAT.hasListeners()) {
			ServerEvents.CHAT.post(new ADJChatEventJS(this));
		}
	}

	public MinecraftServer getServer() {
		return server;
	}

	public ServerPlayer getPlayer() {
		return player;
	}

	public Component getMessage() {
		return message;
	}

	public void setMessage(Component message) {
		this.message = message;
	}
}
