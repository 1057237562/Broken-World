package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class InfusedCrystalBlockEntity extends BlockEntity {
    public float tick = 0;

    public InfusedCrystalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.INFUSED_CRYSTAL_ENTITY_TYPE, pos, state);
    }
}
