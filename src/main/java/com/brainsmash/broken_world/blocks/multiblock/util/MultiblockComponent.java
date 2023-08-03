package com.brainsmash.broken_world.blocks.multiblock.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiblockComponent {
    protected World world;
    protected BlockPos pos;

    public MultiblockComponent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public abstract void tick();
    
    public abstract void writeNbt(NbtCompound nbt);

    public abstract void readNbt(NbtCompound nbt);
}
