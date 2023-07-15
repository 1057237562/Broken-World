package com.brainsmash.broken_world.blocks.entity;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class UVBlockEntity extends BlockEntity {
    public UVBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.UV_ENTITY_TYPE, pos, state);
    }
}
