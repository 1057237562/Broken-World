package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.PhoenixEntity;
import com.brainsmash.broken_world.entity.model.PhoenixEntityModel;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static com.brainsmash.broken_world.Main.MODID;

public class PhoenixEntityRenderer extends MobEntityRenderer<PhoenixEntity, PhoenixEntityModel<PhoenixEntity>> {

    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/phoenix.png");

    public PhoenixEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PhoenixEntityModel<>(context.getPart(EntityRegister.MODEL_PHOENIX_LAYER)), 0.8f);
    }

    @Override
    public Identifier getTexture(PhoenixEntity entity) {
        return TEXTURE;
    }

    @Override
    protected float getAnimationProgress(PhoenixEntity entity, float tickDelta) {
        float g = MathHelper.lerp(tickDelta, entity.prevFlapProgress, entity.flapProgress);
        float h = MathHelper.lerp(tickDelta, entity.prevMaxWingDeviation, entity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0f) * h;
    }

    @Override
    protected void scale(PhoenixEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(2f, 2f, 2f);
    }
}
