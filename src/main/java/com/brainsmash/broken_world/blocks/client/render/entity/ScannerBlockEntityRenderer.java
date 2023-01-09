package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MeshBuilderImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.brainsmash.broken_world.Main.MODID;

public class ScannerBlockEntityRenderer implements BlockEntityRenderer<ScannerBlockEntity> {

    private BlockRenderManager INSTANCE;

    public ScannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        INSTANCE = ctx.getRenderManager();
    }

    @Override
    public boolean rendersOutsideBoundingBox(ScannerBlockEntity blockEntity) {
        return true;
    }

    public static void drawBox(MatrixStack matrices, BufferBuilder bufferBuilder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        drawBox(matrices, bufferBuilder, x1, y1, z1, x2, y2, z2, red, green, blue, alpha, red, green, blue);
    }

    public static void drawBox(MatrixStack matrices, BufferBuilder bufferBuilder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha, float xAxisRed, float yAxisGreen, float zAxisBlue) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        Matrix3f matrix3f = matrices.peek().getNormalMatrix();
        float f = (float)x1;
        float g = (float)y1;
        float h = (float)z1;
        float i = (float)x2;
        float j = (float)y2;
        float k = (float)z2;
        bufferBuilder.vertex(matrix4f, f, g, h).color(red, yAxisGreen, zAxisBlue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, h).color(red, yAxisGreen, zAxisBlue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, g, h).color(xAxisRed, green, zAxisBlue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, h).color(xAxisRed, green, zAxisBlue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, g, h).color(xAxisRed, yAxisGreen, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f, f, g, k).color(xAxisRed, yAxisGreen, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, h).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, h).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, h).color(red, green, blue, alpha).normal(matrix3f, -1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, h).color(red, green, blue, alpha).normal(matrix3f, -1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, h).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, f, g, k).color(red, green, blue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, -1.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, h).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, -1.0f).next();
        bufferBuilder.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 1.0f, 0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, h).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0f, 0.0f, 1.0f).next();
    }

    @Override
    public void render(ScannerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        float scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        float f;
        float g = f = (float)Math.min(scaledWidth, scaledHeight);
        float h = Math.min((float)scaledWidth / f, (float)scaledHeight / g) * 1;
        float i = f * h;
        float j = g * h;
        float k = ((float)scaledWidth - i) / 2.0f;
        float l = ((float)scaledHeight - j) / 2.0f;
        float m = k + i;
        float n = l + j;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0, scaledHeight, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0, l, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, l, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, 0.0, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(k, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(k, l, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(0.0, l, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(m, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, n, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(scaledWidth, l, -90.0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(m, l, -90.0).color(0, 0, 0, 255).next();
        tessellator.draw();
        RenderSystem.enableTexture();
        /*matrices.push();
        for(BlockPos pos : entity.scanned){
            drawBox(matrices,buffer,pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1,1f,0f,0f,1f);
        }
        matrices.pop();*/
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
