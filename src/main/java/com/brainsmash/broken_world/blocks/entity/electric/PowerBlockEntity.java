package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PowerBlockEntity extends CableBlockEntity{

    private boolean running = false;

    public PowerBlockEntity(BlockPos pos, BlockState state) {
        super(Main.POWER_ENTITY_TYPE, pos, state);
    }

    @Override
    public long getEnergy() {
        return 50;
    }
}
