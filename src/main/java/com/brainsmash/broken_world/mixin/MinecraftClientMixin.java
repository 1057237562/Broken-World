package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.weapons.guns.GunBase;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.brainsmash.broken_world.Main.MODID;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Inject(method = "doAttack", at = @At(value = "HEAD"), cancellable = true)
    private void fireGun(CallbackInfoReturnable<Boolean> cir) {
        if (player.getMainHandStack().getItem() instanceof GunBase gunBase) {
            gunBase.fire(player.world, player);
            ClientPlayNetworking.send(new Identifier(MODID, "fire_key_pressed"), PacketByteBufs.create());
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "handleInputEvents", at = @At("STORE"), ordinal = 0)
    private boolean fireGunTick(boolean flag) {
        if (options.attackKey.isPressed()) {
            if (player.getMainHandStack().getItem() instanceof GunBase gunBase) {
                flag |= gunBase.fireTick(player.world, MinecraftClient.getInstance().player);
            }
            ClientPlayNetworking.send(new Identifier(MODID, "fire_key_hold"), PacketByteBufs.create());
        }
        return flag;
    }

    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
    private boolean handleUsingItem(ClientPlayerEntity instance) {
        if (instance.getMainHandStack().getItem() instanceof GunBase) {
            if (!options.useKey.isPressed()) {
                interactionManager.stopUsingItem(player);
            }
            return false;
        }
        return instance.isUsingItem();
    }

}
