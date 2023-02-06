package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.weapons.guns.GunBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
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
    protected abstract void renderCrosshair(MatrixStack matrices);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void checkRenderCrosshair(InGameHud instance, MatrixStack matrices) {
        if (client.player.isUsingItem() && client.player.getActiveItem().getItem() instanceof GunBase) {
            return;
        }
        renderCrosshair(matrices);
    }
}
