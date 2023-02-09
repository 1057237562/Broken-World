package com.brainsmash.broken_world.items.weapons.guns;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class GunItem extends Item {
    int maxMagazine = 20;
    int maxReloadTime = 50;

    public GunItem(Settings settings) {
        super(settings);
    }

    public void fire(World world, PlayerEntity user) {
    }

    public boolean fireTick(World world, PlayerEntity user) {
        return true;
    }

    public boolean hasAmmo(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) return false;
        return nbtCompound.getInt("ammoCount") > 0;
    }

    public void reduceAmmo(ItemStack stack) {
        if (hasAmmo(stack)) {
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound == null) nbtCompound = new NbtCompound();
            nbtCompound.putInt("ammoCount", nbtCompound.getInt("ammoCount") - 1);
            stack.setNbt(nbtCompound);
        }
    }

    public void reload(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("reloading", true);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) nbtCompound = new NbtCompound();
        if (!selected) {
            nbtCompound.putBoolean("reloading", false);
            nbtCompound.putInt("reloadTick", 0);
        } else {
            if (nbtCompound.getBoolean("reloading")) {
                int tick = nbtCompound.getInt("reloadTick");
                if (tick < maxReloadTime) {
                    tick++;
                    nbtCompound.putInt("reloadTick", tick);
                } else {
                    nbtCompound.putBoolean("reloading", false);
                    nbtCompound.putInt("reloadTick", 0);

                    nbtCompound.putInt("ammoCount", countAmmo((PlayerEntity) entity));
                }
            }
        }
        stack.setNbt(nbtCompound);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public int countAmmo(PlayerEntity entity) {
        return 0;
    }
}
