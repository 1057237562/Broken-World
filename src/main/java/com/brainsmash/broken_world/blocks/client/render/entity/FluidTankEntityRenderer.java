package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.FluidTankEntity;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FluidTankEntityRenderer implements BlockEntityRenderer<FluidTankEntity> {
    @Override
    public void render(FluidTankEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        FluidRenderHandlerRegistry.INSTANCE.get(entity.fluidStorage.variant.getFluid()).getFluidSprites();
    }
}
