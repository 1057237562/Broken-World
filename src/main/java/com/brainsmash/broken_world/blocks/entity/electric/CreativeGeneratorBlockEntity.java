package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CreativeGeneratorBlockEntity extends PowerBlockEntity {

    public CreativeGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CREATIVE_GENERATOR_ENTITY_TYPE, pos, state);
        setGenerate(60);
        setMaxCapacity(10000);
        running = true;
    }


}
