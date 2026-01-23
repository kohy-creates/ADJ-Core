package xyz.kohara.adjcore.registry.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CollectibleEntity extends Entity {
    private static final int LIFETIME = 6000;
    private static final float MAX_FOLLOW_DIST = 6f;
    private static final int PICKUP_DELAY = 15;
    public int age;
    private int health = 5;
    private Player followingPlayer;
    public final float bobOffs;

    public CollectibleEntity(EntityType<? extends CollectibleEntity> entityType, Level level) {
        super(entityType, level);
        this.bobOffs = this.random.nextFloat() * (float) Math.PI * 2.0F;
    }

    public CollectibleEntity(EntityType<? extends CollectibleEntity> entityType, Level level, double x, double y, double z) {
        this(entityType, level);
        this.setPos(x, y, z);
        this.setYRot((float) (this.random.nextDouble() * 360.0));
        this.setDeltaMovement((this.random.nextDouble() * 0.2F - 0.1F) * 2.0, this.random.nextDouble() * 0.2 * 2.0, (this.random.nextDouble() * 0.2F - 0.1F) * 2.0);
    }


    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
        }

        if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) * 0.2F, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        if (!this.level().noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
        }

        this.scanForEntities();

        if (this.followingPlayer != null && (this.followingPlayer.isSpectator() || this.followingPlayer.isDeadOrDying())) {
            this.followingPlayer = null;
        }

        if (this.followingPlayer != null && this.age >= PICKUP_DELAY) {
            Vec3 vec3 = new Vec3(
                    this.followingPlayer.getX() - this.getX(),
                    this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0 - this.getY(),
                    this.followingPlayer.getZ() - this.getZ()
            );
            double d0 = vec3.lengthSqr();
            if (d0 < 16.0) {
                double d1 = 1.0 - Math.sqrt(d0) / 8.0;
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.3)));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround()) {
            BlockPos pos = this.getBlockPosBelowThatAffectsMyMovement();
            f = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.98, f));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
        }

        this.age++;
        if (this.age >= LIFETIME) {
            this.discard();
        }
    }

    @Override
    protected @NotNull BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    private void scanForEntities() {
        if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 16.0) {
            this.followingPlayer = this.level().getNearestPlayer(this, MAX_FOLLOW_DIST);
        }
    }

    private void setUnderwaterMovement() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x * 0.99F, Math.min(vec3.y + 5.0E-4F, 0.06F), vec3.z * 0.99F);
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level().isClientSide) {
            return true;
        } else {
            this.markHurt();
            this.health = (int) (this.health - amount);
            if (this.health <= 0) {
                this.discard();
            }

            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("Health", (short) this.health);
        compound.putShort("Age", (short) this.age);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.health = compound.getShort("Health");
        this.age = compound.getShort("Age");
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (this.age < PICKUP_DELAY) return;
        if (!this.level().isClientSide()) {
            PickupSound pickupSound = pickupSounds();
            final float pitch = this.random.nextFloat() * pickupSound.pitchVariance + pickupSound.basePitch;
            pickupSound.soundEvents.forEach(soundEvent -> this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    soundEvent,
                    this.getSoundSource(),
                    pickupSound.volume(),
                    pitch
            ));
            pickupEffects(player);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    public float getSpin(float partialTicks) {
        return (this.age + partialTicks) / 20.0F + this.bobOffs;
    }

    public record PickupSound(List<SoundEvent> soundEvents, float volume, float basePitch, float pitchVariance) {

        @Override
        public float volume() {
            return (this.volume == 0f) ? 0.2f : this.volume;
        }
    }

    // Editables
    public @NotNull Item displayItem() {
        return null;
    }

    public @NotNull PickupSound pickupSounds() {
        return null;
    }

    public void pickupEffects(Player player) {
        player.awardStat(Stats.ITEM_PICKED_UP.get(displayItem()), 1);

        player.take(this, 1);
        this.discard();
    }

}
