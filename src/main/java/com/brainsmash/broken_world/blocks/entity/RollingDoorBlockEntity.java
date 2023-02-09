package com.brainsmash.broken_world.blocks.entity;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class RollingDoorBlockEntity extends BlockEntity {
    private int level = 0;

    public RollingDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.ROLLING_DOOR_ENTITY_TYPE, pos, state);
    }

    public void contract() {
        if (!world.isClient) {
            if (world.getBlockEntity(pos.offset(Direction.DOWN, 1)) instanceof RollingDoorBlockEntity head) {
                head.contract();
            } else if (world.getBlockEntity(
                    pos.offset(Direction.UP, 1)) instanceof RollingDoorBlockEntity rollingDoorBlockEntity) {
                rollingDoorBlockEntity.level += level + 1;
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                rollingDoorBlockEntity.contract();
                
            }
        }
    }

    public boolean extract() {
        if (!world.isClient) {
            if (level > 0) {
                for (int i = 1; i <= level; i++) {
                    if (world.getBlockState(pos.offset(Direction.DOWN, i)).isOf(Blocks.AIR)) {
                        world.setBlockState(pos.offset(Direction.DOWN, i),
                                BlockRegister.blocks[BlockRegistry.ROLLING_DOOR.ordinal()].getDefaultState());
                    } else if (i == 1) {
                        return false;
                    } else {
                        level -= i - 1;
                        return true;
                    }
                }
                level = 0;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("level", level);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        level = nbt.getInt("level");
    }
}
