package com.brainsmash.broken_world.effect;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SuffocateEffect extends StatusEffect {
    public SuffocateEffect() {
        super(StatusEffectCategory.HARMFUL, 0x7F0000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 3; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos blockPos = entity.getBlockPos().add(x, y, z);
                    if (entity.world.getBlockState(blockPos).isAir()) {
                        entity.world.setBlockState(blockPos,
                                BlockRegister.get(BlockRegistry.MAGICAL_STONE).getDefaultState());
                        entity.world.createAndScheduleBlockTick(blockPos,
                                BlockRegister.get(BlockRegistry.MAGICAL_STONE),
                                MathHelper.nextInt(entity.getRandom(), 60, 120));
                    }
                }
            }
        }
    }
}
