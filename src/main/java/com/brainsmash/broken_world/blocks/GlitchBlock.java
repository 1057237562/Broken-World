package com.brainsmash.broken_world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

public class GlitchBlock extends Block {

    private final int range = 3;

    public GlitchBlock(Settings settings) {
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
        if (!world.getBlockState(pos1).isAir() && !world.getBlockState(pos1).isIn(
                TagKey.of(Registry.BLOCK_KEY, new Identifier("broken_world:antiglitch")))) {
            world.setBlockState(pos1, getDefaultState());
        }
    }
}
