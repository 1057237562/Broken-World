package com.brainsmash.broken_world.client.render.block.entity;

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
import net.minecraft.util.math.Vec3f;

public class CreativeGeneratorBlockEntityRenderer implements BlockEntityRenderer<CreativeGeneratorBlockEntity> {
    private static final String SHELL = "shell";
    private static final String MID_ORB = "mid_orb";
    private static final String INNER_ORB = "inner_orb";
    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/creative_generator.png");
    public static final EntityModelLayer CREATIVE_GENERATOR = new EntityModelLayer(new Identifier(Main.MODID, "creative_generator"), "main");
    private final ModelPart shell;
    private final ModelPart midOrb;
    private final ModelPart innerOrb;

    public CreativeGeneratorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        ModelPart modelPart = ctx.getLayerModelPart(CREATIVE_GENERATOR);
        shell = modelPart.getChild(SHELL);
        midOrb = modelPart.getChild(MID_ORB);
        innerOrb = modelPart.getChild(INNER_ORB);
    }
    public static TexturedModelData getTexturedModelData(){
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                SHELL,
                ModelPartBuilder
                        .create()
                        .uv(0, 0)
                        .cuboid(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 16.0f),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                MID_ORB,
                ModelPartBuilder
                        .create()
                        .uv(8, 34)
                        .cuboid(4.0f, 4.0f, 4.0f, 8.0f, 8.0f, 8.0f),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                INNER_ORB,
                ModelPartBuilder
                        .create()
                        .uv(4, 56)
                        .cuboid(6.0f, 6.0f, 6.0f, 4.0f, 4.0f, 4.0f),
                ModelTransform.NONE
        );
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(CreativeGeneratorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        shell.render(matrices, vertexConsumer, 15728880, overlay);
        long time = entity.getWorld().getTime();
        final long cycle = 24;
        float scale = 1.0f + 0.1f*(float)Math.sin((double)time/cycle*2*Math.PI);
        Vec3f vec = new Vec3f(scale, scale, scale);
        midOrb.scale(vec);
        vec.scale(0.5f);
        innerOrb.scale(vec);
        midOrb.render(matrices, vertexConsumer, 15728880, overlay);
        innerOrb.render(matrices, vertexConsumer, 15728880, overlay);
        matrices.pop();
    }
}
