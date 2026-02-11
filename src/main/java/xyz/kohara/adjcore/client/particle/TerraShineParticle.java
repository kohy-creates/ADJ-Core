package xyz.kohara.adjcore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class TerraShineParticle extends TextureSheetParticle {
    public static TerraShineParticleFactory provider(SpriteSet spriteSet) {
        return new TerraShineParticleFactory(spriteSet);
    }

    public static class TerraShineParticleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public TerraShineParticleFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed,
                                       double zSpeed) {
            return new TerraShineParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    private float angularVelocity;
    private final float angularAcceleration;

    protected TerraShineParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 0.8f;
        this.lifetime = 30;
        this.gravity = 0f;
        this.hasPhysics = false;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.angularVelocity = 0.1f;
        this.angularAcceleration = 0.01f;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
        if (!this.removed) this.setSprite(this.spriteSet.get((this.age / 3) % 10 + 1, 10));
    }
}
