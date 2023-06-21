package com.brainsmash.broken_world.blocks.entity.magical;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ManaContainerEntity extends BlockEntity {

    public int mana = 0;

    public ManaContainerEntity(BlockEntityType<? extends ManaContainerEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
