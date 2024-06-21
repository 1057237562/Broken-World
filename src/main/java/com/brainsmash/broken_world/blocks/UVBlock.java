package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.blocks.entity.UVBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class UVBlock extends BlockWithEntity {

    public UVBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UVBlockEntity(pos, state);
    }
}
