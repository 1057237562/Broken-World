package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.client.render.FluidRenderFace;
import com.brainsmash.broken_world.blocks.client.render.FluidVariantRenderer;
import com.brainsmash.broken_world.blocks.entity.magical.XpContainerEntity;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class XpContainerEntityRenderer<T extends XpContainerEntity> implements BlockEntityRenderer<T> {
    private final BlockRenderManager manager;

    public XpContainerEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        manager = ctx.getRenderManager();
    }

    @Override
    public void render(XpContainerEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        List<FluidRenderFace> faces = new ArrayList<>();
        FluidVariantRenderer.INSTANCE.render(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)), faces,
                vertexConsumers, matrices);
        matrices.pop();
    }
}
