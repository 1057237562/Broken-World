package com.brainsmash.broken_world.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.Nullable;

public class CrackParticle extends SpriteBillboardParticle {
    private final float sampleU;
    private final float sampleV;

    CrackParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this(world, x, y, z);
        this.velocityX *= 0.1f;
        this.velocityY *= 0.1f;
        this.velocityZ *= 0.1f;
        this.velocityX += velocityX;
        this.velocityY += velocityY;
        this.velocityZ += velocityZ;
    }


    public CrackParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.gravityStrength = 1.0f;
        this.scale /= 2.0f;
        this.sampleU = this.random.nextFloat() * 3.0f;
        this.sampleV = this.random.nextFloat() * 3.0f;
    }


    @Override
    protected float getMinU() {
        return this.sprite.getFrameU((this.sampleU + 1.0f) / 4.0f * 16.0f);
    }

    @Override
    protected float getMaxU() {
        return this.sprite.getFrameU(this.sampleU / 4.0f * 16.0f);
    }

    @Override
    protected float getMinV() {
        return this.sprite.getFrameV(this.sampleV / 4.0f * 16.0f);
    }

    @Override
    protected float getMaxV() {
        return this.sprite.getFrameV((this.sampleV + 1.0f) / 4.0f * 16.0f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory<T extends ParticleEffect> implements ParticleFactory<T> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider provider) {
            spriteProvider = provider;
        }

        @Nullable
        @Override
        public Particle createParticle(T parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            CrackParticle particle = new CrackParticle(world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
