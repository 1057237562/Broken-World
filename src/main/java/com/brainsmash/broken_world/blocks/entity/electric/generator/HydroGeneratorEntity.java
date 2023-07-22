package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
        if (!world.isClient) {
            int in_cnt = 0;
            int out_cnt = 0;
            Direction face = state.get(Properties.HORIZONTAL_FACING);
            Direction oppo = face.getOpposite();
            BlockPos facepos = pos.offset(face, 3);
            BlockPos oppopos = pos.offset(oppo, 3);
            for (int dy = 0; dy <= RANGE; dy++) {
                final int r = (int) Math.sqrt(RANGE * RANGE - dy * dy);
                for (int dx = -r; dx <= r; dx++) {
                    final int z = (int) Math.sqrt(r * r - dx * dx);
                    for (int dz = -z; dz <= z; dz++) {
                        FluidState fluidState = world.getFluidState(
                                facepos.offset(oppo, dy).offset(oppo.rotateClockwise(Direction.Axis.Y), dx).offset(
                                        Direction.DOWN, dz));
                        if (!fluidState.isEmpty() && !fluidState.isStill()) {
                            in_cnt++;
                        }
                    }
                }
            }
            for (int dy = 0; dy <= RANGE; dy++) {
                final int r = (int) Math.sqrt(RANGE * RANGE - dy * dy);
                for (int dx = -r; dx <= r; dx++) {
                    final int z = (int) Math.sqrt(r * r - dx * dx);
                    for (int dz = -z; dz <= z; dz++) {
                        FluidState fluidState = world.getFluidState(
                                oppopos.offset(face, dy).offset(face.rotateClockwise(Direction.Axis.Y), dx).offset(
                                        Direction.DOWN, dz));
                        if (!fluidState.isEmpty() && !fluidState.isStill()) {
                            out_cnt++;
                        }
                    }
                }
            }
            running = Math.min(in_cnt, out_cnt) > 0;
            setGenerate(Math.min(in_cnt, out_cnt));
        }
        state = state.with(Properties.LIT, isRunning());
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        super.tick(world, pos, state, blockEntity);
    }
}
