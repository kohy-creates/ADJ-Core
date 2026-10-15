package xyz.kohara.adjcore.compat.kubejs.serverevents;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import xyz.kohara.adjcore.misc.events.ADJChatEvent;

public class ADJChatEventJS extends EventJS {

	private final ADJChatEvent event;

	public ADJChatEventJS(ADJChatEvent event) {
		this.event = event;
	}

	public MinecraftServer getServer() {
		return this.event.getServer();
	}

	public ServerPlayer getPlayer() {
		return this.event.getPlayer();
	}

	public Component getMessage() {
		return this.event.getMessage();
	}

	public void setMessage(Component message) {
		this.event.setMessage(message);
	}
}
