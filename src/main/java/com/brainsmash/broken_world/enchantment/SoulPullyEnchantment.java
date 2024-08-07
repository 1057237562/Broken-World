package com.brainsmash.broken_world.enchantment;

import com.brainsmash.broken_world.registry.EnchantmentRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SoulPullyEnchantment extends Enchantment {
    private static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public SoulPullyEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, ALL_ARMOR);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    public static double getExperienceOrbAttractionRadiusBonusRatio(PlayerEntity player) {
        double bonus = 0.0;
        for (ItemStack stack : player.getArmorItems()) {
            bonus += EnchantmentHelper.getLevel(EnchantmentRegister.SOUL_PULLY, stack) * 0.125;
        }
        return bonus;
    }
}
