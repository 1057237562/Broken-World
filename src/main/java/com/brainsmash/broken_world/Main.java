package com.brainsmash.broken_world;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Supplier;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "broken_world";
	public static final Block[] blocks = {
			new FallingBlock(AbstractBlock.Settings.of(Material.MOSS_BLOCK).strength(1.0f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).strength(1.0f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).strength(1.5f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).strength(2.0f))
	};
	public static final Item[] items = {
			new BlockItem(blocks[0],new FabricItemSettings()),
			new BlockItem(blocks[1],new FabricItemSettings()),
			new BlockItem(blocks[2],new FabricItemSettings()),
			new BlockItem(blocks[3],new FabricItemSettings())
	};
	public static final String[] blocknames = {"moon_sand","moon_stone","moon_iron_ore","moon_gold_ore"};
	private static final String[] configurenames = {"moon_sand","moon_iron_ore","moon_gold_ore"};
	private static ConfiguredFeature<?, ?>[] configuredFeatures = {
			new ConfiguredFeature(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[0].getDefaultState(),9)),
			new ConfiguredFeature(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[2].getDefaultState(),9)),
			new ConfiguredFeature(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[3].getDefaultState(),9))
	};

	public static PlacedFeature[] placedFeatures = {
			new PlacedFeature(RegistryEntry.of(configuredFeatures[0]),Arrays.asList(CountPlacementModifier.of(42),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(128)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),Arrays.asList(CountPlacementModifier.of(63),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(128)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),Arrays.asList(CountPlacementModifier.of(36),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(128))))
	};

	private static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "itemgroup"), new Supplier<ItemStack>() {
		@Override
		public ItemStack get() {
			return new ItemStack(items[0]);
		}
	});

	@Override
	public void onInitialize() {
		for(int i = 0;i<blocks.length;i++){
			Registry.register(Registry.BLOCK, new Identifier(MODID, blocknames[i]), blocks[i]);
			Registry.register(Registry.ITEM, new Identifier(MODID, blocknames[i]), items[i]);
		}
		for(int i = 0;i<configuredFeatures.length;i++){
			Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier(MODID, configurenames[i]), configuredFeatures[i]);
			Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MODID, configurenames[i]),placedFeatures[i]);
		}
	}
}
