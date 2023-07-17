package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CentrifugeRecipe {

    public static Map<Pair<Fluid, Item>, Pair<List<Pair<Float, Item>>, Fluid>> recipes = new ConcurrentHashMap<>();

    public static void registCentrifugeRecipes() {
        recipes.put(new Pair<>(FluidRegister.still_fluid[FluidRegistry.OIL.ordinal()], null), new Pair<>(
                Arrays.asList(new Pair<>(0.45f, ItemRegister.items[ItemRegistry.PLASTIC_PLATE.ordinal()]),
                        new Pair<>(0.35f, ItemRegister.items[ItemRegistry.ASPHALT.ordinal()])),
                FluidRegister.still_fluid[FluidRegistry.GASOLINE.ordinal()]));
        recipes.put(new Pair<>(FluidRegister.still_fluid[FluidRegistry.LATEX.ordinal()], null), new Pair<>(
                Arrays.asList(new Pair<>(0.95f, ItemRegister.items[ItemRegistry.RUBBER.ordinal()]),
                        new Pair<>(0.95f, ItemRegister.items[ItemRegistry.RUBBER.ordinal()]),
                        new Pair<>(0.8f, ItemRegister.items[ItemRegistry.RUBBER.ordinal()])), null));
    }
}
