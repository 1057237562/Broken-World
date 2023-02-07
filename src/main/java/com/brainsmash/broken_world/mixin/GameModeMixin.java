package com.brainsmash.broken_world.mixin;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMode.class)
public class GameModeMixin {

    @Inject(method = "setAbilities", at = @At("HEAD"), cancellable = true)
    public void settingAbilities(PlayerAbilities abilities, CallbackInfo ci) {
        System.out.println("settings Gamemode profile");
    }
}
