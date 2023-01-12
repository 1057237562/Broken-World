package com.brainsmash.broken_world.blocks.electric.generator;

import com.brainsmash.broken_world.blocks.electric.base.PowerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.generator.ThermalGeneratorEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThermalGeneratorBlock extends PowerBlock {
    public ThermalGeneratorBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThermalGeneratorEntity(pos,state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((ThermalGeneratorEntity) blockEntity).tick(world1, pos, state1, (ThermalGeneratorEntity) blockEntity);
        return null;
    }
}
