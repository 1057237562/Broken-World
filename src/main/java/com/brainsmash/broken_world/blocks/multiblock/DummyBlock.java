package com.brainsmash.broken_world.blocks.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState dummy, @Nullable BlockEntity blockEntity, ItemStack stack) {
        BlockState state = ((DummyBlockEntity) world.getBlockEntity(pos)).getImitateBlockState();
        BlockEntity originalEntity = ((DummyBlockEntity) world.getBlockEntity(pos)).getImitateBlockEntity();
        super.afterBreak(world, player, pos, state, originalEntity, stack);
    }
}
