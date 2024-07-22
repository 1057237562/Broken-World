package com.brainsmash.broken_world.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ExperiencedEnchantment extends Enchantment {
    private static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public ExperiencedEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, ALL_ARMOR);
    }


    @Override
    public int getMaxLevel() {
        return 4;
    }

    public static float getExperienceBonus(EquipmentSlot slot, int level) {
        return switch (slot) {
            case HEAD, FEET -> level * 0.125f * 0.15f;
            case CHEST -> level * 0.125f * 0.20f;
            case LEGS -> level * 0.125f * 0.30f;
            default -> 0.0f;
        };
    }
}