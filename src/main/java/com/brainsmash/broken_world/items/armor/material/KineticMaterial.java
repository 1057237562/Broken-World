package com.brainsmash.broken_world.items.armor.material;

import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.NbtCompound;
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
    public void processSetBonus(EntityDataExtension dataExtension) {
        NbtCompound nbtCompound = (NbtCompound) dataExtension.getData();
        NbtCompound bonus = new NbtCompound();
        bonus.putBoolean("jet", true);
        nbtCompound.put("bonus", bonus);
        dataExtension.setData(nbtCompound);
    }
}
