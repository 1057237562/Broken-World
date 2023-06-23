package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CompressorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

public class CompressorBlockEntityRenderer implements BlockEntityRenderer<CompressorBlockEntity> {

    private final ModelPart head;
    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/compressor_head.png");
    public static final EntityModelLayer COMPRESSOR = new EntityModelLayer(new Identifier(Main.MODID, "compressor"),
            "main");
    private BlockRenderManager INSTANCE;

    public CompressorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getRenderManager();
        ModelPart modelPart = ctx.getLayerModelPart(COMPRESSOR);
        head = modelPart.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData group = modelPartData.addChild("head",
                ModelPartBuilder.create().uv(0, 2).cuboid(-14.0F, -9.0F, 2.0F, 12.0F, 2.0F, 12.0F).uv(0, 2).cuboid(
                        -10.0F, -15.99F, 6.0F, 4.0F, 6.99F, 4.0F), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        return TexturedModelData.of(modelData, 48, 16);
    }

    @Override
    public void render(CompressorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.multiply(Quaternion.fromEulerXyz(0, 0, (float) Math.PI));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        head.render(matrices, vertexConsumer, lightAbove, overlay);
        matrices.pop();
    }
}
