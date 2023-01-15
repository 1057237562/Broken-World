package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HydroGeneratorEntity extends PowerBlockEntity {
    private static final int RANGE = 2;
    public HydroGeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.HYDRO_GENERATOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        setGenerate(0);
    }
    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient) {
            int cnt = 0;
            for (int dy = -RANGE; dy <= RANGE; dy++) {
                final int r = (int) Math.round(Math.sqrt(RANGE * RANGE - dy * dy));
                for (int dx = -r; dx <= r; dx++) {
                    final int z = (int) Math.round(Math.sqrt(r * r - dx * dx));
                    for (int dz = -z; dz <= z; dz++) {
                        FluidState fluidState = world.getFluidState(pos.east(dx).south(dz).up(dy));
                        if (!fluidState.isEmpty() && !fluidState.isStill()) {
                            cnt++;
                        }
                    }
                }
            }
            running = cnt > 0;
            setGenerate(cnt);
        }
        state = state.with(Properties.LIT, isRunning());
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        super.tick(world, pos, state, blockEntity);
    }
}
