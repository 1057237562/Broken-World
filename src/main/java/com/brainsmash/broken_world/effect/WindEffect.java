package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WindEffect extends StatusEffect {
    public WindEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        int ratio = 10 * (amplifier + 1);
        entity.addVelocity(ratio * entity.getRandom().nextGaussian(), 0, ratio * entity.getRandom().nextGaussian());
        entity.damage(DamageSource.FLY_INTO_WALL, 1.5f * (amplifier + 1));
    }
}
