package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ChunkloaderBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.CrusherEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ChunkloaderBlock extends ConsumerBlock {
    public ChunkloaderBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChunkloaderBlockEntity(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((ChunkloaderBlockEntity) blockEntity).tick(world1, pos, state1, (ChunkloaderBlockEntity) blockEntity);
        return null;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        ((ChunkloaderBlockEntity)world.getBlockEntity(pos)).onRemove();
        super.onBroken(world, pos, state);
    }
}
