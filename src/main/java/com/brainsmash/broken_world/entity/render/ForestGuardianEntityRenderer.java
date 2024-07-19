package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.hostile.ForestGuardianEntity;
import com.brainsmash.broken_world.entity.model.ForestGuardianEntityModel;
import com.brainsmash.broken_world.registry.EntityModelLayerRegister;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class ForestGuardianEntityRenderer extends MobEntityRenderer<ForestGuardianEntity, ForestGuardianEntityModel<ForestGuardianEntity>> {


    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/forest_guardian.png");

    public ForestGuardianEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ForestGuardianEntityModel<>(context.getPart(EntityModelLayerRegister.MODEL_FOREST_GUARDIAN)),
                1f);
    }

    @Override
    public Identifier getTexture(ForestGuardianEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(ForestGuardianEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.2f, 1.2f, 1.2f);
    }
}
