package com.brainsmash.broken_world.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemHelper {
    /**
     * Convert Itemstack to Ingredient
     */
    public static Ingredient fromItemStack(ItemStack stack) {
        return Ingredient.ofStacks(stack);
    }

    /**
     * Convert Ingredient to Itemstack
     */
    public static ItemStack toItemStack(Ingredient ingredient) {
        return ingredient.getMatchingStacks()[0];
    }

    /**
     * Get List<Item> from List<Pair<Float,Item>>
     */
    public static Collection<ItemConvertible> getItemsFromPairs(List<Pair<Float, Item>> pairs) {
        List<ItemConvertible> items = new ArrayList<>();
        for (Pair<Float, Item> pair : pairs) {
            items.add(pair.getRight());
        }
        return items;
    }

    /**
     * Convert List<ItemStack> to List<Ingredient>
     */
    public static List<Ingredient> fromItemStacks(List<ItemStack> stacks) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (ItemStack stack : stacks) {
            ingredients.add(fromItemStack(stack));
        }
        return ingredients;
    }
}
