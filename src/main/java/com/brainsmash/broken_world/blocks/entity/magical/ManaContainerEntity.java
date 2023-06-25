package com.brainsmash.broken_world.blocks.entity.magical;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManaContainerEntity extends BlockEntity implements BlockEntityTicker<ManaContainerEntity> {

    public int mana = 0;

    public ManaContainerEntity(BlockEntityType<? extends ManaContainerEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("mana", mana);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        mana = nbt.getInt("mana");
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ManaContainerEntity blockEntity) {

    }
}
