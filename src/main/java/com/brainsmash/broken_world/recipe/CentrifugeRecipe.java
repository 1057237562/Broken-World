package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CentrifugeRecipe {

    public static Map<Pair<Fluid, Item>, DefaultedList<Pair<Float, Item>>> recipes = new ConcurrentHashMap<>();

    public static void registCentrifugeRecipes() {
        recipes.put(new Pair<>(FluidRegister.still_fluid[FluidRegistry.OIL.ordinal()], null),
                DefaultedList.copyOf(new Pair<>(0.85f, ItemRegister.items[ItemRegistry.PLASTIC_PLATE.ordinal()])));
    }
}
