package com.brainsmash.broken_world.registry;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrusherRegister {

    public static Map<Item, DefaultedList<Pair<Float,Item>>> recipes = new ConcurrentHashMap<>();

    public void RegistCrusherRecipes(){
        recipes.put(Blocks.DIRT.asItem(), DefaultedList.copyOf(new Pair<>(0.1f,Items.IRON_NUGGET)));
    }
}
