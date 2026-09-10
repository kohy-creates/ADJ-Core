package xyz.kohara.adjcore.misc.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.eventbus.api.Event;
import xyz.kohara.adjcore.compat.kubejs.ServerEvents;
import xyz.kohara.adjcore.compat.kubejs.serverevents.ADJArrowHurtEventJS;

public class ADJArrowHurtEvent extends Event {

	private final Entity shooter;
	private final AbstractArrow arrow;
	private final Entity victim;

	private float amount;

	public ADJArrowHurtEvent(Entity shooter, AbstractArrow arrow, Entity victim, float amount) {
		this.shooter = shooter;
		this.arrow = arrow;
		this.victim = victim;
		this.amount = amount;

		if (ServerEvents.ADJ_ARROW_HURT.hasListeners())
			ServerEvents.ADJ_ARROW_HURT.post(new ADJArrowHurtEventJS(this));
	}

	public Entity getShooter() {
		return shooter;
	}

	public AbstractArrow getArrow() {
		return arrow;
	}

	public Entity getVictim() {
		return victim;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
