package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.blocks.entity.electric.ScannerBlockEntity;
import com.brainsmash.broken_world.registry.DimensionRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.brainsmash.broken_world.Main.MODID;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow private @Nullable ShaderEffect entityOutlineShader;
    private boolean shading = true;

    @Redirect(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;"))
    public CloudRenderMode hasCloud(GameOptions instance){
        if(DimensionRegister.noCloudDimension.contains(this.client.world.getDimensionKey().getValue().toTranslationKey())){
            return CloudRenderMode.OFF;
        }else{
            return instance.getCloudRenderModeValue();
        }
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/world/ClientWorld;method_23787(F)F"))
    public float drawStars(ClientWorld instance, float f){
        if(DimensionRegister.noCloudDimension.contains(this.client.world.getDimensionKey().getValue().toTranslationKey())){
            return 0.5f;
        }else {
            return instance.method_23787(f);
        }
    }

    @ModifyArg(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",ordinal = 1),index = 1)
    public Identifier hasMoon(Identifier id){
        if(DimensionRegister.noMoonDimension.contains(this.client.world.getDimensionKey().getValue().toTranslationKey())){
            return new Identifier(MODID,"textures/environment/no_moon.png");
        }else {
            return id;
        }
    }

    @ModifyArgs(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",ordinal = 1))
    public void drawOutline(Args args){
        if(args.get(0) instanceof ScannerBlockEntity) {
            OutlineVertexConsumerProvider outlineVertexConsumerProvider = this.bufferBuilders.getOutlineVertexConsumers();
            outlineVertexConsumerProvider.setColor(255, 255, 255, 255);
            args.set(3, outlineVertexConsumerProvider);
            shading = false;
        }
    }

    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gl/ShaderEffect;render(F)V",ordinal = 0))
    public void enabledShader(CallbackInfo cbi){
        shading = true;
    }

    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",ordinal = 12))
    public void shouldEnableShader(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci){
        if(!shading) {
            entityOutlineShader.render(tickDelta);
            client.getFramebuffer().beginWrite(false);
        }
    }
}
