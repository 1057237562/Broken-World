package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.electric.SifterBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Quaternionf;

public class SifterBlockEntityRenderer implements BlockEntityRenderer<SifterBlockEntity> {

    private ItemRenderer INSTANCE;

    public SifterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getItemRenderer();
    }

    @Override
    public void render(SifterBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(new Quaternionf().rotateXYZ(0, entity.getWorld().getTime() + tickDelta, 0));
        ItemStack itemStack = entity.getItems().get(0);
        if (entity.isRunning())
            INSTANCE.renderItem(itemStack, ModelTransformationMode.GROUND, 15728880, overlay, matrices, vertexConsumers,
                    entity.getWorld(), 0);
        matrices.pop();
    }
}
