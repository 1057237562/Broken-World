package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.weapons.guns.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderCrosshair(DrawContext matrices);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void checkRenderCrosshair(InGameHud instance, DrawContext matrices) {
        if (client.player.isUsingItem() && client.player.getActiveItem().getItem() instanceof GunItem) {
            return;
        }
        renderCrosshair(matrices);
    }


}
