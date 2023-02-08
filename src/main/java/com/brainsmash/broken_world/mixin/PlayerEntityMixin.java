package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}
