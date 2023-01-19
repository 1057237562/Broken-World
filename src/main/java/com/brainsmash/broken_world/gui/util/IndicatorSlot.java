package com.brainsmash.broken_world.gui.util;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class IndicatorSlot extends ValidatedSlot {

    public IndicatorSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        itemStack.setCount(1);
        setStack(itemStack);
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        setStack(ItemStack.EMPTY);
        return false;
    }


}
