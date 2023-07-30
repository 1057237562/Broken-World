package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class MultiblockEntity extends DummyBlockEntity {

    protected Vec3i multiblockSize;

    public MultiblockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.MULTIBLOCK_ENTITY_TYPE, pos, state);
    }

    public Vec3i getMultiblockSize() {
        return multiblockSize;
    }

    public void setMultiblockSize(Vec3i multiblockSize) {
        this.multiblockSize = multiblockSize;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putLong("multiblockSize", new BlockPos(multiblockSize).asLong());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        multiblockSize = BlockPos.fromLong(nbt.getLong("multiblockSize"));
    }
}
