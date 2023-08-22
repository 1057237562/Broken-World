package com.brainsmash.broken_world.items.electrical;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public interface BatteryHolder {
    String ENERGY_KEY = BatteryItem.ENERGY_KEY;
    String MAX_ENERGY_KEY = BatteryItem.MAX_ENERGY_KEY;
    String RECHARGEABLE_KEY = BatteryItem.RECHARGEABLE_KEY;
    String BATTERY_KEY = "battery";
    default int getEnergy(ItemStack stack) {
        return stack.getOrCreateNbt().getCompound(BATTERY_KEY).getInt(ENERGY_KEY);
    }

    default Pair<Integer, Integer> getEnergyPair(ItemStack stack) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        return new Pair<>(batteryNBT.getInt(ENERGY_KEY), batteryNBT.getInt(MAX_ENERGY_KEY));
    }

    default int discharge(ItemStack stack, int amount) {
        return amount < 0 ? 0 : addEnergy(stack, -amount);
    }

    default boolean rechargeable(ItemStack stack) {
        return stack.getOrCreateNbt().getCompound(BATTERY_KEY).getBoolean(RECHARGEABLE_KEY);
    }

    default int charge(ItemStack stack, int amount) {
        return amount < 0 ? 0 : addEnergy(stack, amount);
    }

    default int addEnergy(ItemStack stack, int amount) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        if (amount > 0 && !batteryNBT.getBoolean(RECHARGEABLE_KEY))
            return 0;
        int e1 = batteryNBT.getInt(ENERGY_KEY);
        int e2 = MathHelper.clamp(e1+amount, 0, batteryNBT.getInt(MAX_ENERGY_KEY));
        batteryNBT.putInt(ENERGY_KEY, e2);
        return e2-e1;
    }
}
