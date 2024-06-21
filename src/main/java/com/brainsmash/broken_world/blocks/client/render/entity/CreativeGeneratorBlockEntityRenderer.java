package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CreativeGeneratorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class CreativeGeneratorBlockEntityRenderer implements BlockEntityRenderer<CreativeGeneratorBlockEntity> {
    private static final String FRAME = "frame";
    private static final String SHELL = "shell";
    private static final String MID_ORB = "mid_orb";
    private static final String INNER_ORB = "inner_orb";
    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/creative_generator.png");
    public static final EntityModelLayer CREATIVE_GENERATOR = new EntityModelLayer(
            new Identifier(Main.MODID, "creative_generator"), "main");
    private final ModelPart frame;
    private final ModelPart shell;
    private final ModelPart midOrb;
    private final ModelPart innerOrb;
    private static final long CYCLE = 48;

    public CreativeGeneratorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(CREATIVE_GENERATOR);
        frame = modelPart.getChild(FRAME);
        frame.scale(new Vector3f(-0.01f, -0.01f, -0.01f));
        shell = modelPart.getChild(SHELL);
        shell.scale(new Vector3f(-0.01f, -0.01f, -0.01f));
        midOrb = modelPart.getChild(MID_ORB);
        innerOrb = modelPart.getChild(INNER_ORB);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(FRAME, ModelPartBuilder.create().uv(64, 0).cuboid(-8f, -8f, -8f, 16.0f, 16.0f, 16.0f),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f));
        modelPartData.addChild(SHELL, ModelPartBuilder.create().uv(0, 0).cuboid(-8f, -8f, -8f, 16.0f, 16.0f, 16.0f),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f));
        modelPartData.addChild(MID_ORB, ModelPartBuilder.create().uv(0, 34).cuboid(-4f, -4f, -4f, 8.0f, 8.0f, 8.0f),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f));
        modelPartData.addChild(INNER_ORB, ModelPartBuilder.create().uv(0, 56).cuboid(-2f, -2f, -2f, 4.0f, 4.0f, 4.0f),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void render(CreativeGeneratorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        shell.render(matrices, vertexConsumer, 15728880, overlay);
        frame.render(matrices, vertexConsumer, light, overlay);
        long time = entity.getWorld().getTime();
        float scale = 0.1f * (float) Math.sin((double) time / CYCLE * 2 * Math.PI);
        midOrb.xScale = midOrb.yScale = midOrb.zScale = 1.0f + scale;
        scale *= 0.5f;
        innerOrb.xScale = innerOrb.yScale = innerOrb.zScale = 1.0f + scale;
        midOrb.render(matrices, vertexConsumer, 15728880, overlay);
        innerOrb.render(matrices, vertexConsumer, 15728880, overlay);
        matrices.pop();
    }
}
