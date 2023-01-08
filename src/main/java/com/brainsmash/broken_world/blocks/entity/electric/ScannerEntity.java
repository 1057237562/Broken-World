package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ScannerEntity extends ConsumerBlockEntity {

    public List<BlockPos> scanned = DefaultedList.of();
    public ScannerEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SCANNER_ENTITY_TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
