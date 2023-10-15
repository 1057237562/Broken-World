package com.brainsmash.broken_world.blocks.entity.container;

import net.minecraft.item.ItemStack;

public interface ItemInterface {

    ItemStack insertItemToOutput(ItemStack stack);

    ItemStack insertItemToInput(ItemStack stack);
}
