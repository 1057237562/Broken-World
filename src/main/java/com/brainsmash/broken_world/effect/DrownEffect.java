package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;

public class DrownEffect extends StatusEffect {
    public DrownEffect() {
        super(StatusEffectCategory.HARMFUL, 0x0000FF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getAir() > 0) entity.setAir(0);
        entity.world.addParticle(ParticleTypes.BUBBLE,
                entity.getX() + (entity.getRandom().nextFloat() - 0.5D) * (double) entity.getWidth(),
                entity.getY() + entity.getRandom().nextFloat() * (double) entity.getHeight(),
                entity.getZ() + (entity.getRandom().nextFloat() - 0.5D) * (double) entity.getWidth(), 0.0D, 0.0D, 0.0D);
    }
}
