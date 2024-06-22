package com.brainsmash.broken_world.mixin;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SodiumWorldRenderer.class)
public class SodiumRendererMixin {

    OutlineVertexConsumerProvider outlineVertexConsumerProvider;

    @ModifyVariable(method = "renderBlockEntities(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/BufferBuilderStorage;Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;Lnet/minecraft/client/render/Camera;F)V", at = @At(value = "HEAD"))
    public BufferBuilderStorage captureConsumerProvider(BufferBuilderStorage bufferBuilders) {
        outlineVertexConsumerProvider = bufferBuilders.getOutlineVertexConsumers();
        return bufferBuilders;
    }

//    @ModifyArgs(method = "renderBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
//    public void drawOutLine(Args args) {
//        if (args.get(0) instanceof ScannerBlockEntity) {
//            args.set(3, outlineVertexConsumerProvider);
//            shading = false;
//        }
//    }
}
