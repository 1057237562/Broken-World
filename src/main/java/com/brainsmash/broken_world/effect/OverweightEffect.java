package com.brainsmash.broken_world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class OverweightEffect extends StatusEffect {
    public OverweightEffect() {
        super(StatusEffectCategory.HARMFUL, 0x000000);
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "6107DE5E-7CE8-4030-940E-514C1F160890", -0.15,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }
}
