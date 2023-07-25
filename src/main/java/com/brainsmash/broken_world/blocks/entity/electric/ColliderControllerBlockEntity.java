package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ColliderControllerBlockEntity extends ConsumerBlockEntity {

    protected List<ColliderCoilBlockEntity> coilBlockEntityList = null;
    public ColliderControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_CONTROLLER_ENTITY_TYPE, pos, state);
        setMaxCapacity(200);
        maxProgression = 40;
        powerConsumption = 4;
    }


}
