package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.magical.MortarBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public class MortarBlockEnityRenderer implements BlockEntityRenderer<MortarBlockEntity> {

    private ItemRenderer INSTANCE;

    public MortarBlockEnityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getItemRenderer();
    }

    @Override
    public void render(MortarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        if (entity.grindTime == 0) {
            matrices.translate(0.5, 0.2, 0.5);
        } else {
            matrices.translate(0.5, 0.22, 0.5);
        }
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(
                new Quaternionf().rotateXYZ(0, (float) entity.grindTime / entity.MAX_GRIND_TIME * 2 * (float) Math.PI,
                        0));
        matrices.multiply(new Quaternionf().rotateXYZ((float) (Math.PI / 2), 0, 0));
        INSTANCE.renderItem(entity.getGrindItem(), ModelTransformationMode.FIXED, light, overlay, matrices,
                vertexConsumers, entity.getWorld(), 0);
        matrices.pop();

        matrices.push();
        if (entity.grindTime == 0) {
            matrices.translate(0.5, 0.22, 0.5);
        } else {
            matrices.translate(0.5, 0.2, 0.5);
        }
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(new Quaternionf().rotateXYZ((float) (Math.PI / 2), 0, 0));
        INSTANCE.renderItem(entity.getOutputItem(), ModelTransformationMode.FIXED, light, overlay, matrices,
                vertexConsumers, entity.getWorld(), 0);
        matrices.pop();

    }
}
