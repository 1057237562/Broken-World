package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ConsumerBlockEntity extends CableBlockEntity{

    private boolean running = false;

    public ConsumerBlockEntity(BlockPos pos, BlockState state) {
        super(Main.CONSUMER_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

}
