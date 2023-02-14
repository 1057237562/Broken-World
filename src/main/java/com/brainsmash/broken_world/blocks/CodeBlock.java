package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class CodeBlock extends Block {

    private final int range = 3;

    public CodeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = random.nextBetween(-range, range);
        int j = random.nextBetween(-range, range);
        int k = random.nextBetween(-range, range);
        BlockPos pos1 = pos.add(i, j, k);
        if (world.getBlockState(pos1) == BlockRegister.blocks[BlockRegistry.GLITCH.ordinal()].getDefaultState()) {
            world.setBlockState(pos1, getDefaultState());
        }
    }
}
