package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.util.BonusHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements PlayerDataExtension {

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Redirect(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPose(Lnet/minecraft/entity/EntityPose;)V"))
    private void checkCrawlKey(PlayerEntity instance, EntityPose entityPose) {
        boolean crawling = getFlag(2);
        instance.setPose(crawling ? EntityPose.SWIMMING : entityPose);
        setFlag(2, false);
    }

    @Override
    public void forceSetFlag(int index, boolean value) {
        setFlag(index, value);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V"))
    public void setAirStrafingSpeed(CallbackInfo ci) {
        if (this.getData() instanceof NbtCompound nbtCompound) {
            if (BonusHelper.getBoolean(nbtCompound, "jet")) {
                //airStrafingSpeed *= 1.5f;
            }
        }
    }

}
