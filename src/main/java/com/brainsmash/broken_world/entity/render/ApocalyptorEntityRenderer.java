package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.ApocalyptorEntity;
import com.brainsmash.broken_world.entity.model.ApocalyptorEntityModel;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class ApocalyptorEntityRenderer extends MobEntityRenderer<ApocalyptorEntity, ApocalyptorEntityModel<ApocalyptorEntity>> {


    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/apocalyptor.png");

    public ApocalyptorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ApocalyptorEntityModel<>(context.getPart(EntityRegister.MODEL_APOCALYPTOR_LAYER)), 1f);
        this.addFeature(new HeldItemFeatureRenderer<ApocalyptorEntity, ApocalyptorEntityModel<ApocalyptorEntity>>(
                (FeatureRendererContext) this, context.getHeldItemRenderer()) {

            @Override
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ApocalyptorEntity apocalyptorEntity, float f, float g, float h, float j, float k, float l) {
                super.render(matrixStack, vertexConsumerProvider, i, apocalyptorEntity, f, g, h, j, k, l);
            }
        });
    }

    @Override
    public Identifier getTexture(ApocalyptorEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(ApocalyptorEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.2f, 1.2f, 1.2f);
    }
}
