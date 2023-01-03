package com.brainsmash.broken_world.client.render.block.entity;

import com.brainsmash.broken_world.blocks.entity.electric.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.CreativeBatteryBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Quaternion;

import java.util.Dictionary;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CreativeBatteryBlockEntityRenderer implements BlockEntityRenderer<CreativeBatteryBlockEntity> {

    private final EntityRenderDispatcher DISPATCHER;

    public CreativeBatteryBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        DISPATCHER = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(CreativeBatteryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Entity chargedCreeper = entity.getCreeper();

        matrices.push();
        matrices.translate(0.5d, 0.0d, 0.5d);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(Quaternion.fromEulerXyz(0, (float) ((chargedCreeper.age + tickDelta)*2*Math.PI/180.0),0));
        DISPATCHER.render(chargedCreeper, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, 15728640);
        matrices.pop();
    }
}
