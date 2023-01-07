package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrusherRegister {

    public static Map<Item, DefaultedList<Pair<Float,Item>>> recipes = new ConcurrentHashMap<>();

    public static void RegistCrusherRecipes(){
        recipes.put(Blocks.STONE.asItem(), DefaultedList.copyOf(
                new Pair<>(0.26f,Items.RAW_IRON),
                new Pair<>(0.80f,Items.GRAVEL),
                new Pair<>(0.08f,Items.RAW_GOLD),
                new Pair<>(0.3f,Items.RAW_COPPER),
                new Pair<>(0.2f,Items.DIRT)));
        recipes.put(Blocks.COBBLESTONE.asItem(),DefaultedList.copyOf(
                new Pair<>(0.16f,Items.RAW_IRON),
                new Pair<>(0.80f,Items.GRAVEL),
                new Pair<>(0.08f,Items.RAW_GOLD),
                new Pair<>(0.3f,Items.RAW_COPPER),
                new Pair<>(0.2f,Items.DIRT)));
        recipes.put(Blocks.DEEPSLATE.asItem(),DefaultedList.copyOf(
                new Pair<>(0.22f,Items.RAW_IRON),
                new Pair<>(0.90f,Items.COBBLESTONE),
                new Pair<>(0.004f,Items.DIAMOND),
                new Pair<>(0.16f,Items.RAW_GOLD),
                new Pair<>(0.16f,Items.REDSTONE),
                new Pair<>(0.3f,Items.COAL)));


    }
}
