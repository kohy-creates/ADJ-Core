package xyz.kohara.adjcore.registry.entities;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import xyz.kohara.adjcore.registry.ADJParticles;

import java.util.ArrayList;
import java.util.List;

public class TerraSlashEntity extends Projectile {

    public static final float FADE_OUT_DURATION = 5f;

    private static final double HALF_WIDTH = 0.75;
    private static final double HALF_HEIGHT = 0.05;
    private static final double HALF_LENGTH = 1.5;

    private final List<Entity> hitEntities = new ArrayList<>();
    private int pierceCount;
    private boolean forceFadeOut;
    private final float damage;
    public int ageOnFadeOut = -1;

    private static final EntityDataAccessor<Float> DATA_X_DISPLAY =
            SynchedEntityData.defineId(TerraSlashEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_Y_DISPLAY =
            SynchedEntityData.defineId(TerraSlashEntity.class, EntityDataSerializers.FLOAT);

    public float xDisplay;
    public float yDisplay;

    public TerraSlashEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.damage = getBaseDamage();
    }

    @Override
    public void defineSynchedData() {
        this.entityData.define(DATA_X_DISPLAY, 0f);
        this.entityData.define(DATA_Y_DISPLAY, 0f);
    }

    public void setDisplayAngles(float x, float y) {
        this.xDisplay = x;
        this.yDisplay = y;
        this.entityData.set(DATA_X_DISPLAY, x);
        this.entityData.set(DATA_Y_DISPLAY, y);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            this.xDisplay = this.entityData.get(DATA_X_DISPLAY);
            this.yDisplay = this.entityData.get(DATA_Y_DISPLAY);
        }

        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() > 1e-5 && !isFadingOut()) {
            float yaw = (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG);
            float pitch = (float) (Mth.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)) * Mth.RAD_TO_DEG);

            setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
            setYRot(yaw);
            setXRot(pitch);
            this.yRotO = yaw;
            this.xRotO = pitch;
        }

        setDeltaMovement(motion.scale(0.915));

        if (!level().isClientSide()) {
            checkEntityCollisions();
        }

        if (ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity).getType() == HitResult.Type.BLOCK
                || Math.round(getDamage()) <= 1) {
            forceFadeOut = true;
        }

        if (isFadingOut() && ageOnFadeOut == -1) {
            ageOnFadeOut = tickCount;
        }

        spawnParticles();

        if (tickCount > 40 || (isFadingOut() && tickCount - ageOnFadeOut > FADE_OUT_DURATION)) {
            discard();
        }
    }

    public boolean isFadingOut() {
        return forceFadeOut || tickCount > 30;
    }

    @Override
    public boolean canHitEntity(@NotNull Entity target) {
        return target != getOwner();
    }

    private Vec3 rotate(Vec3 v) {
        double yawRad = Math.toRadians(yDisplay);
        double pitchRad = Math.toRadians(xDisplay);

        double cosYaw = Math.cos(yawRad);
        double sinYaw = Math.sin(yawRad);
        double x1 = v.x * cosYaw - v.z * sinYaw;
        double z1 = v.x * sinYaw + v.z * cosYaw;
        double y1 = v.y;

        double cosPitch = Math.cos(pitchRad);
        double sinPitch = Math.sin(pitchRad);
        double y2 = y1 * cosPitch - z1 * sinPitch;
        double z2 = y1 * sinPitch + z1 * cosPitch;

        return new Vec3(x1, y2, z2);
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel server)) return;
        int particleCount = 2;
        for (int i = 0; i < particleCount; i++) {
            Vec3 local = new Vec3(
                    (this.random.nextDouble() - 0.5) * 2 * HALF_WIDTH,
                    (this.random.nextDouble() - 0.5) * 2 * HALF_HEIGHT,
                    (this.random.nextDouble() - 0.5) * 2 * HALF_LENGTH
            );
            Vec3 pos = position().add(rotate(local));
            float yawRad = (float) Math.toRadians(yDisplay);
            float pitchRad = (float) Math.toRadians(xDisplay);
            double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
            double dy = -Math.sin(pitchRad);
            double dz = Math.cos(yawRad) * Math.cos(pitchRad);
            Vec3 velocity = new Vec3(dx, dy, dz)
                    .scale(0.5 + random.nextDouble() * 0.5);
            server.sendParticles(
                    ADJParticles.TERRA_SHINE.get(),
                    pos.x, pos.y, pos.z,
                    0,
                    velocity.x, velocity.y, velocity.z,
                    1.0
            );
        }
    }

    private void checkEntityCollisions() {
        if (!(getOwner() instanceof LivingEntity owner)) return;

        AABB box = new AABB(position(), position())
                .inflate(HALF_WIDTH, HALF_HEIGHT, HALF_LENGTH);

        for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, box, e -> e != owner)) {
            if (!target.level().isClientSide() && !this.hitEntities.contains(target)) {
                this.hitEntities.add(target);
                target.hurt(damageSources().mobProjectile(this, owner), getDamage());
                spawnHitBurst((ServerLevel) target.level(), new Vec3(target.getX(), target.getY() + 0.8d * target.getEyeHeight(), target.getZ()));
                this.pierceCount++;
            }
        }
    }

    public static void spawnHitBurst(ServerLevel level, Vec3 position) {
        level.sendParticles(
                ADJParticles.FLASHING_SPARK.get(),
                position.x, position.y, position.z,
                8,
                0, 0, 0,
                0.75
        );
    }

    private float getBaseDamage() {
        if (getOwner() instanceof LivingEntity owner) {
            var attr = owner.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attr != null) return (float) attr.getValue() * 0.75f;
        }
        return 10f;
    }

    public float getDamage() {
        return (float) (this.damage * Math.pow(0.75, this.pierceCount));
    }
}
