package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SinkingEffect extends StatusEffect {
    public SinkingEffect() {
        super(StatusEffectCategory.HARMFUL, 0x009B72);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.isOnGround()) {
            entity.addVelocity(0, -1, 0);
            entity.damage(DamageSource.FLY_INTO_WALL, amplifier + 1);
        }
    }
}
