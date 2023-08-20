package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GrindRecipe {

    public static Map<Item, ItemStack> GRINDING_RECIPES = new HashMap<>();

    public static void addGrindingRecipe(Item input, ItemStack output) {
        GRINDING_RECIPES.put(input, output);
    }

    public static void registerGrindingRecipe() {
        addGrindingRecipe(Items.AMETHYST_SHARD, ItemRegister.get(ItemRegistry.AMETHYST_POWDER).getDefaultStack());
    }
}
