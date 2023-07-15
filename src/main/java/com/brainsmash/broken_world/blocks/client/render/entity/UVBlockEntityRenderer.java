package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.UVBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class UVBlockEntityRenderer implements BlockEntityRenderer<UVBlockEntity> {
    private static final String CUBE = "cube";

    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/uv.png");
    public static final EntityModelLayer UV = new EntityModelLayer(new Identifier(Main.MODID, "uv"), "main");
    private final ModelPart cube;

    public UVBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        ModelPart modelPart = ctx.getLayerModelPart(UV);
        cube = modelPart.getChild(CUBE);
    }

    public static TexturedModelData getTexturedModelData(){
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                CUBE,
                ModelPartBuilder
                        .create()
                        .uv(0, 0)
                        .cuboid(0F, 0F, 0F, 16F, 16F, 16F),
                ModelTransform.NONE
        );
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(UVBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));

        cube.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
    }
}
