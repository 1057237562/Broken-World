package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.electric.ScannerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

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
        for(BlockPos pos : entity.scanned) {
            matrices.push();
            matrices.translate(pos.getX(), pos.getY(), pos.getZ());
            INSTANCE.renderBlockAsEntity(entity.getWorld().getBlockState(entity.getPos().add(pos.getX(), pos.getY(), pos.getZ())), matrices, vertexConsumers, light, overlay);
            matrices.pop();
        }
    }
}
