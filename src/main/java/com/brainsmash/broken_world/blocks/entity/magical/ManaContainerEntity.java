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
    public int transferRate = 1;
    public int maxMana = 100;
    public BlockPos link;

    public ManaContainerEntity(BlockEntityType<? extends ManaContainerEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        link = pos;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("mana", mana);
        nbt.putInt("transferRate", transferRate);
        nbt.putInt("maxMana", maxMana);
        nbt.putLong("link", link.asLong());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        mana = nbt.getInt("mana");
        transferRate = nbt.getInt("transferRate");
        maxMana = nbt.getInt("maxMana");
        link = BlockPos.fromLong(nbt.getLong("link"));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ManaContainerEntity blockEntity) {
        if (!world.isClient) {
            if (link != null) {
                BlockEntity be = world.getBlockEntity(link);
                if (be instanceof ManaContainerEntity) {
                    ManaContainerEntity mce = (ManaContainerEntity) be;
                    if (mana > 0) {
                        if (mce.mana < mce.maxMana) {
                            int transfer = Math.min(Math.min(transferRate, mce.maxMana - mce.mana), mana);
                            mana -= transfer;
                            mce.mana += transfer;
                        }
                    }
                }
            }
        }
    }
}
