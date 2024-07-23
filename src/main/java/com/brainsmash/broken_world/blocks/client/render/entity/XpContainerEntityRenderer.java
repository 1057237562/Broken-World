package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.client.render.FluidRenderFace;
import com.brainsmash.broken_world.blocks.client.render.FluidVariantRenderer;
import com.brainsmash.broken_world.blocks.entity.magical.XpContainerEntity;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class XpContainerEntityRenderer<T extends XpContainerEntity> implements BlockEntityRenderer<T> {
    private final BlockRenderManager manager;

    public XpContainerEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        manager = ctx.getRenderManager();
    }

    @Override
    public void render(XpContainerEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        List<FluidRenderFace> faces = new ArrayList<>();

        double x0 = 0.126;
        double y0 = 0.001;
        double z0 = 0.126;
        double x1 = 0.874;
        double y1 = 0.001 + (12 / 16.0 - 0.002) * entity.xpStorage.amount / (float) entity.xpStorage.getCapacity();
        double z1 = 0.874;

        EnumSet<Direction> sides = EnumSet.allOf(Direction.class);
        FluidRenderFace.appendCuboid(x0, y0, z0, x1, y1, z1, 1, sides, faces);
        for (FluidRenderFace face : faces) {
            face.light = light;
        }
        FluidVariantRenderer.INSTANCE.render(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)), faces,
                vertexConsumers, matrices);
        matrices.pop();
    }
}
