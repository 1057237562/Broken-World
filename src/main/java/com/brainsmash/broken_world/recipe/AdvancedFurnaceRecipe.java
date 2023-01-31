package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedFurnaceRecipe {

    public static Map<Item, List<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registAdvancedFurnaceRecipe() {
        recipes.put(Items.IRON_INGOT,
                Arrays.asList(new Pair<>(0.975f, ItemRegister.items[ItemRegistry.STEEL_INGOT.ordinal()]),
                        new Pair<>(0.075f, Items.COAL)));
        recipes.put(Items.RAW_IRON,
                Arrays.asList(new Pair(1f, Items.IRON_INGOT),
                        new Pair<>(0.15f, Items.COAL),
                        new Pair<>(0.35f, Items.IRON_NUGGET),
                        new Pair<>(0.12f, Items.GOLD_NUGGET)));
        recipes.put(Items.COBBLESTONE,
                Arrays.asList(new Pair<>(0.55f, Items.MAGMA_BLOCK), new Pair<>(0.15f, Items.IRON_NUGGET)));
        recipes.put(Items.STONE,
                Arrays.asList(new Pair<>(0.55f, Items.MAGMA_BLOCK), new Pair<>(0.15f, Items.IRON_NUGGET)));
        recipes.put(Items.DEEPSLATE,
                Arrays.asList(new Pair<>(0.75f, Items.MAGMA_BLOCK),
                        new Pair<>(0.12f, Items.IRON_INGOT),
                        new Pair<>(0.15f, Items.IRON_NUGGET),
                        new Pair<>(0.02f, Items.GOLD_NUGGET)));
    }
}
