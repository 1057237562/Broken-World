package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.DimensionRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Redirect(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;"))
    public CloudRenderMode hasCloud(GameOptions instance){
        if(DimensionRegister.noCloudDimension.contains(this.client.world.getDimensionKey().getValue().toTranslationKey())){
            return CloudRenderMode.OFF;
        }else{
            return instance.getCloudRenderModeValue();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderScannedBlock(CallbackInfo ci) {
        VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();
    }
}
