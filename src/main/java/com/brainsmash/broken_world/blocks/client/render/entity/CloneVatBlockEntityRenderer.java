package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.CloneVatBlockEntity;
import com.brainsmash.broken_world.entity.VatPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class CloneVatBlockEntityRenderer implements BlockEntityRenderer<CloneVatBlockEntity> {
    private final EntityRenderDispatcher DISPATCHER;

    public CloneVatBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        DISPATCHER = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(CloneVatBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VatPlayerEntity playerEntity = entity.getPlayerEntity();
        matrices.push();
        matrices.translate(0.5d, 0d, 0.5d);
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        matrices.multiply(direction.getRotationQuaternion());
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
        DISPATCHER.render(playerEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, 15728640);
        matrices.pop();
    }
}
