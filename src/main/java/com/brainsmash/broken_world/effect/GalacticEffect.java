package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class GalacticEffect extends StatusEffect {
    public GalacticEffect() {
        super(StatusEffectCategory.HARMFUL, 0xC9F6FF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.upwardSpeed = 0.1f * amplifier;
    }
}
