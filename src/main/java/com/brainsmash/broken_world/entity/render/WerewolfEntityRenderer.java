package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.WerewolfEntity;
import com.brainsmash.broken_world.entity.model.WerewolfEntityModel;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class WerewolfEntityRenderer extends MobEntityRenderer<WerewolfEntity, WerewolfEntityModel<WerewolfEntity>> {


    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/werewolf.png");

    public WerewolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new WerewolfEntityModel<>(context.getPart(EntityRegister.MODEL_WEREWOLF_LAYER)), 0.35f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()) {

            @Override
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WerewolfEntity werewolfEntity, float f, float g, float h, float j, float k, float l) {
                super.render(matrixStack, vertexConsumerProvider, i, werewolfEntity, f, g, h, j, k, l);
            }
        });
    }

    @Override
    public Identifier getTexture(WerewolfEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(WerewolfEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.5f, 1.5f, 1.5f);
    }
}
