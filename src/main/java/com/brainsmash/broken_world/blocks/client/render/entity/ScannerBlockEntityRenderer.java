package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import com.brainsmash.broken_world.registry.ScanColorRegister;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {

    private BlockRenderManager INSTANCE;

    public ScannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getRenderManager();
    }

    @Override
    public boolean rendersOutsideBoundingBox(ScannerBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
            for (Pair<BlockPos, Integer> p : entity.scanned) {
                BlockPos pos = p.getLeft();
                Color color = ScanColorRegister.colorList.get(p.getRight());
                outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
                matrices.push();
                matrices.translate(pos.getX(), pos.getY(), pos.getZ());
                INSTANCE.renderBlockAsEntity(
                        entity.getWorld().getBlockState(entity.getPos().add(pos.getX(), pos.getY(), pos.getZ())),
                        matrices, outlineVertexConsumerProvider, light, overlay);
                matrices.pop();
            }
        }
    }
}
