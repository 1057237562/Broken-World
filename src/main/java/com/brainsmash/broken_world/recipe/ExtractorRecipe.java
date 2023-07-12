package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtractorRecipe {

    public static Map<Item, Pair<Float, Item>> recipes = new ConcurrentHashMap<>();

    public static void registExtractorRecipes() {
        recipes.put(ItemRegister.items[ItemRegistry.BOWL_OF_RUBBER.ordinal()], new Pair<>(0.5F, Items.ACACIA_LOG));
    }
}
