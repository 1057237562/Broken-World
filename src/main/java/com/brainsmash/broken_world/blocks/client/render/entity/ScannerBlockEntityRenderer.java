package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {

    private BlockRenderManager INSTANCE;

    public ScannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        INSTANCE = ctx.getRenderManager();
    }

    @Override
    public boolean rendersOutsideBoundingBox(ScannerBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for(BlockPos pos : entity.scanned){
            matrices.push();
            matrices.translate(pos.getX(), pos.getY(), pos.getZ());
            INSTANCE.renderBlockAsEntity(entity.getWorld().getBlockState(entity.getPos().add(pos.getX(),pos.getY(),pos.getZ())),matrices,vertexConsumers,15728640,overlay);
            matrices.pop();
        }
    }
}
