package com.brainsmash.broken_world.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

public class ExperiencedEnchantment extends Enchantment {

    public ExperiencedEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        super.onTargetDamaged(user, target, level);
        if (!target.isAlive()) {
            if (target instanceof MobEntity mob && target.world instanceof ServerWorld serverWorld) {
                ExperienceOrbEntity.spawn(serverWorld, mob.getPos(),
                        (int) Math.round(mob.getXpToDrop() * Math.pow(1.5f, level)));
            }
        }
    }
}