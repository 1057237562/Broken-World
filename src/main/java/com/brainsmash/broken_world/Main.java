package com.brainsmash.broken_world;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Block[] blocks = {
			new FallingBlock(FabricBlockSettings.of(Material.SOIL).strength(1.0f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).strength(1.0f))
	};
	public static final String[] blocknames = {"moon_sand","moon_stone"};
	@Override
	public void onInitialize() {
		for(int i = 0;i<blocks.length;i++){
			Registry.register(Registry.BLOCK, new Identifier("broken_world", blocknames[i]), blocks[i]);
			Registry.register(Registry.ITEM, new Identifier("broken_world", blocknames[i]), new BlockItem(blocks[i], new FabricItemSettings()));
		}
	}
}
