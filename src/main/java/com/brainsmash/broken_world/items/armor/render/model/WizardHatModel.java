package com.brainsmash.broken_world.items.armor.render.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class WizardHatModel extends BipedEntityModel<LivingEntity> {

    public WizardHatModel(ModelPart root) {
        super(root);
    }

    public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(53, 0).cuboid(-1.0F, -18.0F, 5.0F, 2.0F, 2.0F, 3.0F).uv(69, 1).cuboid(
                        -7.0F, -7.0F, -7.0F, 14.0F, 1.0F, 14.0F).uv(0, 13).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 3.0F,
                        10.0F).uv(0, 0).cuboid(-4.0F, -13.0F, -3.0F, 8.0F, 3.0F, 8.0F).uv(0, 27).cuboid(-3.0F, -16.0F,
                        1.0F, 6.0F, 4.0F, 5.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild(EntityModelPartNames.HAT,
                ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F));
        modelPartData.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F));
        modelPartData.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(-5.0F, 2.0F + pivotOffsetY, 0.0F));
        modelPartData.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
                        dilation), ModelTransform.pivot(5.0F, 2.0F + pivotOffsetY, 0.0F));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(-1.9F, 12.0F + pivotOffsetY, 0.0F));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(1.9F, 12.0F + pivotOffsetY, 0.0F));
        return modelData;
    }
}
