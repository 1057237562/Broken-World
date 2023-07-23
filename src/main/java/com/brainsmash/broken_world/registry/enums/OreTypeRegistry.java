package com.brainsmash.broken_world.registry.enums;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OreTypeRegistry {

    public static Map<Block, Integer> mapping = new ConcurrentHashMap<>();

    public static void RegistOreType() {
        mapping.put(Blocks.REDSTONE_ORE, 0);
        mapping.put(Blocks.DEEPSLATE_REDSTONE_ORE, 1);
        mapping.put(Blocks.COPPER_ORE, 2);
        mapping.put(Blocks.DEEPSLATE_COPPER_ORE, 3);
        mapping.put(Blocks.GOLD_ORE, 4);
        mapping.put(Blocks.NETHER_GOLD_ORE, 5);
        mapping.put(Blocks.DEEPSLATE_GOLD_ORE, 6);
        mapping.put(Blocks.IRON_ORE, 7);
        mapping.put(Blocks.DEEPSLATE_IRON_ORE, 8);
        mapping.put(Blocks.COAL_ORE, 9);
        mapping.put(Blocks.DEEPSLATE_COAL_ORE, 10);
        mapping.put(Blocks.EMERALD_ORE, 11);
        mapping.put(Blocks.DEEPSLATE_EMERALD_ORE, 12);
        mapping.put(Blocks.LAPIS_ORE, 13);
        mapping.put(Blocks.DEEPSLATE_LAPIS_ORE, 14);
        mapping.put(Blocks.DIAMOND_ORE, 15);
        mapping.put(Blocks.DEEPSLATE_DIAMOND_ORE, 16);
        mapping.put(Blocks.NETHER_QUARTZ_ORE, 17);
        mapping.put(BlockRegister.blocks[BlockRegistry.MOON_IRON_ORE.ordinal()], 18);
        mapping.put(BlockRegister.blocks[BlockRegistry.MOON_GOLD_ORE.ordinal()], 19);
        mapping.put(BlockRegister.blocks[BlockRegistry.MOON_REDSTONE_ORE.ordinal()], 20);
        mapping.put(BlockRegister.blocks[BlockRegistry.TUNGSTEN_ORE.ordinal()], 21);
        mapping.put(BlockRegister.blocks[BlockRegistry.KYANITE_ORE.ordinal()], 22);
        mapping.put(BlockRegister.blocks[BlockRegistry.TIN_ORE.ordinal()], 23);
        mapping.put(BlockRegister.blocks[BlockRegistry.ALUMINUM_ORE.ordinal()], 24);
        mapping.put(BlockRegister.blocks[BlockRegistry.LEAD_ORE.ordinal()], 25);
    }

}
