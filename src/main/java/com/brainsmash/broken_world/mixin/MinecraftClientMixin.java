package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.weapons.guns.GunBase;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.brainsmash.broken_world.Main.MODID;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "HEAD"), cancellable = true)
    public void fireGun(CallbackInfoReturnable<Boolean> cir) {
        boolean flag = false;
        for (ItemStack stack : player.getHandItems()) {
            if (stack.getItem() instanceof GunBase gunBase) {
                gunBase.fire(player.world, player);
                flag = true;
            }
        }
        if (flag) {
            ClientPlayNetworking.send(new Identifier(MODID, "fire_key_pressed"), PacketByteBufs.create());
            cir.setReturnValue(true);
        }
    }
}
