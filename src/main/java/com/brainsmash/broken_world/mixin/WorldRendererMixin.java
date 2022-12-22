package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;"))
    public CloudRenderMode hasCloud(GameOptions instance){
        if(Main.noCloudDimension.contains(this.client.world.getDimensionKey().getValue().toTranslationKey())){
            return CloudRenderMode.OFF;
        }else{
            return instance.getCloudRenderModeValue();
        }
    }
}
