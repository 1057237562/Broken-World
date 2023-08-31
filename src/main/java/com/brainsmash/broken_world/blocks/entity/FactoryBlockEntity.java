package com.brainsmash.broken_world.blocks.entity;

import net.minecraft.inventory.Inventory;

import java.util.List;

public interface FactoryBlockEntity<T extends Inventory, C> {

    boolean matches(T itemList);

    List<C> craft(T itemList);
}
