package com.brainsmash.broken_world.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public static List<ItemStack> getItemstackFromPairs(List<Pair<Float, Item>> pairs) {
        List<ItemStack> items = new ArrayList<>();
        for (Pair<Float, Item> pair : pairs) {
            items.add(pair.getRight().getDefaultStack());
        }
        return items;
    }

    /**
     * Convert List<ItemStack> to List<Ingredient>
     */
    public static List<Ingredient> fromItemStacks(List<ItemStack> stacks) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) ingredients.add(fromItemStack(stack));
        }
        return ingredients;
    }

    /**
     * Make pairs of item's hashcode based on the compare item's hashcode
     */
    public static String makePair(Item... items) {
        ArrayList<Item> itemList = new ArrayList<>(List.of(items));
        itemList.sort(Comparator.comparingInt(Item::hashCode));
        return itemList.stream().map(Item::hashCode).map(String::valueOf).collect(Collectors.joining(":"));
    }

    /**
     * Make pairs of itemstack's count based on the compare itemstack's item's hashcode
     */
    public static List<Integer> makePair(ItemStack... itemStacks) {
        ArrayList<ItemStack> itemList = new ArrayList<>(List.of(itemStacks));
        itemList.sort(Comparator.comparingInt(itemStack -> itemStack.getItem().hashCode()));
        return itemList.stream().map(ItemStack::getCount).collect(Collectors.toList());
    }

    public static int calculateItemBarColor(int value, int maxValue) {
        float f = Math.max(0.0f, (float)value / (float)maxValue);
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    public static int calculateItemBarStep(int value, int maxValue) {
        return Math.round((float)value * 13.0f / (float)maxValue);
    }
}
