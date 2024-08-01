package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.XpContainerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class XpContainer extends BlockWithEntity {
    public XpContainer(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpContainerEntity(pos, state);
    }
}
