package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.magical.InfusedCrystalBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

public class InfusedCrystalBlockEntityRenderer implements BlockEntityRenderer<InfusedCrystalBlockEntity> {

    private final ModelPart inner;
    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/infused_crystal_inner.png");
    public static final EntityModelLayer INFUSED_CRYSTAL = new EntityModelLayer(
            new Identifier(Main.MODID, "infused_crystal"), "main");

    public InfusedCrystalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(INFUSED_CRYSTAL);
        inner = modelPart.getChild("inner");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bone = modelPartData.addChild("inner", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = bone.addChild("cube_r1",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.of(0.0F, -8.0F, 0.0F, -0.7854F, 0.0F, -0.7854F));

        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void render(InfusedCrystalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(Quaternion.fromEulerXyz(0, 0, (float) Math.PI));
        entity.tick += tickDelta;
        matrices.scale(0.5f, 1f, 0.5f);
        matrices.multiply(Quaternion.fromEulerXyz(0, (float) (entity.tick * 4 * Math.PI / 180.0), 0));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        inner.render(matrices, vertexConsumer, 15728880, overlay);
        matrices.pop();
    }
}
