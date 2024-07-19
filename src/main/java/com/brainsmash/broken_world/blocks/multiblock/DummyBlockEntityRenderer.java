package com.brainsmash.broken_world.blocks.multiblock;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class DummyBlockEntityRenderer implements BlockEntityRenderer<DummyBlockEntity> {

    private BlockRenderManager blockRenderManager;
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public DummyBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        blockRenderManager = ctx.getRenderManager();
        blockEntityRenderDispatcher = ctx.getRenderDispatcher();
    }

    @Override
    public void render(DummyBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.visible) return;
        blockRenderManager.renderBlock(entity.imitateBlock, entity.getPos(), entity.getWorld(), matrices,
                vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom());
        if (entity.imitateBlockEntity != null) {
            blockEntityRenderDispatcher.render(entity.imitateBlockEntity, tickDelta, matrices, vertexConsumers);
        }
    }
}
