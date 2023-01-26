package com.brainsmash.broken_world.blocks.entity.fluid;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FluidTank extends AbstractPart {
    public FluidTank(PartDefinition definition, MultipartHolder holder) {
        super(definition, holder);
    }

    @Override
    public VoxelShape getShape() {
        return null;
    }

    @Nullable
    @Override
    public PartModelKey getModelKey() {
        return null;
    }
}
