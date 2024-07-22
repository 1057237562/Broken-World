package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.enchantment.ExperiencedEnchantment;
import com.brainsmash.broken_world.registry.EnchantmentRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {

    public static float getExperienceBonusRatio(PlayerEntity player) {
        float ratio = 1.0f;
        Map<EquipmentSlot, ItemStack> equipments = EnchantmentRegister.EXPERIENCED.getEquipment(player);
        for (EquipmentSlot slot : equipments.keySet()) {
            ItemStack stack = equipments.get(slot);
            int level = EnchantmentHelper.getLevel(EnchantmentRegister.EXPERIENCED, stack);
            ratio += ExperiencedEnchantment.getExperienceBonus(slot, level);
        }
        return ratio;
    }

    @Shadow
    private int amount;

    @Shadow
    private World world;

    @Inject(at = @At("HEAD"), method = "onPlayerCollision")
    private void onPlayerCollisionHead(PlayerEntity player, CallbackInfo info) {
        if (!this.world.isClient) {
            float ratio = getExperienceBonusRatio(player);
            System.out.println("HEAD: old amount = " + this.amount + ", ratio = " + ratio);
            this.amount = Math.round(this.amount * ratio);
            System.out.println("HEAD: new amount " + this.amount);
        }
    }

    @Inject(at = @At("TAIL"), method = "onPlayerCollision")
    private void onPlayerCollisionTail(PlayerEntity player, CallbackInfo info) {
        if (!this.world.isClient) {
            float ratio = getExperienceBonusRatio(player);
            System.out.println("TAIL: old amount = " + this.amount + ", ratio = " + ratio);
            this.amount = Math.round(this.amount / ratio);
            System.out.println("TAIL: new amount " + this.amount);
        }
    }
}