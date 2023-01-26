package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SifterRecipe {

    public static Map<Item, DefaultedList<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registSifterRecipes() {
        recipes.put(Blocks.DIRT.asItem(),
                DefaultedList.copyOf(new Pair<>(0.06f, Items.RAW_IRON),
                        new Pair<>(0.80f, Items.SAND),
                        new Pair<>(0.1f, Items.WHEAT_SEEDS),
                        new Pair<>(0.03f, Items.MELON_SEEDS),
                        new Pair<>(0.04f, Items.PUMPKIN_SEEDS)));
        recipes.put(Blocks.SAND.asItem(),
                DefaultedList.copyOf(new Pair<>(0.0015f, Items.RAW_GOLD),
                        new Pair<>(0.2f, ItemRegister.items[ItemRegistry.SILICON.ordinal()])));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_SAND.ordinal()].asItem(),
                DefaultedList.copyOf(new Pair<>(0.0015f, Items.RAW_IRON),
                        new Pair<>(0.012f, Items.RAW_IRON),
                        new Pair<>(0.2f, ItemRegister.items[ItemRegistry.SILICON.ordinal()])));
    }
}
