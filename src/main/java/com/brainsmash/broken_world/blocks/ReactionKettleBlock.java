package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ReactionKettleBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReactionKettleBlock extends ConsumerBlock {
    public ReactionKettleBlock(Settings settings) {
        super(settings);
        setDefaultState(
                stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.LIT,
                        false));
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactionKettleBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((ReactionKettleBlockEntity) blockEntity).tick(world1, pos,
                    state1, (ReactionKettleBlockEntity) blockEntity);
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
}
