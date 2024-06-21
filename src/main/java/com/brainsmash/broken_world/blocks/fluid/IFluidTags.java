package com.brainsmash.broken_world.blocks.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class IFluidTags {
    public static final TagKey<Fluid> OIL = IFluidTags.of("oil");
    public static final TagKey<Fluid> ACID = IFluidTags.of("acid");

    private static TagKey<Fluid> of(String id) {
        return TagKey.of(RegistryKeys.FLUID, new Identifier(id));
    }
}
