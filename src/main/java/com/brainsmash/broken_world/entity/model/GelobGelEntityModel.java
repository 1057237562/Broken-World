package com.brainsmash.broken_world.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

public class GelobGelEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    protected ModelPart root;

    public GelobGelEntityModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(EntityModelPartNames.CUBE,
                ModelPartBuilder.create().uv(32, 10).cuboid(-0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f),
                ModelTransform.of(0, 0, 0, 0, 0, 0)
        );
        return TexturedModelData.of(modelData, 4, 4);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }
}
