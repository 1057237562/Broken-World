package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.GelobEntity;
import com.brainsmash.broken_world.entity.model.GelobEntityModel;
import com.brainsmash.broken_world.entity.render.feature.GelobOverlayFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static com.brainsmash.broken_world.Main.MODID;

public class GelobEntityRenderer extends MobEntityRenderer<GelobEntity, GelobEntityModel> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/gelob.png");

    public GelobEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GelobEntityModel(context.getPart(EntityModelLayers.SLIME)), 0.25f);
        this.addFeature(new GelobOverlayFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    public void render(GelobEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25f * (float)entity.getSize();
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(GelobEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(0.999f, 0.999f, 0.999f);
        matrices.translate(0.0, 0.001f, 0.0);
        float h = entity.getSize();
        float jumpStretchWeight = (float) (GelobEntity.TOUCH_DOWN_STRETCH_TICKS - entity.onGroundTicks) / GelobEntity.TOUCH_DOWN_STRETCH_TICKS;
        float i = MathHelper.lerp(
                amount,
                MathHelper.lerp(jumpStretchWeight, 0, entity.lastJumpStretch),
                MathHelper.lerp(jumpStretchWeight, 0, entity.jumpStretch)
        );
        i += MathHelper.lerp(jumpStretchWeight, entity.moveStretch, 0);
        i /= h * 0.5f + 1.0f;
        float j = 1.0f / (i + 1.0f);
        matrices.scale(j * h, 1.0f / j * h, j * h);
    }

    @Override
    public Identifier getTexture(GelobEntity entity) {
        return TEXTURE;
    }
}
