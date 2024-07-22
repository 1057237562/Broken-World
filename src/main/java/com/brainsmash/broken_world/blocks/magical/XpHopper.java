package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.XpHopperEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class XpHopper extends BlockWithEntity {

    public XpHopper(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpHopperEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((XpHopperEntity) blockEntity).tick(world1, pos, state1,
                    (XpHopperEntity) blockEntity);
        return null;
    }
}
