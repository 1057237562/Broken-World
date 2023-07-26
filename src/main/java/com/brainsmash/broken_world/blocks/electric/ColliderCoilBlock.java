package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderCoilBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.CrusherBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.world.World;

public class ColliderCoilBlock extends ConsumerBlock {
    public ColliderCoilBlock(Settings settings) {
        super(settings);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient())
            return null;
        return (world1, pos, state1, blockEntity) -> ((ColliderCoilBlockEntity) blockEntity).tick(world1, pos, state1, (ColliderCoilBlockEntity) blockEntity);
    }
}
