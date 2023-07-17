package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class RefineryRecipe {
    public static Map<String, ItemStack> recipes = new HashMap<>();

    public static String makePair(Item a, Item b) {
        if (a.hashCode() > b.hashCode()) {
            return a.hashCode() + String.valueOf(b.hashCode());
        } else {
            return b.hashCode() + String.valueOf(a.hashCode());
        }
    }

    public static void registRefineryRecipes() {
        recipes.put(makePair(ItemRegister.items[ItemRegistry.OXYGEN_TANK.ordinal()], Items.IRON_INGOT),
                new ItemStack(ItemRegister.items[ItemRegistry.STEEL_INGOT.ordinal()]));
    }
}
