package com.brainsmash.broken_world.util;

import net.minecraft.nbt.NbtCompound;

public class BonusHelper {
    public static boolean getBoolean(NbtCompound compound, String key) {
        if (compound.get("bonus") instanceof NbtCompound nbtCompound) {
            return nbtCompound.getBoolean(key);
        }
        return false;
    }
}
