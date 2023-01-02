package com.brainsmash.broken_world.client.render.block.entity;

import com.brainsmash.broken_world.blocks.entity.electric.BatteryBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CreativeBatteryBlockEntityRenderer implements BlockEntityRenderer<BatteryBlockEntity> {
    private final ModelPart CREEPER_MODEL;
    private final RenderLayer CREEPER_RENDER_LAYER;

    public CreativeBatteryBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        EntityModelLoader modelLoader = ctx.getLayerRenderDispatcher();
        CREEPER_MODEL = modelLoader.getModelPart(EntityModelLayers.CREEPER);
        CREEPER_RENDER_LAYER = RenderLayer.getEntityCutoutNoCullZOffset(new Identifier("textures/entity/creeper/creeper.png"));
    }

    @Override
    public void render(BatteryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CREEPER_RENDER_LAYER);
        matrices.scale(0.5f, 0.5f, 0.5f);
        CREEPER_MODEL.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
    }
}
