package com.brainsmash.broken_world.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ExperiencedEnchantment extends Enchantment {

    public ExperiencedEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        if (level != 4) {
            return super.getMinPower(level);
        }
        return 60;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    public static float getExperienceBonus(int level) {
        return switch (level) {
            case 1 -> 0.08f;
            case 2 -> 0.16f;
            case 3 -> 0.28f;
            case 4 -> 0.40f;
            default -> 0.0f;
        };
    }
}