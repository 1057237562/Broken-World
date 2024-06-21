package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrusherRecipe {

    public static Map<Item, List<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registerCrusherRecipes() {
        recipes.put(Blocks.STONE.asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON), new Pair<>(0.30f, Items.GRAVEL),
                        new Pair<>(0.02f, Items.RAW_GOLD), new Pair<>(0.15f, Items.RAW_COPPER),
                        new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Blocks.COBBLESTONE.asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON), new Pair<>(0.30f, Items.GRAVEL),
                        new Pair<>(0.04f, Items.RAW_GOLD), new Pair<>(0.15f, Items.RAW_COPPER),
                        new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Blocks.DEEPSLATE.asItem(),
                Arrays.asList(new Pair<>(0.12f, Items.RAW_IRON), new Pair<>(0.40f, Items.COBBLESTONE),
                        new Pair<>(0.0001f, Items.DIAMOND), new Pair<>(0.08f, Items.RAW_GOLD),
                        new Pair<>(0.08f, Items.REDSTONE), new Pair<>(0.16f, Items.COAL)));
        recipes.put(Blocks.COBBLED_DEEPSLATE.asItem(),
                Arrays.asList(new Pair<>(0.12f, Items.RAW_IRON), new Pair<>(0.40f, Items.COBBLESTONE),
                        new Pair<>(0.001f, Items.DIAMOND), new Pair<>(0.08f, Items.RAW_GOLD),
                        new Pair<>(0.08f, Items.REDSTONE), new Pair<>(0.16f, Items.COAL)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_STONE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.08f, Items.RAW_IRON),
                        new Pair<>(0.30f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_GOLD), new Pair<>(0.04f, Items.REDSTONE)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_IRON_ORE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.90f, Items.RAW_IRON), new Pair<>(0.90f, Items.RAW_IRON),
                        new Pair<>(0.30f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_GOLD), new Pair<>(0.04f, Items.REDSTONE)));
        recipes.put(BlockRegister.blocks[BlockRegistry.MOON_GOLD_ORE.ordinal()].asItem(),
                Arrays.asList(new Pair<>(0.90f, Items.RAW_GOLD), new Pair<>(0.85f, Items.RAW_GOLD),
                        new Pair<>(0.65f, Items.GOLD_INGOT),
                        new Pair<>(0.30f, BlockRegister.blockitems[BlockRegistry.MOON_SAND.ordinal()]),
                        new Pair<>(0.04f, Items.RAW_IRON), new Pair<>(0.04f, Items.REDSTONE)));
        recipes.put(BlockRegister.blockitems[BlockRegistry.TIN_ORE.ordinal()],
                Arrays.asList(new Pair<>(0.90f, ItemRegister.items[ItemRegistry.RAW_TIN.ordinal()]),
                        new Pair<>(0.90f, ItemRegister.items[ItemRegistry.RAW_TIN.ordinal()]),
                        new Pair<>(0.08f, Items.RAW_IRON), new Pair<>(0.30f, Items.GRAVEL),
                        new Pair<>(0.04f, Items.RAW_GOLD), new Pair<>(0.15f, Items.RAW_COPPER),
                        new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Items.DIORITE, Arrays.asList(new Pair<>(0.25f, Items.QUARTZ), new Pair<>(0.20f, Items.GRAVEL),
                new Pair<>(0.1f, Items.DIRT)));
        recipes.put(Items.GRANITE, Arrays.asList(new Pair<>(0.15f, Items.REDSTONE), new Pair<>(0.3f, Items.COBBLESTONE),
                new Pair<>(0.20f, Items.GRAVEL), new Pair<>(0.1f, Items.DIRT)));
    }

    public boolean matches(ImplementedInventory itemList) {
        return recipes.containsKey(itemList.getStack(0).getItem());
    }

    public List<ItemStack> craft(ImplementedInventory itemList) {
        return null;
    }
}
