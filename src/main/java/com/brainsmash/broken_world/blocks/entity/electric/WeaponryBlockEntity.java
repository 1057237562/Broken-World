package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class WeaponryBlockEntity extends ConsumerBlockEntity {
    public WeaponryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WEAPONRY_ENTITY_TYPE, pos, state);
    }
}
