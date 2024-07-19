package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrucibleRecipe {

    public static Map<List<Ingredient>, Potion> recipes = new ConcurrentHashMap<>();

    public static void addRecipe(List<Ingredient> ingredients, Potion potion) {
        recipes.put(ingredients, potion);
    }

    public static void registerCrucibleRecipe() {
        addRecipe(List.of(Ingredient.ofItems(Items.NAUTILUS_SHELL)), Potions.LUCK);
        addRecipe(List.of(Ingredient.ofItems(Items.GLOWSTONE_DUST)), Potions.THICK);
    }

    public boolean matches(ImplementedInventory inventory) {
        for (List<Ingredient> ingredients : recipes.keySet()) {
            if (ingredients.stream().allMatch(ingredient -> {
                for (ItemStack item : inventory.getItems()) {
                    if (ingredient.test(item)) return true;
                }
                return false;
            })) return true;
        }
        return false;
    }

    public Potion craft(ImplementedInventory inventory) {
        for (List<Ingredient> ingredients : recipes.keySet()) {
            if (ingredients.stream().allMatch(ingredient -> {
                for (ItemStack item : inventory.getItems()) {
                    if (ingredient.test(item)) return true;
                }
                return false;
            })) {
                return recipes.get(ingredients);
            }
        }
        return Potions.WATER;
    }
}
