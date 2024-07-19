package com.brainsmash.broken_world.blocks.fluid.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.NbtCompound;

public abstract class SingleFluidStorage<T extends FluidVariant> extends SingleVariantStorage<T> {

    public void readNbt(NbtCompound nbt) {
        variant = (T) FluidVariant.fromNbt(nbt.getCompound("variant"));
        amount = nbt.getInt("amount");
    }

    public boolean isEmpty() {
        return variant.equals(FluidVariant.blank()) || amount == 0;
    }
}
