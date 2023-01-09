package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import com.brainsmash.broken_world.registry.DimensionRegister;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    /*@ModifyArgs(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
    public void drawOutline(Args args){

        if(args.get(0) instanceof ScannerBlockEntity) {
            System.out.println(((BlockEntity)args.get(0)).getPos());
            args.set(3, bufferBuilders.getOutlineVertexConsumers());
        }
    }*/

    @Redirect(method = "render",at =@At(value = "INVOKE",target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V"))
    public void Redirect(ShaderEffect instance, float tickDelta){

    }

}
