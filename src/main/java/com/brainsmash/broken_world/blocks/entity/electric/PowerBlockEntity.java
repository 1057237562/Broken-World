package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerBlockEntity extends CableBlockEntity{

    private boolean running = false;

    public PowerBlockEntity(BlockPos pos, BlockState state) {
        super(Main.POWER_ENTITY_TYPE, pos, state);
    }

    @Override
    public int getMaxFlow() {
        return 32;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        increaseEnergy(50);
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public int getMaxCapacity(){
        return 10000;
    }
}
