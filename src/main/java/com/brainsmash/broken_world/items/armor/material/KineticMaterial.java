package com.brainsmash.broken_world.items.armor.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class KineticMaterial implements ArmorMaterialWithSetBonus {


    private static final int[] BASE_DURABILITY = new int[]{
            13,
            15,
            16,
            11
    };
    private static final int[] PROTECTION_VALUES = new int[]{
            3,
            6,
            8,
            3
    };

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * 100;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return PROTECTION_VALUES[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 4;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_TURTLE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        return "kinetic";
    }

    @Override
    public float getToughness() {
        return 3.0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.5f;
    }

    @Override
    public void processSetBonus(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            player.getAbilities().allowFlying = true;
        }
        System.out.println(entity + ":" + "has Set bonus");
    }

    @Override
    public void processSetBonusOnPlayer(PlayerEntity player) {
        player.getAbilities().allowFlying = true;
    }
}
