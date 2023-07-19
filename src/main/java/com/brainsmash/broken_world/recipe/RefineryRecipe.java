package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class RefineryRecipe {
    public static Map<String, ItemStack> recipes = new HashMap<>();
    public static Map<String, Pair<Integer, Integer>> counts = new HashMap<>();

    public static String makePair(Item a, Item b) {
        if (a.hashCode() > b.hashCode()) {
            return a.hashCode() + String.valueOf(b.hashCode());
        } else {
            return b.hashCode() + String.valueOf(a.hashCode());
        }
    }

    /**
     * Make pairs of itemstack's count based on the compare itemstack's item's hashcode
     */
    public static Pair<Integer, Integer> makePair(ItemStack a, ItemStack b) {
        if (a.getItem().hashCode() > b.getItem().hashCode()) {
            return new Pair<>(a.getCount(), b.getCount());
        } else {
            return new Pair<>(b.getCount(), a.getCount());
        }
    }

    public static void registRefineryRecipes() {
        registRefineryRecipe(new ItemStack(ItemRegister.items[ItemRegistry.OXYGEN_TANK.ordinal()]),
                new ItemStack(Items.IRON_INGOT), new ItemStack(ItemRegister.items[ItemRegistry.STEEL_INGOT.ordinal()]));
        registRefineryRecipe(new ItemStack(ItemRegister.items[ItemRegistry.TIN_INGOT.ordinal()]),
                new ItemStack(Items.COPPER_INGOT, 3),
                new ItemStack(ItemRegister.items[ItemRegistry.BRONZE_INGOT.ordinal()], 3));
        registRefineryRecipe(new ItemStack(ItemRegister.items[ItemRegistry.TIN_INGOT.ordinal()]),
                new ItemStack(Items.IRON_INGOT, 2),
                new ItemStack(ItemRegister.items[ItemRegistry.TINPLATE.ordinal()], 2));
    }

    /**
     * regist recipe
     */
    public static void registRefineryRecipe(ItemStack a, ItemStack b, ItemStack c) {
        recipes.put(makePair(a.getItem(), b.getItem()), c);
        counts.put(makePair(a.getItem(), b.getItem()), makePair(a, b));
    }
}
