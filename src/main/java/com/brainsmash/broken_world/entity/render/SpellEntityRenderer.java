package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.SpellEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import static com.brainsmash.broken_world.Main.MODID;

public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {

    public SpellEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SpellEntity entity) {
        return new Identifier(MODID, "textures/entity/magic_spell.png");
    }

    @Override
    public void render(SpellEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.getOwner() != null && entity.normal != null) {
            matrices.push();
            Vec3d shift = entity.getOwner().getEyePos().subtract(entity.normal.negate().add(entity.getPos()));
            matrices.translate(shift.x, shift.y, shift.z);
            renderLines(vertexConsumers.getBuffer(RenderLayer.getLines()), matrices, entity);

            //MinecraftClient.getInstance().getTextureManager().bindTexture(getTexture(entity));
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, getTexture(entity));
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.DST_ALPHA);
            RenderSystem.enableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            renderSpell(bufferBuilder, matrices, entity, light);
            tessellator.draw();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private void renderLines(VertexConsumer vc, MatrixStack matrices, SpellEntity entity) {
        MatrixStack.Entry entry = matrices.peek();
        Quaternion rotation = new Quaternion(0, 0, 0, 1);
        rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.rot.y));
        rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.rot.x));
        rotation.conjugate();

        double u = entity.normal.negate().dotProduct(entity.normal) / entity.normal.negate().dotProduct(
                entity.getOwner().getRotationVector());
        Vec3f vec3f = new Vec3f(entity.getOwner().getRotationVector().multiply(u).add(entity.normal.negate()));
        Vec3f vec3f1 = vec3f.copy();
        vec3f1.rotate(rotation);

        if (new Vec3d(vec3f1).length() < 1.25) {
            vc.vertex(entry.getPositionMatrix(), 0, 0, 0).color(255, 255, 0, 200).normal(entry.getNormalMatrix(), 0, 0,
                    0).next();
            vc.vertex(entry.getPositionMatrix(), vec3f.getX(), vec3f.getY(), vec3f.getZ()).color(255, 255, 0,
                    200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
        }

    }

    private void renderSpell(VertexConsumer vc, MatrixStack matrices, SpellEntity entity, int light) {
        entity.scale = 0.04f + entity.scale * 31f / 32f;
        matrices.push();

        Vec3f[] v = new Vec3f[]{
                new Vec3f(-1.0F, -1.0F, 0.0F),
                new Vec3f(-1.0F, 1.0F, 0.0F),
                new Vec3f(1.0F, 1.0F, 0.0F),
                new Vec3f(1.0F, -1.0F, 0.0F)
        };
        Quaternion rotation = new Quaternion(0, 0, 0, 1);
        rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.rot.y));
        rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.rot.x));
        for (int k = 0; k < 4; ++k) {
            v[k].rotate(rotation);
            v[k].scale(entity.scale);
        }
        MatrixStack.Entry entry = matrices.peek();
        vc.vertex(entry.getPositionMatrix(), v[0].getX(), v[0].getY(), v[0].getZ()).texture(1, 1).next();
        vc.vertex(entry.getPositionMatrix(), v[1].getX(), v[1].getY(), v[1].getZ()).texture(1, 0).next();
        vc.vertex(entry.getPositionMatrix(), v[2].getX(), v[2].getY(), v[2].getZ()).texture(0, 0).next();
        vc.vertex(entry.getPositionMatrix(), v[3].getX(), v[3].getY(), v[3].getZ()).texture(0, 1).next();
        vc.vertex(entry.getPositionMatrix(), v[3].getX(), v[3].getY(), v[3].getZ()).texture(0, 1).next();
        vc.vertex(entry.getPositionMatrix(), v[2].getX(), v[2].getY(), v[2].getZ()).texture(0, 0).next();
        vc.vertex(entry.getPositionMatrix(), v[1].getX(), v[1].getY(), v[1].getZ()).texture(1, 0).next();
        vc.vertex(entry.getPositionMatrix(), v[0].getX(), v[0].getY(), v[0].getZ()).texture(1, 1).next();
        matrices.pop();
    }
}
