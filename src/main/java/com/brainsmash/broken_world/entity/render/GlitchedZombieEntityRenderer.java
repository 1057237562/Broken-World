package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.GlitchedZombieEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class GlitchedZombieEntityRenderer<T extends GlitchedZombieEntity, M extends ZombieEntityModel<T>> extends BipedEntityRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/glitched/zombie.png");

    public GlitchedZombieEntityRenderer(EntityRendererFactory.Context ctx, M bodyModel, M legsArmorModel, M bodyArmorModel) {
        super(ctx, bodyModel, 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, legsArmorModel, bodyArmorModel));
    }

    @Override
    public Identifier getTexture(GlitchedZombieEntity zombieEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(T zombieEntity) {
        return false;
    }
}

