package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.magical.LuminInjectorEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;

public class LuminInjectorBlockEnityRenderer implements BlockEntityRenderer<LuminInjectorEntity> {

    private ItemRenderer INSTANCE;

    public LuminInjectorBlockEnityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getItemRenderer();
    }

    @Override
    public void render(LuminInjectorEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        if (entity.crafting && entity.itemStacks.size() == entity.shift.size()) {
            double offset = 1 - Math.sqrt((double) (entity.progress - 200) / 100);

            for (int i = 0; i < entity.itemStacks.size(); i++) {
                matrices.push();
                matrices.translate(0.5 + offset * entity.shift.get(i).x, 0.1, 0.5 + offset * entity.shift.get(i).y);
                INSTANCE.renderItem(entity.itemStacks.get(i), ModelTransformation.Mode.GROUND, 15728880, overlay,
                        matrices, vertexConsumers, 0);
                matrices.pop();
            }
        }
        matrices.pop();

    }
}
