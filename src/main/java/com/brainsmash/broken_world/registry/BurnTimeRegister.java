package com.brainsmash.broken_world.registry;

import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class BurnTimeRegister {
    @Nullable
    public static Map<Item,Integer> generator_fuel = null;

    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"));
            }
            return;
        }
        fuelTimes.put(item2, fuelTime);
    }
    
    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (RegistryEntry<Item> registryEntry : Registry.ITEM.iterateEntries(tag)) {
            if (isNonFlammableWood(registryEntry.value())) continue;
            fuelTimes.put(registryEntry.value(), fuelTime);
        }
    }

    public static void getGeneratorMap(){
        if(generator_fuel == null) {
            generator_fuel = new LinkedHashMap<>();
            RegistGeneratorFuel();
        }
    }

    public static void RegistGeneratorFuel(){
        addFuel(generator_fuel, Blocks.COAL_BLOCK, 16000);
        addFuel(generator_fuel, Items.COAL, 1600);
        addFuel(generator_fuel, Items.CHARCOAL, 1600);
        addFuel(generator_fuel, ItemTags.LOGS, 300);
        addFuel(generator_fuel, ItemTags.PLANKS, 300);
        addFuel(generator_fuel, ItemTags.WOODEN_STAIRS, 300);
        addFuel(generator_fuel, ItemTags.WOODEN_SLABS, 150);
        addFuel(generator_fuel, ItemTags.WOODEN_TRAPDOORS, 300);
        addFuel(generator_fuel, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        addFuel(generator_fuel, Blocks.OAK_FENCE, 300);
        addFuel(generator_fuel, Blocks.BIRCH_FENCE, 300);
        addFuel(generator_fuel, Blocks.SPRUCE_FENCE, 300);
        addFuel(generator_fuel, Blocks.JUNGLE_FENCE, 300);
        addFuel(generator_fuel, Blocks.DARK_OAK_FENCE, 300);
        addFuel(generator_fuel, Blocks.ACACIA_FENCE, 300);
        addFuel(generator_fuel, Blocks.MANGROVE_FENCE, 300);
        addFuel(generator_fuel, Blocks.OAK_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.BIRCH_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.SPRUCE_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.JUNGLE_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.DARK_OAK_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.ACACIA_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.MANGROVE_FENCE_GATE, 300);
        addFuel(generator_fuel, Blocks.NOTE_BLOCK, 300);
        addFuel(generator_fuel, Blocks.BOOKSHELF, 300);
        addFuel(generator_fuel, Blocks.LECTERN, 300);
        addFuel(generator_fuel, Blocks.JUKEBOX, 300);
        addFuel(generator_fuel, Blocks.CHEST, 300);
        addFuel(generator_fuel, Blocks.TRAPPED_CHEST, 300);
        addFuel(generator_fuel, Blocks.CRAFTING_TABLE, 300);
        addFuel(generator_fuel, Blocks.DAYLIGHT_DETECTOR, 300);
        addFuel(generator_fuel, ItemTags.BANNERS, 300);
        addFuel(generator_fuel, Items.BOW, 300);
        addFuel(generator_fuel, Items.FISHING_ROD, 300);
        addFuel(generator_fuel, Blocks.LADDER, 300);
        addFuel(generator_fuel, ItemTags.SIGNS, 200);
        addFuel(generator_fuel, Items.WOODEN_SHOVEL, 200);
        addFuel(generator_fuel, Items.WOODEN_SWORD, 200);
        addFuel(generator_fuel, Items.WOODEN_HOE, 200);
        addFuel(generator_fuel, Items.WOODEN_AXE, 200);
        addFuel(generator_fuel, Items.WOODEN_PICKAXE, 200);
        addFuel(generator_fuel, ItemTags.WOODEN_DOORS, 200);
        addFuel(generator_fuel, ItemTags.BOATS, 1200);
        addFuel(generator_fuel, ItemTags.WOOL, 100);
        addFuel(generator_fuel, ItemTags.WOODEN_BUTTONS, 100);
        addFuel(generator_fuel, Items.STICK, 100);
        addFuel(generator_fuel, ItemTags.SAPLINGS, 100);
        addFuel(generator_fuel, Items.BOWL, 100);
        addFuel(generator_fuel, ItemTags.WOOL_CARPETS, 67);
        addFuel(generator_fuel, Blocks.DRIED_KELP_BLOCK, 4001);
        addFuel(generator_fuel, Items.CROSSBOW, 300);
        addFuel(generator_fuel, Blocks.BAMBOO, 50);
        addFuel(generator_fuel, Blocks.DEAD_BUSH, 100);
        addFuel(generator_fuel, Blocks.SCAFFOLDING, 50);
        addFuel(generator_fuel, Blocks.LOOM, 300);
        addFuel(generator_fuel, Blocks.BARREL, 300);
        addFuel(generator_fuel, Blocks.CARTOGRAPHY_TABLE, 300);
        addFuel(generator_fuel, Blocks.FLETCHING_TABLE, 300);
        addFuel(generator_fuel, Blocks.SMITHING_TABLE, 300);
        addFuel(generator_fuel, Blocks.COMPOSTER, 300);
        addFuel(generator_fuel, Blocks.AZALEA, 100);
        addFuel(generator_fuel, Blocks.FLOWERING_AZALEA, 100);
        addFuel(generator_fuel, Blocks.MANGROVE_ROOTS, 300);
    }
}
