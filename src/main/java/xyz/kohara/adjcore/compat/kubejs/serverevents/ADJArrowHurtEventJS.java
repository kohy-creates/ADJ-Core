package xyz.kohara.adjcore.compat.kubejs.serverevents;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import xyz.kohara.adjcore.misc.events.ADJArrowHurtEvent;

public class ADJArrowHurtEventJS extends EventJS {

	private final ADJArrowHurtEvent event;

	public ADJArrowHurtEventJS(ADJArrowHurtEvent event) {
		this.event = event;
	}

	public Entity getShooter() {
		return event.getShooter();
	}

	public AbstractArrow getArrow() {
		return event.getArrow();
	}

	public Entity getVictim() {
		return event.getVictim();
	}

	public float getAmount() {
		return event.getAmount();
	}

	public void setAmount(float amount) {
		event.setAmount(amount);
	}
}
