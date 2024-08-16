package com.brainsmash.broken_world.entity.model;

import com.brainsmash.broken_world.entity.hostile.GelobEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.util.math.MathHelper;

public class GelobEntityModel extends SlimeEntityModel<GelobEntity> {
    public GelobEntityModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(GelobEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        entity.moveStretch = MathHelper.cos(limbAngle * 0.25f) * limbDistance * 0.4f;
    }
}