package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.blocks.entity.magical.DimInfuserEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

public class DimInfuserBlockEnityRenderer implements BlockEntityRenderer<DimInfuserEntity> {

    private ItemRenderer INSTANCE;

    public DimInfuserBlockEnityRenderer(BlockEntityRendererFactory.Context ctx) {
        INSTANCE = ctx.getItemRenderer();
    }

    @Override
    public void render(DimInfuserEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        entity.tick += tickDelta;
        if (entity.crafting && entity.vitemStacks.size() == entity.shift.size()) {
            double offset = 1 - Math.sqrt((double) (entity.progress - 200) / 200);

            for (int i = 0; i < entity.vitemStacks.size(); i++) {
                matrices.push();
                matrices.translate(0.5 + offset * entity.shift.get(i).x, 0.1, 0.5 + offset * entity.shift.get(i).y);
                matrices.multiply(Quaternion.fromEulerXyz(0, entity.tick / 64.0f, 0));
                INSTANCE.renderItem(entity.vitemStacks.get(i), ModelTransformation.Mode.GROUND, 15728880, overlay,
                        matrices, vertexConsumers, 0);
                matrices.pop();
            }
        }
        matrices.pop();

    }
}
