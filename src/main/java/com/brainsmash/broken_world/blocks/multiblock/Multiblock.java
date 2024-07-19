package com.brainsmash.broken_world.blocks.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Multiblock extends DummyBlock {
    public Multiblock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MultiblockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient) {
            return (world1, pos, state1, blockEntity) -> ((MultiblockEntity) blockEntity).tick(world1, pos, state1,
                    (MultiblockEntity) blockEntity);
        }
        return null;
    }
}
