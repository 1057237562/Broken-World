/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package com.brainsmash.broken_world.entity.render.feature;

import com.brainsmash.broken_world.entity.hostile.GelobEntity;
import com.brainsmash.broken_world.entity.model.GelobEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value=EnvType.CLIENT)
public class GelobOverlayFeatureRenderer
        extends FeatureRenderer<GelobEntity, GelobEntityModel> {
    private final GelobEntityModel model;

    public GelobOverlayFeatureRenderer(FeatureRendererContext<GelobEntity, GelobEntityModel> context, EntityModelLoader loader) {
        super(context);
        this.model = new GelobEntityModel(loader.getModelPart(EntityModelLayers.SLIME_OUTER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, GelobEntity livingEntity, float f, float g, float h, float j, float k, float l) {
        boolean bl;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
        if (livingEntity.isInvisible() && !bl) {
            return;
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(livingEntity))) : vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(livingEntity, f, g, h);
        this.model.setAngles(livingEntity, f, g, j, k, l);
        this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
    }
}

