package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompressorRecipe {
    public static Map<Item, ItemStack> recipes = new ConcurrentHashMap<>();

    public static void registCompressorRecipes() {
        recipes.put(Items.IRON_INGOT, new ItemStack(ItemRegister.items[ItemRegistry.IRON_PLATE.ordinal()], 2));
    }
}
