package com.brainsmash.broken_world.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class MagicBroomEntityModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart main;

    public MagicBroomEntityModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bb_main = modelPartData.addChild("main",
                ModelPartBuilder.create().uv(9, 30).cuboid(-2.0F, -14.0F, 5.0F, 4.0F, 4.0F, 17.0F),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = bb_main.addChild("cube_r1",
                ModelPartBuilder.create().uv(9, 31).cuboid(-3.0F, -2.0F, -1.0F, 6.0F, 2.0F, 17.0F),
                ModelTransform.of(-1.0F, -12.0F, 6.0F, 0.0873F, 0.0F, -1.5708F));

        ModelPartData cube_r2 = bb_main.addChild("cube_r2",
                ModelPartBuilder.create().uv(9, 31).cuboid(-3.0F, -2.0F, -1.0F, 6.0F, 2.0F, 17.0F),
                ModelTransform.of(0.0F, -13.0F, 6.0F, 0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r3 = bb_main.addChild("cube_r3",
                ModelPartBuilder.create().uv(9, 31).cuboid(-3.0F, -2.0F, -1.0F, 6.0F, 2.0F, 17.0F),
                ModelTransform.of(3.0F, -12.0F, 6.0F, -0.0873F, 0.0F, -1.5708F));

        ModelPartData cube_r4 = bb_main.addChild("cube_r4",
                ModelPartBuilder.create().uv(9, 31).cuboid(-3.0F, -2.0F, -1.0F, 6.0F, 2.0F, 17.0F),
                ModelTransform.of(0.0F, -9.0F, 6.0F, -0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r5 = bb_main.addChild("cube_r5",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -1.0F, -1.0F, 32.0F, 2.0F, 2.0F),
                ModelTransform.of(0.0F, -12.0F, 3.0F, 0.0F, 1.5708F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        main.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
