package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GasCollectorRecipe {

    public static Map<Item, List<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registExtractorRecipes() {
        recipes.put(ItemRegister.items[ItemRegistry.GAS_TANK.ordinal()],
                Arrays.asList(new Pair<>(1F, ItemRegister.items[ItemRegistry.OXYGEN_TANK.ordinal()])));
    }
}
