package com.brainsmash.broken_world.blocks.fluid.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

public class FluidSlot {

    public FluidVariant variant;
    public long amount;

    public FluidSlot(FluidVariant variant, long amount) {
        this.variant = variant;
        this.amount = amount;
    }

    public boolean isEmpty() {
        return variant.equals(FluidVariant.blank()) || amount == 0;
    }
}
