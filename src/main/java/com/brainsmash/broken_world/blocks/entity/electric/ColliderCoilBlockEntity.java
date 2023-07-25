package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ColliderCoilBlockEntity extends ConsumerBlockEntity {

    public ColliderCoilBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_COIL_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        powerConsumption = 100;
    }

}
