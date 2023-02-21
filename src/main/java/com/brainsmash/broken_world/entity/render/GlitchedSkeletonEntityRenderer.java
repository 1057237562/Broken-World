package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.GlitchedSkeletonEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class GlitchedSkeletonEntityRenderer extends BipedEntityRenderer<GlitchedSkeletonEntity, SkeletonEntityModel<GlitchedSkeletonEntity>> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/glitched/skeleton.png");

    public GlitchedSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.SKELETON, EntityModelLayers.SKELETON_INNER_ARMOR,
                EntityModelLayers.SKELETON_OUTER_ARMOR);
    }

    public GlitchedSkeletonEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new SkeletonEntityModel(ctx.getPart(layer)), 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, new SkeletonEntityModel(ctx.getPart(legArmorLayer)),
                new SkeletonEntityModel(ctx.getPart(bodyArmorLayer))));
    }

    @Override
    public Identifier getTexture(GlitchedSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(GlitchedSkeletonEntity abstractSkeletonEntity) {
        return abstractSkeletonEntity.isShaking();
    }
}
