package com.brainsmash.broken_world.entity.model;

import com.brainsmash.broken_world.entity.hostile.DroneEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class DroneEntityModel<T extends DroneEntity> extends SinglePartEntityModel<T> {

    private final ModelPart root;

    public DroneEntityModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData root = modelPartData.addChild("bb_main",
                ModelPartBuilder.create().uv(0, 25).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 4.0F, 6.0F).uv(0, 0).cuboid(-4.0F,
                        -8.0F, -4.0F, 8.0F, 3.0F, 8.0F).uv(24, 25).cuboid(-2.0F, -11.0F, -2.0F, 4.0F, 3.0F, 4.0F).uv(0,
                        11).cuboid(-1.0F, -16.0F, 3.0F, 1.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = root.addChild("cube_r1",
                ModelPartBuilder.create().uv(0, 11).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F),
                ModelTransform.of(0.0F, -3.0F, 0.0F, -2.9671F, 0.0F, 3.1416F));

        ModelPartData cube_r2 = root.addChild("cube_r2",
                ModelPartBuilder.create().uv(16, 13).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F),
                ModelTransform.of(0.0F, -3.0F, 0.0F, 0.1745F, 1.0472F, 0.0F));

        ModelPartData cube_r3 = root.addChild("cube_r3",
                ModelPartBuilder.create().uv(22, 1).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F),
                ModelTransform.of(0.0F, -3.0F, 0.0F, 0.1745F, -1.0472F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
