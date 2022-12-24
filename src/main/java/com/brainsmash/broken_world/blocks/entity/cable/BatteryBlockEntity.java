package com.brainsmash.broken_world.blocks.entity.cable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BatteryBlockEntity extends CableBlockEntity{
    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public int getMaxCapacity() {
        return 10000;
    }
}
