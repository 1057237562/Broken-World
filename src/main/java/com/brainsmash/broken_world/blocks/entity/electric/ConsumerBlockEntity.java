package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConsumerBlockEntity extends CableBlockEntity{

    private boolean running = false;

    public ConsumerBlockEntity(BlockPos pos, BlockState state) {
        super(Main.CONSUMER_ENTITY_TYPE, pos, state);
    }

    @Override
    public int getMaxCapacity() {
        return 200;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
    }
}
