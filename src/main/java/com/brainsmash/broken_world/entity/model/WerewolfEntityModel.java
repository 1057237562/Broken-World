package com.brainsmash.broken_world.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class WerewolfEntityModel<T extends LivingEntity> extends PlayerEntityModel<T> {

    private static final String UPPER_BODY = "upper_body";
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart neck;

    public WerewolfEntityModel(ModelPart root) {
        super(root, true);
        this.neck = root.getChild(UPPER_BODY);
        this.rightHindLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftHindLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightFrontLeg = root.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftFrontLeg = root.getChild(EntityModelPartNames.LEFT_ARM);
        this.tail = root.getChild(EntityModelPartNames.TAIL);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float f = 13.5f;
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.0f, -3.0f, -2.0f, 6.0f, 6.0f, 4.0f).uv(16, 14).cuboid(
                        -2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).uv(16, 14).cuboid(2.0f, -5.0f, 0.0f, 2.0f, 2.0f, 1.0f).uv(
                        0, 10).cuboid(-0.5f, -0.001f, -5.0f, 3.0f, 3.0f, 4.0f),
                ModelTransform.pivot(-1.0f, -3.0f, 1.0f));
        modelPartData.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(18, 14).cuboid(-3.0f, -2.0f, -3.0f, 6.0f, 9.0f, 6.0f),
                ModelTransform.of(0.0f, 9.0f, 3.0f, 0.0f, 0.0f, 0.0f));
        modelPartData.addChild(UPPER_BODY,
                ModelPartBuilder.create().uv(21, 0).cuboid(-3.0f, -3.0f, -3.0f, 8.0f, 6.0f, 7.0f),
                ModelTransform.of(-1.0f, 4.0f, 3.0f, 0.0f, 0.0f, 0.0f));
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 18).cuboid(0.0f, 0.0f, -1.0f, 2.0f, 8.0f,
                2.0f);
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, modelPartBuilder,
                ModelTransform.pivot(-2.5f, 16.0f, 3.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, modelPartBuilder,
                ModelTransform.pivot(0.5f, 16.0f, 3.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, modelPartBuilder,
                ModelTransform.pivot(-5.5f, 2.0f, 3.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_ARM, modelPartBuilder, ModelTransform.pivot(3.5f, 2.0f, 3.0f));
        ModelPartData tail = modelPartData.addChild(EntityModelPartNames.TAIL,
                ModelPartBuilder.create().uv(9, 18).cuboid(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f),
                ModelTransform.of(-1.0f, 12.0f, 5.0f, 0.62831855f, 0.0f, 0.0f));

        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("ear", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("cloak", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("left_sleeve", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("right_sleeve", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("left_pants", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("right_pants", ModelPartBuilder.create(), ModelTransform.NONE);
        modelPartData.addChild("jacket", ModelPartBuilder.create(), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg,
                this.tail, this.neck);
    }

    @Override
    public void animateModel(T wolfEntity, float f, float g, float h) {
        this.tail.yaw = MathHelper.cos(f * 0.6662f) * 1.4f * g;
        this.neck.pitch = (float) (Math.PI / 2);
        this.rightHindLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g;
        this.leftHindLeg.pitch = MathHelper.cos(f * 0.6662f + (float) Math.PI) * 1.4f * g;
        this.rightFrontLeg.pitch = MathHelper.cos(f * 0.6662f + (float) Math.PI) * 1.4f * g;
        this.leftFrontLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g;
    }

    @Override
    public void setAngles(T wolfEntity, float f, float g, float h, float i, float j) {
        this.head.pitch = j * ((float) Math.PI / 180);
        this.head.yaw = i * ((float) Math.PI / 180);
    }
}
