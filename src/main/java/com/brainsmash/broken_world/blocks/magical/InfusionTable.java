package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.model.CustomModelBlock;
import com.brainsmash.broken_world.screens.cotton.InfusionTableScreen;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class InfusionTable extends BlockWithEntity implements CustomModelBlock {
    public InfusionTable(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null; // TODO
    }
}
