package com.brainsmash.broken_world.blocks.entity.cable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ConsumerBlockEntity extends CableBlockEntity{

    public ConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
