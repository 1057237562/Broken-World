package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ScannerBlock extends ConsumerBlock {
    public ScannerBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScannerBlockEntity(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((ScannerBlockEntity) blockEntity).tick(world1, pos, state1, (ScannerBlockEntity) blockEntity);
        return null;
    }
}
