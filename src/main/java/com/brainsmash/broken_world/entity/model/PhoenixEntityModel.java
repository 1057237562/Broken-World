package com.brainsmash.broken_world.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class PhoenixEntityModel<T extends Entity> extends AnimalModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart tail;

    public PhoenixEntityModel(ModelPart root) {
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
        this.tail = root.getChild(EntityModelPartNames.TAIL);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData tail = modelPartData.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create(),
                ModelTransform.pivot(-2.0F, 14.0F, 6.0F));

        ModelPartData cube_r1 = tail.addChild("cube_r1",
                ModelPartBuilder.create().uv(34, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1381F, -0.3875F, -0.081F));

        ModelPartData cube_r2 = tail.addChild("cube_r2",
                ModelPartBuilder.create().uv(12, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(2.0F, 2.0F, 0.0F, 0.192F, 0.2161F, -0.0271F));

        ModelPartData cube_r3 = tail.addChild("cube_r3",
                ModelPartBuilder.create().uv(16, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(2.0F, 2.0F, 0.0F, 0.0275F, 0.2161F, -0.0271F));

        ModelPartData cube_r4 = tail.addChild("cube_r4",
                ModelPartBuilder.create().uv(4, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(2.0F, 2.0F, 0.0F, 0.192F, -0.002F, -0.0265F));

        ModelPartData cube_r5 = tail.addChild("cube_r5",
                ModelPartBuilder.create().uv(20, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(1.0F, 2.0F, 0.0F, 0.0334F, -0.002F, -0.0265F));

        ModelPartData cube_r6 = tail.addChild("cube_r6",
                ModelPartBuilder.create().uv(8, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(1.0F, 2.0F, 0.0F, 0.192F, -0.2637F, -0.0274F));

        ModelPartData cube_r7 = tail.addChild("cube_r7",
                ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(1.0F, 2.0F, 0.0F, 0.0405F, -0.2637F, -0.0274F));

        ModelPartData cube_r8 = tail.addChild("cube_r8",
                ModelPartBuilder.create().uv(34, 21).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1322F, -0.2578F, -0.0624F));

        ModelPartData cube_r9 = tail.addChild("cube_r9",
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.128F, -0.0352F, -0.0126F));

        ModelPartData cube_r10 = tail.addChild("cube_r10",
                ModelPartBuilder.create().uv(0, 50).cuboid(0.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1283F, 0.0F, -0.0171F));

        ModelPartData cube_r11 = tail.addChild("cube_r11",
                ModelPartBuilder.create().uv(55, 1).cuboid(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F),
                ModelTransform.of(3.0F, 2.0F, 0.0F, 0.1283F, 0.0698F, -0.0171F));

        ModelPartData cube_r12 = tail.addChild("cube_r12",
                ModelPartBuilder.create().uv(0, 29).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 20.0F),
                ModelTransform.of(3.0F, 2.0F, 0.0F, 0.134F, 0.3047F, 0.0119F));

        ModelPartData cube_r13 = tail.addChild("cube_r13",
                ModelPartBuilder.create().uv(4, 29).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 20.0F),
                ModelTransform.of(3.0F, 2.0F, 0.0F, 0.1409F, 0.4343F, 0.0311F));

        ModelPartData left_leg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(4, 10).cuboid(0.0F, -8.0F, 2.0F, 1.0F, 8.0F, 1.0F).uv(0, 32).cuboid(-1.0F,
                        0.0F, -1.0F, 3.0F, 0.0F, 3.0F), ModelTransform.pivot(1.0F, 24.0F, 1.0F));

        ModelPartData right_leg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create().uv(0, 10).cuboid(0.0F, -8.0F, 2.0F, 1.0F, 8.0F, 1.0F).uv(0, 32).cuboid(-1.0F,
                        0.0F, -1.0F, 3.0F, 0.0F, 3.0F), ModelTransform.pivot(-3.0F, 24.0F, 1.0F));

        ModelPartData left_wing = modelPartData.addChild(EntityModelPartNames.LEFT_WING,
                ModelPartBuilder.create().uv(0, 50).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 6.0F, 23.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, -0.4363F, 0.0F, 0.0F));
        ModelPartData right_wing = modelPartData.addChild(EntityModelPartNames.RIGHT_WING,
                ModelPartBuilder.create().uv(0, 50).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 6.0F, 23.0F),
                ModelTransform.of(0.0F, 2.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        ModelPartData head = modelPartData.addChild("head",
                ModelPartBuilder.create().uv(109, 98).cuboid(-2.0F, -4.0F, -1.0F, 3.0F, 3.0F, 1.0F).uv(12, 3).cuboid(
                        -2.0F, -1.0F, -3.0F, 3.0F, 1.0F, 1.0F).uv(9, 14).cuboid(-1.0F, 0.0F, -6.0F, 1.0F, 2.0F,
                        5.0F).uv(9, 0).cuboid(-2.0F, 0.0F, -4.0F, 3.0F, 1.0F, 2.0F).uv(9, 7).cuboid(-2.0F, -1.0F, -2.0F,
                        3.0F, 4.0F, 3.0F), ModelTransform.pivot(0.0F, 3.0F, -6.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData neck_r1 = bb_main.addChild("neck_r1",
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -3.0F, 3.0F, 7.0F, 3.0F),
                ModelTransform.of(0.0F, -16.0F, -3.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData body_r1 = bb_main.addChild("body_r1",
                ModelPartBuilder.create().uv(55, 31).cuboid(-3.0F, -8.0F, -8.0F, 5.0F, 4.0F, 11.0F),
                ModelTransform.of(0.0F, -4.0F, 0.0F, -0.3927F, 0.0F, 0.0F));


        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.tail);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * ((float) Math.PI / 180);
        this.head.yaw = headYaw * ((float) Math.PI / 180);
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance;
        this.rightWing.roll = animationProgress;
        this.leftWing.roll = -animationProgress;
    }
}
