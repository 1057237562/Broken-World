package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.registry.DimensionRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean hasNoAir(LivingEntity instance, TagKey<Fluid> tagKey) {
        if (TrinketsApi.getTrinketComponent(instance).get().isEquipped(ItemRegister.items[2])) {
            return false;
        }
        if (instance instanceof PlayerEntity) {
            if (DimensionRegister.noAirDimension.contains(instance.world.getDimensionKey().getValue().toTranslationKey())) {
                return true;
            }
        }
        return instance.isSubmergedIn(tagKey);
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    private double getGravity(double earthGravity) {
        double multiplier = 1.0;
        if (DimensionRegister.dimensionGravity.containsKey(world.getDimensionKey().getValue().toTranslationKey())) {
            multiplier = DimensionRegister.dimensionGravity.get(world.getDimensionKey().getValue().toTranslationKey());
        }
        return earthGravity * multiplier;
    }

    @Redirect(method = "handleFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"))
    public int computeFallDamage(LivingEntity instance, float fallDistance, float damageMultiplier) {
        StatusEffectInstance statusEffectInstance = instance.getStatusEffect(StatusEffects.JUMP_BOOST);
        double multiplier = DimensionRegister.dimensionGravity.getOrDefault(world.getDimensionKey().getValue().toTranslationKey(),
                1.0);

        float f = statusEffectInstance == null ? 0.0f : (float) (statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance * Math.sqrt(multiplier) - 3.0f - f) * damageMultiplier);
    }
}
