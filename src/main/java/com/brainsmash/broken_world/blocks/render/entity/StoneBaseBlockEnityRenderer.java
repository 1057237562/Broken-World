package com.brainsmash.broken_world.blocks.render.entity;

import com.brainsmash.broken_world.blocks.entity.magical.StoneBaseBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

public class StoneBaseBlockEnityRenderer implements BlockEntityRenderer<StoneBaseBlockEntity> {

    private ItemRenderer INSTANCE;

    public StoneBaseBlockEnityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getItemRenderer();
    }

    @Override
    public void render(StoneBaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        entity.tick += tickDelta;
        double offset = Math.sin(entity.tick / 32.0) / 8.0;
        if (entity.progress != 0) {
            offset = (double) entity.progress / entity.maxProgress;
        }
        matrices.translate(0.5, (entity.isBlack ? -0.5 : 1.25) + (entity.isBlack ? -offset : offset), 0.5);
        matrices.multiply(Quaternion.fromEulerXyz(0, entity.tick / 64.0f, 0));
        INSTANCE.renderItem(entity.itemStack, ModelTransformation.Mode.GROUND, 15728880, overlay, matrices,
                vertexConsumers, 0);
        matrices.pop();

    }
}
