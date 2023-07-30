package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DummyBlock extends BlockWithEntity {
    public DummyBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DummyBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState dummy, PlayerEntity player) {
        BlockState state = ((DummyBlockEntity) world.getBlockEntity(pos)).getImitateBlockState();
        super.onBreak(world, pos, state, player);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState dummy, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof DummyBlockEntity dummyBlockEntity) {
            BlockState state = dummyBlockEntity.getImitateBlockState();
            if (state.getBlock() != BlockRegister.dummy)
                return state.getBlock().getOutlineShape(state, world, pos, context);
        }
        return super.getOutlineShape(dummy, world, pos, context);
    }


    @Override
    public VoxelShape getCullingShape(BlockState dummy, BlockView world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof DummyBlockEntity dummyBlockEntity) {
            BlockState state = dummyBlockEntity.getImitateBlockState();
            if (state.getBlock() != BlockRegister.dummy) return state.getBlock().getCullingShape(state, world, pos);
        }
        return super.getCullingShape(dummy, world, pos);

    }

    @Override
    public boolean hasDynamicBounds() {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState dummy, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof DummyBlockEntity dummyBlockEntity) {
            BlockState state = dummyBlockEntity.getImitateBlockState();
            if (state.getBlock() != BlockRegister.dummy) return state.getCollisionShape(world, pos, context);
        }
        return super.getCollisionShape(dummy, world, pos, context);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = ((DummyBlockEntity) world.getBlockEntity(pos)).getImitateBlockEntity();
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState dummy, @Nullable BlockEntity blockEntity, ItemStack stack) {
        BlockState state = ((DummyBlockEntity) blockEntity).getImitateBlockState();
        BlockEntity originalEntity = ((DummyBlockEntity) blockEntity).getImitateBlockEntity();
        super.afterBreak(world, player, pos, state, originalEntity, stack);
    }

}
