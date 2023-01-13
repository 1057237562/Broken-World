package com.brainsmash.broken_world.util;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

public class ThermalFluidInv extends SimpleFixedFluidInv {
    public ThermalFluidInv(int invSize, FluidAmount tankCapacity) {
        super(invSize, tankCapacity);
    }

    private FluidFilter filter = ExactFluidFilter.of(Fluids.LAVA);

    @Override
    public boolean isFluidValidForTank(int tank, FluidKey fluid) {
        if(filter.matches(fluid)){
            return true;
        }
        return false;
    }

    @Override
    public FluidFilter getFilterForTank(int tank) {
        return ExactFluidFilter.of(Fluids.LAVA);
    }
}
