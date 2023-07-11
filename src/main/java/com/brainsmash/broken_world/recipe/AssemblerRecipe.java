package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class AssemblerRecipe {
    public static Map<Pair<Item, Item>, ItemStack> recipes = new HashMap<>();

    public static Pair<Item, Item> makePair(Item a, Item b) {
        if (a.hashCode() > b.hashCode()) {
            return new Pair<>(a, b);
        } else {
            return new Pair<>(b, a);
        }
    }

    public static void registAssemblerRecipe() {
        recipes.put(makePair(ItemRegister.items[ItemRegistry.SILICON.ordinal()], Items.GOLD_INGOT),
                new ItemStack(ItemRegister.items[ItemRegistry.MEMORY_CHIP.ordinal()]));
    }
}
