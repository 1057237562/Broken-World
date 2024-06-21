package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.FishboneEntity;
import com.brainsmash.broken_world.entity.model.FishboneEntityModel;
import com.brainsmash.broken_world.registry.EntityModelLayerRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static com.brainsmash.broken_world.Main.MODID;

@Environment(value = EnvType.CLIENT)
public class FishboneEntityRenderer extends MobEntityRenderer<FishboneEntity, FishboneEntityModel<FishboneEntity>> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/fish/fishbone.png");

    public FishboneEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishboneEntityModel(context.getPart(EntityModelLayerRegister.MODEL_FISHBONE_LAYER)), 0.4f);
    }

    @Override
    public Identifier getTexture(FishboneEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void setupTransforms(FishboneEntity fishboneEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(fishboneEntity, matrixStack, f, g, h);
        float i = 1.0f;
        float j = 1.0f;
        if (!fishboneEntity.isTouchingWater()) {
            i = 1.3f;
            j = 1.7f;
        }
        float k = i * 4.3f * MathHelper.sin(j * 0.6f * f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k));
        matrixStack.translate(0.0, 0.0, -0.4f);
        if (!fishboneEntity.isTouchingWater()) {
            matrixStack.translate(0.2f, 0.1f, 0.0);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));
        }
    }
}
