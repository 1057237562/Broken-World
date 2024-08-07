package com.brainsmash.broken_world.blocks.fluid;

import com.brainsmash.broken_world.registry.FluidRegister;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class PotionFluid extends Fluid {

    public Potion potion;

    public PotionFluid(Potion potion) {
        this.potion = potion;
    }

    public static PotionFluid get(Potion potion) {
        return FluidRegister.potionFluids.get(potion);
    }

    public int getRenderColor() {
        return PotionUtil.getColor(potion);
    }

    @Override
    public Item getBucketItem() {
        return null;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected Vec3d getVelocity(BlockView world, BlockPos pos, FluidState state) {
        return null;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 0;
    }

    @Override
    protected float getBlastResistance() {
        return 0;
    }

    @Override
    public float getHeight(FluidState state, BlockView world, BlockPos pos) {
        return 0;
    }

    @Override
    public float getHeight(FluidState state) {
        return 0;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return null;
    }

    @Override
    public boolean isStill(FluidState state) {
        return true;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockView world, BlockPos pos) {
        return null;
    }
}
