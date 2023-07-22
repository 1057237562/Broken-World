package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.brainsmash.broken_world.util.ItemHelper.makePair;

public class ReactionRecipe {
    public static Map<String, ItemStack> recipes = new ConcurrentHashMap<>();
    public static Map<String, List<Integer>> counts = new ConcurrentHashMap<>();
    public static Map<List<ItemStack>, ItemStack> rei = new HashMap<>();

    public static void registReactionRecipes() {
        registReaction(new ItemStack(Items.FLINT, 4),
                ItemRegister.items[ItemRegistry.SULFUR.ordinal()].getDefaultStack(), Items.CHARCOAL.getDefaultStack(),
                new ItemStack(Items.GUNPOWDER, 2));
    }

    /**
     * regist recipe
     */
    public static void registReaction(ItemStack a, ItemStack b, ItemStack c, ItemStack d) {
        recipes.put(makePair(a.getItem(), b.getItem(), c.getItem()), d);
        counts.put(makePair(a.getItem(), b.getItem(), c.getItem()), makePair(a, b, c));
        rei.put(List.of(a, b, c), d);
    }
}
