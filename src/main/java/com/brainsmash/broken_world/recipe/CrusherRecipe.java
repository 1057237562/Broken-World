package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrusherRecipe {

    public static Map<Item, List<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registCrusherRecipes() {
        recipes.put(Blocks.STONE.asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON),
                        new Pair<>(0.80f, Items.GRAVEL),
                        new Pair<>(0.04f, Items.RAW_GOLD),
                        new Pair<>(0.15f, Items.RAW_COPPER),
                        new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Blocks.COBBLESTONE.asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON),
                        new Pair<>(0.80f, Items.GRAVEL),
                        new Pair<>(0.04f, Items.RAW_GOLD),
                        new Pair<>(0.15f, Items.RAW_COPPER),
                        new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Blocks.DEEPSLATE.asItem(),
                Arrays.asList(new Pair<>(0.12f, Items.RAW_IRON),
                        new Pair<>(0.90f, Items.COBBLESTONE),
                        new Pair<>(0.004f, Items.DIAMOND),
                        new Pair<>(0.08f, Items.RAW_GOLD),
                        new Pair<>(0.08f, Items.REDSTONE),
                        new Pair<>(0.16f, Items.COAL)));
        recipes.put(Blocks.COBBLED_DEEPSLATE.asItem(),
                Arrays.asList(new Pair<>(0.12f, Items.RAW_IRON),
                        new Pair<>(0.90f, Items.COBBLESTONE),
                        new Pair<>(0.004f, Items.DIAMOND),
                        new Pair<>(0.08f, Items.RAW_GOLD),
                        new Pair<>(0.08f, Items.REDSTONE),
                        new Pair<>(0.16f, Items.COAL)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_STONE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON),
                        new Pair<>(0.90f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_GOLD),
                        new Pair<>(0.04f, Items.REDSTONE)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_IRON_ORE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.90f, Items.RAW_IRON),
                        new Pair<>(0.90f, Items.RAW_IRON),
                        new Pair<>(0.90f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_GOLD),
                        new Pair<>(0.04f, Items.REDSTONE)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_GOLD_ORE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.90f, Items.RAW_GOLD),
                        new Pair<>(0.85f, Items.RAW_GOLD),
                        new Pair<>(0.65f, Items.GOLD_INGOT),
                        new Pair<>(0.90f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_IRON),
                        new Pair<>(0.04f, Items.REDSTONE)));
    }
}
