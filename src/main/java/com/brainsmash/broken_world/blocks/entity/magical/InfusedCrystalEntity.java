package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class InfusedCrystalEntity extends ManaContainerEntity {
    public InfusedCrystalEntity(BlockEntityType<InfusedCrystalEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public InfusedCrystalEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.INFUSED_CRYSTAL_ENTITY_TYPE, pos, state);
    }
}
