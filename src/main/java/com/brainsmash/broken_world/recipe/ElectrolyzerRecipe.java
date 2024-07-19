package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElectrolyzerRecipe {
    public static Map<Item, ItemStack> recipes = new ConcurrentHashMap<>();
    public static Map<Item, Integer> counts = new ConcurrentHashMap<>();

    public static void registerElectrolyzerRecipes() {
        registElectrolyzerRecipe(new ItemStack(ItemRegister.items[ItemRegistry.RAW_ALUMINUM.ordinal()], 3),
                ItemRegister.items[ItemRegistry.ALUMINUM_INGOT.ordinal()].getDefaultStack());
    }

    public static void registElectrolyzerRecipe(ItemStack a, ItemStack b) {
        recipes.put(a.getItem(), b);
        counts.put(a.getItem(), a.getCount());
    }

}
