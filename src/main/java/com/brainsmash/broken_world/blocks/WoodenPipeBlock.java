package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.blocks.entity.WoodenPipeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WoodenPipeBlock extends BlockWithEntity {
    public WoodenPipeBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenPipeBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((WoodenPipeBlockEntity) blockEntity).tick(world1, pos, state1,
                    (WoodenPipeBlockEntity) blockEntity);
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getSide() != Direction.UP && ctx.getSide() != Direction.DOWN)
            return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getSide().getOpposite());
        else return super.getPlacementState(ctx);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> {
                return VoxelShapes.cuboid(6F / 16F, 6F / 16F, 6F / 16F, 10F / 16F, 9F / 16F, 1);
            }
            case WEST -> {
                return VoxelShapes.cuboid(0, 6F / 16F, 6F / 16F, 10F / 16F, 9F / 16F, 10F / 16F);
            }
            case EAST -> {
                return VoxelShapes.cuboid(6F / 16F, 6F / 16F, 6F / 16F, 1, 9F / 16F, 10F / 16F);
            }
            default -> {
                return VoxelShapes.cuboid(6F / 16F, 6F / 16F, 0, 10F / 16F, 9F / 16F, 10F / 16F);
            }
        }
    }
}
