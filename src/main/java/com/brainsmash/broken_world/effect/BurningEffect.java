package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BurningEffect extends StatusEffect {
    public BurningEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFF8C00);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setFireTicks(4 * (amplifier + 1));
        entity.damage(DamageSource.LAVA, 1.0f * (amplifier + 1));
    }
}
