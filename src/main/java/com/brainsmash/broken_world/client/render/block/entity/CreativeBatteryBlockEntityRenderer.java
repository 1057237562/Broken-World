package com.brainsmash.broken_world.client.render.block.entity;

import com.brainsmash.broken_world.blocks.entity.electric.BatteryBlockEntity;
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

import java.util.Dictionary;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CreativeBatteryBlockEntityRenderer implements BlockEntityRenderer<BatteryBlockEntity> {

    private final EntityRenderDispatcher DISPATCHER;
    private Entity chargedCreeper;
    private float tick = 0;

    public CreativeBatteryBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        DISPATCHER = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(BatteryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(chargedCreeper == null) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", "creeper");
            nbt.putBoolean("powered", true);
            chargedCreeper = EntityType.loadEntityWithPassengers(nbt, entity.getWorld(), Function.identity());
        }

        matrices.push();
        matrices.translate(0.5d, 0.0d, 0.5d);
        matrices.scale(0.5f, 0.5f, 0.5f);
        tick += 0.5*tickDelta;
        DISPATCHER.render(chargedCreeper, 0.0, 0.0, 0.0, 0.0f, tick, matrices, vertexConsumers, 15728640);
        matrices.pop();
    }
}
