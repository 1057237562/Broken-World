package com.brainsmash.broken_world.blocks.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CentrifugeBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

public class CentrifugeBlockEntityRenderer implements BlockEntityRenderer<CentrifugeBlockEntity> {

    private final ModelPart inner;
    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/centrifuge.png");
    public static final EntityModelLayer CENTRIFUGE = new EntityModelLayer(new Identifier(Main.MODID, "centrifuge"),
            "main");

    public CentrifugeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(CENTRIFUGE);
        inner = modelPart.getChild("inner");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("inner",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -13.0F, 7.0F, 2.0F, 8.0F, 2.0F).uv(8, 0).cuboid(-9.0F,
                        -16.0F, 7.0F, 2.0F, 14.0F, 2.0F).uv(0, 0).cuboid(-14.0F, -13.0F, 7.0F, 2.0F, 8.0F, 2.0F).uv(0,
                        0).cuboid(-9.0F, -13.0F, 12.0F, 2.0F, 8.0F, 2.0F).uv(0, 0).cuboid(-9.0F, -13.0F, 2.0F, 2.0F,
                        8.0F, 2.0F).uv(2, 4).cuboid(-9.0F, -11.0F, 2.0F, 2.0F, 0.0F, 12.0F).uv(-2, 16).cuboid(-14.0F,
                        -11.0F, 7.0F, 12.0F, 0.0F, 2.0F), ModelTransform.pivot(8.0F, 0.0F, -8.0F));

        return TexturedModelData.of(modelData, 24, 18);
    }

    @Override
    public void render(CentrifugeBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(Quaternion.fromEulerXyz(0, 0, (float) Math.PI));
        if (entity.isRunning()) entity.tick += tickDelta;
        matrices.multiply(Quaternion.fromEulerXyz(0, (float) (entity.tick * 16 * Math.PI / 180.0), 0));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        inner.render(matrices, vertexConsumer, lightAbove, overlay);
        matrices.pop();
    }
}
