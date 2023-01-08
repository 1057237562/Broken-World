package com.brainsmash.broken_world.entities.render;

import com.brainsmash.broken_world.Client;
import com.brainsmash.broken_world.entities.DetectEntity;
import com.brainsmash.broken_world.entities.model.DetectEntityModel;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class DetectEntityRenderer extends MobEntityRenderer<DetectEntity,DetectEntityModel> {
    public DetectEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DetectEntityModel(context.getPart(EntityRegister.MODEL_DETECTOR_LAYER)),0.0f);
    }

    @Override
    public Identifier getTexture(DetectEntity entity) {
        return new Identifier(MODID, "textures/entity/detector/detector.png");
    }
}
