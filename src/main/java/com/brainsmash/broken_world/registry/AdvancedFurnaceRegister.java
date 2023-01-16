package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedFurnaceRegister {

    public static Map<Item, DefaultedList<Pair<Float,Item>>> recipes = new ConcurrentHashMap<>();

    public static void RegistCrusherRecipes(){
    }
}
