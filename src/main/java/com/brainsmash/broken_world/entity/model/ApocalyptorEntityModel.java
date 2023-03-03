package com.brainsmash.broken_world.entity.model;

import com.brainsmash.broken_world.entity.hostile.ApocalyptorEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class ApocalyptorEntityModel<T extends ApocalyptorEntity> extends SinglePartEntityModel<T> implements ModelWithArms {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ApocalyptorEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.addChild("body",
                ModelPartBuilder.create().uv(30, 55).cuboid(-2.0F, -12.0F, -2.0F, 3.0F, 27.0F, 4.0F).uv(0, 42).cuboid(
                        -8.0F, 8.0F, -4.0F, 14.0F, 5.0F, 8.0F).uv(0, 25).cuboid(-9.0F, 1.0F, -5.0F, 16.0F, 7.0F,
                        10.0F).uv(0, 0).cuboid(-11.0F, -12.0F, -6.0F, 20.0F, 13.0F, 12.0F).uv(74, 39).cuboid(-7.0F,
                        13.0F, 2.1F, 13.0F, 5.0F, 0.0F).uv(74, 16).cuboid(-7.0F, 13.0F, -2.1F, 13.0F, 5.0F, 0.0F),
                ModelTransform.pivot(1.0F, -6.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg",
                ModelPartBuilder.create().uv(0, 77).cuboid(-3.0F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F).uv(42, 25).cuboid(0.0F,
                        3.0F, -2.0F, 4.0F, 4.0F, 4.0F).uv(44, 56).cuboid(1.0F, 7.0F, -3.0F, 5.0F, 10.0F, 6.0F),
                ModelTransform.pivot(4.0F, 7.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg",
                ModelPartBuilder.create().uv(74, 44).cuboid(-2.0F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F).uv(44, 72).cuboid(
                        -5.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F).uv(64, 0).cuboid(-7.0F, 7.0F, -3.0F, 5.0F, 10.0F, 6.0F),
                ModelTransform.pivot(-3.0F, 7.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm",
                ModelPartBuilder.create().uv(74, 28).cuboid(10.0F, -3.0F, -3.0F, 5.0F, 5.0F, 6.0F).uv(0, 55).cuboid(
                        11.0F, -4.0F, -4.0F, 7.0F, 14.0F, 8.0F).uv(66, 56).cuboid(12.0F, 10.0F, -3.0F, 5.0F, 9.0F,
                        6.0F), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm",
                ModelPartBuilder.create().uv(60, 71).cuboid(-15.0F, -3.0F, -3.0F, 5.0F, 5.0F, 6.0F).uv(44, 34).cuboid(
                        -18.0F, -4.0F, -4.0F, 7.0F, 14.0F, 8.0F).uv(58, 19).cuboid(-17.0F, 10.0F, -3.0F, 5.0F, 9.0F,
                        6.0F), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData shield_r1 = left_arm.addChild("shield_r1",
                ModelPartBuilder.create().uv(88, 45).cuboid(-3.0F, -35.0F, 10.0F, 2.0F, 32.0F, 10.0F),
                ModelTransform.of(-4.0F, 37.0F, 3.0F, 2.3521F, -1.3334F, -2.285F));

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));


        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        this.head.yaw = i * ((float) Math.PI / 180);
        this.head.pitch = j * ((float) Math.PI / 180);
        this.rightLeg.pitch = -1.5f * MathHelper.wrap(f, 13.0f) * g;
        this.leftLeg.pitch = 1.5f * MathHelper.wrap(f, 13.0f) * g;
        this.rightLeg.yaw = 0.0f;
        this.leftLeg.yaw = 0.0f;
        this.rotateMainArm(livingEntity);
        if (livingEntity.isAttacking()) {
            CrossbowPosing.meleeAttack(this.head, this.rightArm, true, this.handSwingProgress, h);
        }
    }

    @Override
    public void animateModel(T livingEntity, float f, float g, float h) {
        this.rightArm.pitch = (-0.2f + 1.5f * MathHelper.wrap(f, 13.0f)) * g;
        this.leftArm.pitch = (-0.2f - 1.5f * MathHelper.wrap(f, 13.0f)) * g;
        if (livingEntity.isAttacking()) {
            CrossbowPosing.meleeAttack(this.head, this.rightArm, true, this.handSwingProgress, h);
        }
        this.rotateMainArm(livingEntity);
    }

    private void rotateMainArm(T entity) {
        if (entity.isLeftHanded()) {
            this.leftArm.pitch = -1.8f;
        } else {
            this.rightArm.pitch = -1.8f;
        }
    }

    private ModelPart getAttackingArm(Arm arm) {
        if (arm == Arm.LEFT) {
            return this.leftArm;
        }
        return this.rightArm;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.getAttackingArm(arm).rotate(matrices);
        matrices.translate(1.0f, 8.0f / 16.0f, 0.0f);
        matrices.scale(1.5f, 1.5f, 1.5f);
    }
}
