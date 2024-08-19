package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.SpellEntity;
import com.brainsmash.broken_world.util.MathHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

import static com.brainsmash.broken_world.Main.MODID;

public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public SpellEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SpellEntity entity) {
        return new Identifier(MODID, "textures/entity/magic_spell.png");
    }

    @Override
    public void render(SpellEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.active) {
            matrices.push();
            matrices.translate(entity.shift.x, entity.shift.y, entity.shift.z);
            renderLines(vertexConsumers.getBuffer(RenderLayer.getLines()), matrices, entity, false);

            //MinecraftClient.getInstance().getTextureManager().bindTexture(getTexture(entity));
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, getTexture(entity));
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.DST_ALPHA);
            RenderSystem.enableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            renderSpell(bufferBuilder, matrices, entity, light);
            renderBeam(entity, matrices, vertexConsumers, tickDelta, entity.world.getTime());
            tessellator.draw();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        } else if (entity.getOwner() != null && entity.normal != null) {
            matrices.push();
            entity.shift = entity.getOwner().getEyePos().subtract(entity.normal.negate().add(entity.getPos()));
            matrices.translate(entity.shift.x, entity.shift.y, entity.shift.z);
            renderLines(vertexConsumers.getBuffer(RenderLayer.getLines()), matrices, entity, true);

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

    private void renderLines(VertexConsumer vc, MatrixStack matrices, SpellEntity entity, boolean last) {
        MatrixStack.Entry entry = matrices.peek();
        Quaternion rotateForward = new Quaternion(0, 0, 0, 1);
        rotateForward.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.rot.y));
        rotateForward.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.rot.x));
        Quaternion rotateBack = rotateForward.copy();
        rotateBack.conjugate();

        double u = entity.normal.negate().dotProduct(entity.normal) / entity.normal.negate().dotProduct(
                entity.getOwner().getRotationVector());
        Vec3f vec3f = new Vec3f(entity.getOwner().getRotationVector().multiply(u).add(entity.normal.negate()));
        Vec3f vec3f1 = vec3f.copy();
        vec3f1.rotate(rotateBack);

        if (new Vec3d(vec3f1).length() < 1.25 && last) {
            Vec3f lastDegree = getOctantByPhase(entity.seq.get(entity.seq.size() - 1), rotateForward);
            vc.vertex(entry.getPositionMatrix(), lastDegree.getX(), lastDegree.getY(), lastDegree.getZ()).color(255,
                    255, 0, 200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
            vc.vertex(entry.getPositionMatrix(), vec3f.getX(), vec3f.getY(), vec3f.getZ()).color(255, 255, 0,
                    200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
        }
        for (int i = 0; i < entity.seq.size() - 1; i++) {
            Vec3f octant = getOctantByPhase(entity.seq.get(i), rotateForward);
            vc.vertex(entry.getPositionMatrix(), octant.getX(), octant.getY(), octant.getZ()).color(255, 255, 0,
                    200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
            octant = getOctantByPhase(entity.seq.get(i + 1), rotateForward);
            vc.vertex(entry.getPositionMatrix(), octant.getX(), octant.getY(), octant.getZ()).color(255, 255, 0,
                    200).normal(entry.getNormalMatrix(), 0, 0, 0).next();
        }

    }

    private Vec3f getOctantByPhase(int phase, Quaternion rot) {
        if (phase == -1) return new Vec3f(0, 0, 0);
        int degree = phase * 45;
        Vec3f origin = new Vec3f(-MathHelper.cos(MathHelper.toRadians(degree)),
                MathHelper.sin(MathHelper.toRadians(-degree)), 0);
        origin.rotate(rot);
        return origin;
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

    private static void renderBeam(SpellEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime) {
        matrices.push();
        Quaternion rotation = new Quaternion(0, 0, 0, 1);
        rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.rot.y));
        rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.rot.x + 90));
        matrices.multiply(rotation);
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, worldTime, 0, 128, new float[]{
                1.0f,
                1.0f,
                0.0f
        }, 0.8F, 1.0F);
        matrices.pop();
    }

    public static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius) {
        int i = yOffset + maxY;
        matrices.push();
//        matrices.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = net.minecraft.util.math.MathHelper.fractionalPart(
                g * 0.2F - (float) net.minecraft.util.math.MathHelper.floor(g * 0.1F));
        float j = color[0];
        float k = color[1];
        float l = color[2];
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * 2.25F - 45.0F));
        float m = 0.0F;
        float p = 0.0F;
        float q = -innerRadius;
        float r = 0.0F;
        float s = 0.0F;
        float t = -innerRadius;
        float u = 0.0F;
        float v = 1.0F;
        float w = -1.0F + h;
        float x = (float) maxY * heightScale * (0.5F / innerRadius) + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)), j, k, l, 1.0F,
                yOffset, i, 0.0F, innerRadius, innerRadius, 0.0F, q, 0.0F, 0.0F, t, 0.0F, 1.0F, x, w);
        matrices.pop();
        m = -outerRadius;
        float n = -outerRadius;
        p = -outerRadius;
        q = -outerRadius;
        u = 0.0F;
        v = 1.0F;
        w = -1.0F + h;
        x = (float) maxY * heightScale + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)), j, k, l,
                0.125F, yOffset, i, m, n, outerRadius, p, q, outerRadius, outerRadius, outerRadius, 0.0F, 1.0F, x, w);
        matrices.pop();
    }

    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2,
                v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2,
                v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2,
                v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2,
                v1, v2);
    }

    private static void renderBeamFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    /**
     * @param v the top-most coordinate of the texture region
     * @param u the left-most coordinate of the texture region
     */
    private static void renderBeamVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, (float) y, z).color(red, green, blue, alpha).texture(u, v).overlay(
                OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0.0F,
                1.0F, 0.0F).next();
    }
}
