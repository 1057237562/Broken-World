package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.armor.material.ArmorMaterialWithSetBonus;
import com.brainsmash.broken_world.registry.DimensionRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract ItemStack getActiveItem();

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean hasNoAir(LivingEntity instance, TagKey<Fluid> tagKey) {
        if (TrinketsApi.getTrinketComponent(instance).get().isEquipped(ItemRegister.items[2])) {
            return false;
        }
        if (instance instanceof PlayerEntity) {
            if (DimensionRegister.noAirDimension.contains(
                    instance.getWorld().getDimensionKey().getValue().toTranslationKey())) {
                return true;
            }
        }
        return instance.isSubmergedIn(tagKey);
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    private double getGravity(double earthGravity) {
        double multiplier = 1.0;
        if (DimensionRegister.dimensionGravity.containsKey(
                getWorld().getDimensionKey().getValue().toTranslationKey())) {
            multiplier = DimensionRegister.dimensionGravity.get(
                    getWorld().getDimensionKey().getValue().toTranslationKey());
        }
        return earthGravity * multiplier;
    }

    @Redirect(method = "handleFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"))
    public int computeFallDamage(LivingEntity instance, float fallDistance, float damageMultiplier) {
        StatusEffectInstance statusEffectInstance = instance.getStatusEffect(StatusEffects.JUMP_BOOST);
        double multiplier = DimensionRegister.dimensionGravity.getOrDefault(
                getWorld().getDimensionKey().getValue().toTranslationKey(), 1.0);

        float f = statusEffectInstance == null ? 0.0f : (float) (statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance * Math.sqrt(multiplier) - 3.0f - f) * damageMultiplier);
    }

    @Inject(method = "onEquipStack", at = @At("HEAD"))
    public void resetSetBonus(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (brokenWorld$getData() instanceof NbtCompound nbtCompound) {
            nbtCompound.remove("bonus");
            brokenWorld$setData(nbtCompound);
        }
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            if (newStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getMaterial() instanceof ArmorMaterialWithSetBonus material) {
                    boolean flag = true;
                    for (ItemStack equipment : getArmorItems()) {
                        if (!(equipment.getItem() instanceof ArmorItem item && item.getMaterial().getClass().equals(
                                material.getClass()))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        material.processSetBonus(this);
                    }
                }
            }
        }
    }
}
