package com.brainsmash.broken_world.blocks.entity;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorBlockEntity extends BlockEntity implements BlockEntityTicker<ReactorBlockEntity> {
    public ReactorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.REACTOR_ENTITY_TYPE, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ReactorBlockEntity blockEntity) {

    }
}
