package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class FabricatorBlockEntity extends ConsumerBlockEntity {

    public FabricatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.FABRICATOR_ENTITY_TYPE,pos, state);
    }


}
