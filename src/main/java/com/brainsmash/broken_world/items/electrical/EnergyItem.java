package com.brainsmash.broken_world.items.electrical;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public interface EnergyItem {
    String MAX_ENERGY_KEY = "maxEnergy";
    String RECHARGEABLE_KEY = "rechargeable";
    String BATTERY_KEY = "battery";
    String ENERGY_KEY = "energy";

    default int getEnergy(ItemStack stack) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        return batteryNBT.getInt(ENERGY_KEY);
    }

    default Pair<Integer, Integer> getEnergyPair(ItemStack stack) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        return new Pair<>(batteryNBT.getInt(ENERGY_KEY), batteryNBT.getInt(MAX_ENERGY_KEY));
    }

    default int discharge(ItemStack stack, int amount) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        int energy = batteryNBT.getInt(ENERGY_KEY);
        int delta = -Math.min(energy, amount);
        batteryNBT.putInt(ENERGY_KEY, energy + delta);
        return delta;
    }

    default boolean chargeable(ItemStack stack) {
        return stack.getOrCreateNbt().getCompound(BATTERY_KEY).getBoolean(RECHARGEABLE_KEY);
    }

    default int charge(ItemStack stack, int amount) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        if (!batteryNBT.getBoolean(RECHARGEABLE_KEY))
            return 0;
        int energy = batteryNBT.getInt(ENERGY_KEY);
        int maxEnergy = batteryNBT.getInt(MAX_ENERGY_KEY);
        int delta = Math.min(energy + amount, maxEnergy) - energy;
        batteryNBT.putInt(ENERGY_KEY, energy + delta);
        return delta;
    }
}
