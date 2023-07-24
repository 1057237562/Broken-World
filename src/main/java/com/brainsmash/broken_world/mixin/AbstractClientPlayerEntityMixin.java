package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.weapons.guns.SniperRifle;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends LivingEntityMixin {

    @Inject(method = "getFovMultiplier", at = @At("HEAD"), cancellable = true)
    public void fovMultiplier(CallbackInfoReturnable<Float> cir) {
        if (isUsingItem()) {
            if (getActiveItem().getItem() instanceof SniperRifle) {
                cir.setReturnValue(0.5f);
                cir.cancel();
            }
        }
    }
}
