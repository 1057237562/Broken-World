package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.SpellEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static com.brainsmash.broken_world.Main.MODID;

public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {

    public SpellEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SpellEntity entity) {
        return new Identifier(MODID, "textures/entity/spell.png");
    }

    @Override
    public void render(SpellEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.getOwner() != null && entity.normal != null) {
//            Vec3d pos = entity.getOwner().getEyePos().add(entity.normal);
//            entity.setPosition(pos.x, pos.y, pos.z);
            matrices.push();
            VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getLines());
            MatrixStack.Entry entry = matrices.peek();
            vc.vertex(entry.getPositionMatrix(), 0, 0, 0).color(255, 255, 0, 200).normal(entry.getNormalMatrix(), 0, 0,
                    0).next();
            Vec3d vec3d = entity.getOwner().getRotationVector().add(entity.normal.negate());

            vc.vertex(entry.getPositionMatrix(), (float) vec3d.getX(), (float) vec3d.getY(),
                    (float) vec3d.getZ()).color(255, 255, 0, 200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
            matrices.pop();
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
