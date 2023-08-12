package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderCoilBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderControllerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ColliderCoilBlock extends ConsumerBlock {
    public ColliderCoilBlock(Settings settings) {
        super(settings);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient())
            return null;
        return (world1, pos, state1, blockEntity) -> ((ColliderCoilBlockEntity) blockEntity).tick(world1, pos, state1, (ColliderCoilBlockEntity) blockEntity);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ColliderCoilBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof ColliderCoilBlockEntity entity)
                entity.onMultiblockBreak();
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockEntity(pos) instanceof ColliderCoilBlockEntity coil && coil.hasBoundController()) {
            BlockEntity sourceEntity = world.getBlockEntity(sourcePos);
            if (
                    sourceEntity instanceof ColliderCoilBlockEntity sourceCoil && !sourceCoil.hasBoundController() ||
                    sourceEntity instanceof ColliderControllerBlockEntity sourceController && !sourceController.hasBoundCoils()
            )
                coil.onMultiblockBreak();
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }
}
