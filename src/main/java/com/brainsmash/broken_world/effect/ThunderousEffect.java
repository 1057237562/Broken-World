package com.brainsmash.broken_world.effect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ThunderousEffect extends StatusEffect {
    public ThunderousEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFD800);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getRandom().nextFloat() < 0.01f * (amplifier + 1)) {
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.world);
            lightningEntity.refreshPositionAfterTeleport(entity.getPos());
            entity.world.spawnEntity(lightningEntity);
        }
    }
}
