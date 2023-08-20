package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtractorRecipe {

    public static Map<Item, List<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registerExtractorRecipes() {
        recipes.put(ItemRegister.items[ItemRegistry.BOWL_OF_LATEX.ordinal()],
                Arrays.asList(new Pair<>(0.8F, ItemRegister.items[ItemRegistry.RUBBER.ordinal()]),
                        new Pair<>(0.8F, ItemRegister.items[ItemRegistry.RUBBER.ordinal()]),
                        new Pair<>(0.8F, ItemRegister.items[ItemRegistry.RUBBER.ordinal()])));
    }
}
