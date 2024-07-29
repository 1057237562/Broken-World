package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.vehicle.MagicBroomEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

public class MagicBroomEntityRenderer<T extends MagicBroomEntity> extends EntityRenderer<T> {

    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/magic_broom.png");

    public MagicBroomEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
