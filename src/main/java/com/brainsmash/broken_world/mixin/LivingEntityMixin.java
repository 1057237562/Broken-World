package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.enchantment.ExperiencedEnchantment;
import com.brainsmash.broken_world.items.armor.material.ArmorMaterialWithSetBonus;
import com.brainsmash.broken_world.registry.DimensionRegister;
import com.brainsmash.broken_world.registry.EnchantmentRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow
    public float airStrafingSpeed;

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract ItemStack getActiveItem();

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean hasNoAir(LivingEntity instance, TagKey<Fluid> tagKey) {
        if (TrinketsApi.getTrinketComponent(instance).get().isEquipped(ItemRegister.items[2])) {
            return false;
        }
        if (instance instanceof PlayerEntity) {
            if (DimensionRegister.noAirDimension.contains(
                    instance.world.getDimensionKey().getValue().toTranslationKey())) {
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
        double multiplier = DimensionRegister.dimensionGravity.getOrDefault(
                world.getDimensionKey().getValue().toTranslationKey(), 1.0);

        float f = statusEffectInstance == null ? 0.0f : (float) (statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance * Math.sqrt(multiplier) - 3.0f - f) * damageMultiplier);
    }

    @Inject(method = "onEquipStack", at = @At("HEAD"))
    public void resetSetBonus(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if (getData() instanceof NbtCompound nbtCompound) {
            nbtCompound.remove("bonus");
            setData(nbtCompound);
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

    @Shadow
    public abstract boolean shouldAlwaysDropXp();

    @Shadow
    public int playerHitTimer;

    @Inject(method = "drop", at = @At("TAIL"))
    public void applySoulLeechingExpBonus(DamageSource damageSource, CallbackInfo info) {
        Entity source = damageSource.getSource();
        if (!(damageSource instanceof EntityDamageSource) || source == null) {
            return;
        }
        ItemStack weaponMainHand = null;
        for (ItemStack itemStack : source.getHandItems()) {
            weaponMainHand = itemStack;
            break;
        }
        if (weaponMainHand == null) {
            return;
        }
        int level = EnchantmentHelper.getLevel(EnchantmentRegister.EXPERIENCED, weaponMainHand);
        float bonusRatio = ExperiencedEnchantment.getExperienceBonus(level);
        if (bonusRatio == 0.0f)
            return;
        LivingEntity zis = (LivingEntity) (Object) this;
        float bonus = zis.getXpToDrop() * bonusRatio;
        int bonusRounded = Math.round(bonus);
        if (zis.world instanceof ServerWorld && !zis.isExperienceDroppingDisabled() && (this.shouldAlwaysDropXp() || this.playerHitTimer > 0 && zis.shouldDropXp() && zis.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
            ExperienceOrbEntity.spawn((ServerWorld) zis.world, zis.getPos(), bonusRounded);
            if (Math.random() < (bonus - bonusRounded)) {
                ExperienceOrbEntity.spawn((ServerWorld) zis.world, zis.getPos(), 1);
            }
        }
    }
}
