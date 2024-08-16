package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.GelobGelEntity;
import com.brainsmash.broken_world.entity.model.GelobGelEntityModel;
import com.brainsmash.broken_world.registry.EntityModelLayerRegister;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class GelobGelEntityRenderer extends EntityRenderer<GelobGelEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/gelob.png");

    protected GelobGelEntityModel<GelobGelEntity> model;

    public GelobGelEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        model = new GelobGelEntityModel<>(context.getModelLoader().getModelPart(EntityModelLayerRegister.MODEL_GELOB_GEL_LAYER));
    }

    @Override
    public void render(GelobGelEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float scale = entity.getScale();
        matrices.scale(scale, scale, scale);
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));
        model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public Identifier getTexture(GelobGelEntity entity) {
        return TEXTURE;
    }
}