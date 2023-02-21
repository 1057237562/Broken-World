package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.GlitchedZombieEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class GlitchedZombieEntityRenderer extends BipedEntityRenderer<GlitchedZombieEntity, ZombieEntityModel<GlitchedZombieEntity>> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/glitched/zombie.png");

    public GlitchedZombieEntityRenderer(EntityRendererFactory.Context context) {
        this(context, new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE)),
                new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)),
                new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR)));
    }

    public GlitchedZombieEntityRenderer(EntityRendererFactory.Context ctx, ZombieEntityModel<GlitchedZombieEntity> bodyModel, ZombieEntityModel<GlitchedZombieEntity> legsArmorModel, ZombieEntityModel<GlitchedZombieEntity> bodyArmorModel) {
        super(ctx, bodyModel, 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, legsArmorModel, bodyArmorModel));
    }

    @Override
    public Identifier getTexture(GlitchedZombieEntity zombieEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(GlitchedZombieEntity zombieEntity) {
        return false;
    }
}

