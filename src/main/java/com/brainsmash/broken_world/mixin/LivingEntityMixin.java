package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.Main;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

import static dev.emi.trinkets.api.TrinketsApi.getTrinketComponent;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Redirect(method = "baseTick",at = @At(value="INVOKE",target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean hasAir(LivingEntity instance, TagKey<Fluid> tagKey){
        if(instance instanceof PlayerEntity) {
            if (Main.noAirDimension.contains(instance.world.getDimensionKey().getValue().toTranslationKey())) {
                return true;
            }
        }
        return instance.isSubmergedIn(tagKey);
    }

    @ModifyConstant(method = "travel",constant = @Constant(doubleValue = 0.08))
    private double getGravity(double earthGravity){
        double multiplier = 1.0;
        if(Main.dimensionGravity.containsKey(world.getDimensionKey().getValue().toTranslationKey())){
            multiplier = Main.dimensionGravity.get(world.getDimensionKey().getValue().toTranslationKey());
        }
        return earthGravity * multiplier;
    }

    @Redirect(method = "handleFallDamage",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"))
    public int computeFallDamage(LivingEntity instance, float fallDistance, float damageMultiplier) {
        StatusEffectInstance statusEffectInstance = instance.getStatusEffect(StatusEffects.JUMP_BOOST);
        double multiplier = 1.0;
        if(Main.dimensionGravity.containsKey(world.getDimensionKey().getValue().toTranslationKey())){
            multiplier = Main.dimensionGravity.get(world.getDimensionKey().getValue().toTranslationKey());
        }
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance - (3.0f + f) / multiplier) * damageMultiplier);
    }
}
