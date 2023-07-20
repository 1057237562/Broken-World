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
    public static Map<Item, Integer> counts = new ConcurrentHashMap<>();

    public static void registCompressorRecipes() {
        registCompressorRecipe(Items.IRON_INGOT.getDefaultStack(),
                new ItemStack(ItemRegister.items[ItemRegistry.IRON_PLATE.ordinal()], 2));
        registCompressorRecipe(ItemRegister.items[ItemRegistry.STEEL_INGOT.ordinal()].getDefaultStack(),
                new ItemStack(ItemRegister.items[ItemRegistry.STEEL_PLATE.ordinal()], 1));
        registCompressorRecipe(new ItemStack(Items.STONE, 4), new ItemStack(Items.COBBLED_DEEPSLATE));
    }

    public static void registCompressorRecipe(ItemStack a, ItemStack b) {
        recipes.put(a.getItem(), b);
        counts.put(a.getItem(), a.getCount());
    }

}
