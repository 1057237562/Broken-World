package com.brainsmash.broken_world.blocks.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.generator.WindTurbineEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;

public class WindTurbineEntityRenderer implements BlockEntityRenderer<WindTurbineEntity> {

    public static final EntityModelLayer WIND_TURBINE = new EntityModelLayer(new Identifier(Main.MODID, "wind_turbine"),
            "main");

    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/wind_turbine.png");
    private static final String BLADE1 = "blade1";
    private static final String BLADE2 = "blade2";
    private static final String BLADE3 = "blade3";

    private final ModelPart blade1;
    private final ModelPart blade2;
    private final ModelPart blade3;

    float tick = 0.0f;

    public WindTurbineEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(WIND_TURBINE);
        blade1 = modelPart.getChild(BLADE1);
        blade2 = modelPart.getChild(BLADE2);
        blade3 = modelPart.getChild(BLADE3);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BLADE1,
                ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F),
                ModelTransform.rotation(2.1561F, 0.9275F, -2.4498F));
        modelPartData.addChild(BLADE2,
                ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F),
                ModelTransform.rotation(0.9855F, -0.9275F, -2.4498F));
        modelPartData.addChild(BLADE3,
                ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F),
                ModelTransform.rotation(-1.5708F, 0.0F, -0.3927F));
        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void render(WindTurbineEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        tick += 0.1f * entity.getGenerate() / entity.maxOutput * tickDelta;
        matrices.translate(0.5, 0.5, 0.5);
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        matrices.translate(0.6 * direction.getOffsetX(), 0.6 * direction.getOffsetY(), 0.6 * direction.getOffsetZ());
        matrices.multiply(Quaternion.fromEulerXyz(tick * direction.getOffsetX(), tick * direction.getOffsetY(),
                tick * direction.getOffsetZ()));
        matrices.multiply(direction.getRotationQuaternion());
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        blade1.render(matrices, vertexConsumer, 15728880, overlay);
        blade2.render(matrices, vertexConsumer, 15728880, overlay);
        blade3.render(matrices, vertexConsumer, 15728880, overlay);
        matrices.pop();
    }
}