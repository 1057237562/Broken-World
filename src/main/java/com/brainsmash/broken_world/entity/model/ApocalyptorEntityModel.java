package com.brainsmash.broken_world.entity.model;

import com.brainsmash.broken_world.entity.hostile.ApocalyptorEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

public class ApocalyptorEntityModel<T extends ApocalyptorEntity> extends SinglePartEntityModel<T> implements ModelWithArms, ModelWithHead {

    private final ModelPart body;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart bb_main;

    public ApocalyptorEntityModel(ModelPart root) {
        this.body = root.getChild("body");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.bb_main = root.getChild("bb_main");
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.addChild("body",
                ModelPartBuilder.create().uv(30, 55).cuboid(-1.0F, -42.0F, -2.0F, 3.0F, 27.0F, 4.0F).uv(0, 42).cuboid(
                        -7.0F, -22.0F, -4.0F, 14.0F, 5.0F, 8.0F).uv(0, 25).cuboid(-8.0F, -29.0F, -5.0F, 16.0F, 7.0F,
                        10.0F).uv(0, 0).cuboid(-10.0F, -42.0F, -6.0F, 20.0F, 13.0F, 12.0F).uv(74, 39).cuboid(-6.0F,
                        -17.0F, 2.1F, 13.0F, 5.0F, 0.0F).uv(74, 16).cuboid(-6.0F, -17.0F, -2.1F, 13.0F, 5.0F, 0.0F),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg",
                ModelPartBuilder.create().uv(0, 77).cuboid(1.0F, -17.0F, -2.0F, 5.0F, 3.0F, 4.0F).uv(42, 25).cuboid(
                        4.0F, -14.0F, -2.0F, 4.0F, 4.0F, 4.0F).uv(44, 56).cuboid(5.0F, -10.0F, -3.0F, 5.0F, 10.0F,
                        6.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg",
                ModelPartBuilder.create().uv(74, 44).cuboid(-5.0F, -17.0F, -2.0F, 5.0F, 3.0F, 4.0F).uv(44, 72).cuboid(
                        -8.0F, -14.0F, -2.0F, 4.0F, 4.0F, 4.0F).uv(64, 0).cuboid(-10.0F, -10.0F, -3.0F, 5.0F, 10.0F,
                        6.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm",
                ModelPartBuilder.create().uv(74, 28).cuboid(0.0F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F).uv(0, 55).cuboid(1.0F,
                        -3.0F, -4.0F, 7.0F, 14.0F, 8.0F).uv(66, 56).cuboid(2.0F, 11.0F, -3.0F, 5.0F, 9.0F, 6.0F),
                ModelTransform.pivot(10.0F, -14.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm",
                ModelPartBuilder.create().uv(60, 71).cuboid(4.0F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F).uv(44, 34).cuboid(
                        1.0F, -3.0F, -4.0F, 7.0F, 14.0F, 8.0F).uv(58, 19).cuboid(2.0F, 11.0F, -3.0F, 5.0F, 9.0F, 6.0F),
                ModelTransform.pivot(-19.0F, -14.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData shield_r1 = bb_main.addChild("shield_r1",
                ModelPartBuilder.create().uv(88, 45).cuboid(-3.0F, -35.0F, 10.0F, 2.0F, 32.0F, 10.0F),
                ModelTransform.of(-3.0F, 0.0F, 3.0F, -2.0742F, -1.2204F, 2.1011F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {

    }

    @Override
    public ModelPart getHead() {
        return null;
    }

    @Override
    public ModelPart getPart() {
        return null;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
