package com.brainsmash.broken_world.recipe.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;

import java.util.Iterator;
import java.util.List;

public class ItemInventory extends CraftingInventory {

    private List<ItemStack> itemStackList;

    public ItemInventory(List<ItemStack> itemStacks) {
        super(null, 0, 0);
        this.itemStackList = itemStacks;
    }

    @Override
    public int size() {
        return itemStackList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return itemStackList.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(itemStackList, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(itemStackList, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        itemStackList.set(slot, stack);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        itemStackList.clear();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        Iterator var2 = this.itemStackList.iterator();

        while (var2.hasNext()) {
            ItemStack itemStack = (ItemStack) var2.next();
            finder.addUnenchantedInput(itemStack);
        }

    }
}
