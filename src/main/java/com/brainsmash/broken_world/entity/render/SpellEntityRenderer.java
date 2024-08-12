package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.SpellEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {

    public SpellEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SpellEntity entity) {
        return new Identifier(MODID, "textures/entity/spell.png");
    }
}
