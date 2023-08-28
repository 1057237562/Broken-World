package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class StoneBaseBlockEntity extends BlockEntity {

    public ItemStack itemStack = ItemStack.EMPTY;

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("item", itemStack.writeNbt(new NbtCompound()));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        itemStack = ItemStack.fromNbt(nbt.getCompound("item"));
    }

    public StoneBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.STONE_BASE_ENTITY_TYPE, pos, state);
    }
}
