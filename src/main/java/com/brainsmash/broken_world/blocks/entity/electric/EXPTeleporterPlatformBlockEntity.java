package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EXPTeleporterPlatformBlockEntity extends ConsumerBlockEntity {
    public EXPTeleporterPlatformBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
}
