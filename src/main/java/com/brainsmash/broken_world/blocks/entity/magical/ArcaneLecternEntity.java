package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ArcaneLecternEntity extends BlockEntity {
    public ArcaneLecternEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.ARCANE_LECTERN_ENTITY_TYPE, pos, state);
    }
}
