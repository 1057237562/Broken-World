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
    public static Map<String, ItemStack> recipes = new HashMap<>();
    public static Map<Pair<Item, Item>, ItemStack> rei = new HashMap<>();

    public static String makePair(Item a, Item b) {
        if (a.hashCode() > b.hashCode()) {
            return a.hashCode() + ":" + b.hashCode();
        } else {
            return b.hashCode() + ":" + a.hashCode();
        }
    }

    public static void registerAssemblerRecipes() {
        registAssemblerRecipe(ItemRegister.items[ItemRegistry.SILICON.ordinal()], Items.GOLD_INGOT,
                new ItemStack(ItemRegister.items[ItemRegistry.MEMORY_CHIP.ordinal()]));
        registAssemblerRecipe(ItemRegister.items[ItemRegistry.PLASTIC_PLATE.ordinal()], Items.GOLD_INGOT,
                new ItemStack(ItemRegister.items[ItemRegistry.CIRCUIT_BOARD.ordinal()]));
        registAssemblerRecipe(ItemRegister.items[ItemRegistry.PLASTIC_PLATE.ordinal()],
                ItemRegister.items[ItemRegistry.SILICON.ordinal()],
                new ItemStack(ItemRegister.items[ItemRegistry.CIRCUIT_BOARD.ordinal()]));
    }

    /**
     * regist Assembler recipe
     */
    public static void registAssemblerRecipe(Item a, Item b, ItemStack c) {
        recipes.put(makePair(a, b), c);
        rei.put(new Pair<>(a, b), c);
    }
}
