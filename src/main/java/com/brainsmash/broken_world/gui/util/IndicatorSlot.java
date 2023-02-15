package com.brainsmash.broken_world.gui.util;

import com.brainsmash.broken_world.gui.widgets.WIndicatorItemSlot;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class IndicatorSlot extends ValidatedSlot {

    protected final Multimap<WIndicatorItemSlot, WIndicatorItemSlot.ChangeListener> listeners = HashMultimap.create();

    public IndicatorSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        ItemStack itemStack = stack.copy();
        itemStack.setCount(1);
        setStack(itemStack);
        return stack;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        setStack(ItemStack.EMPTY);
        markDirty();
        inventory.markDirty();
        return false;
    }

    @Override
    public void markDirty() {
        listeners.forEach((slot, listener) -> listener.onStackChanged(slot, inventory, getInventoryIndex(), getStack()));
        super.markDirty();
    }

    public void addChangeListener(WIndicatorItemSlot owner, WIndicatorItemSlot.ChangeListener listener) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(listener, "listener");
        listeners.put(owner, listener);
    }
}
