package com.brainsmash.broken_world.blocks.multiblock.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiblockComponent {
    protected World world;
    protected BlockPos pos;

    public MultiblockComponent(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public abstract void tick(World world, BlockPos pos, BlockState state);

    public abstract void writeNbt(NbtCompound nbt);

    public abstract void readNbt(NbtCompound nbt);

    public abstract ActionResult onUse(World world, BlockPos pos, BlockState imitateBlock, PlayerEntity player, Hand hand, BlockHitResult hit);
}
