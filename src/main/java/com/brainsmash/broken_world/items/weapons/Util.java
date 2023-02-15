package com.brainsmash.broken_world.items.weapons;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Util {

    public static ItemStack getAmmo(PlayerEntity entity, Item match) {
        Inventory inventory = entity.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(match)) {
                return inventory.getStack(i);
            }
        }
        return ItemStack.EMPTY;
    }
}
