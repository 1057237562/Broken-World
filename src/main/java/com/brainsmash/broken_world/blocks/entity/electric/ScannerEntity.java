package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ScannerEntity extends ConsumerBlockEntity {
    public ScannerEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
}
