package com.brainsmash.broken_world.blocks.fluid.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidWorldUtil {
    public static SingleFluidStorage<FluidVariant> drain(World world, BlockPos pointPos) {
        SingleFluidStorage<FluidVariant> fluidStorage = new SingleFluidStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return FluidConstants.BUCKET;
            }
        };
        FluidState fluidState = world.getBlockState(pointPos).getFluidState();
        if (fluidState.isStill()) {
            world.setBlockState(pointPos, Blocks.AIR.getDefaultState());
            fluidStorage.variant = FluidVariant.of(fluidState.getFluid());
            fluidStorage.amount = FluidConstants.BUCKET;
        }
        return fluidStorage;
    }
}
