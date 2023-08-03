package com.brainsmash.broken_world.blocks.magical.multiblock;

import com.brainsmash.broken_world.blocks.multiblock.util.MultiblockComponent;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManaGeneratorMultiblock extends MultiblockComponent {
    public ManaGeneratorMultiblock(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {
        System.out.println("tick by Mana Generator");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {

    }

    @Override
    public void readNbt(NbtCompound nbt) {

    }
}
