package com.brainsmash.broken_world;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Supplier;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "broken_world";
	private static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "itemgroup"), new Supplier<>() {
		@Override
		public ItemStack get() {
			return new ItemStack(items[0]);
		}
	});

	public static final Block[] blocks = {
			new FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).jumpVelocityMultiplier(3).strength(1.0f)),
			new Block(FabricBlockSettings.copyOf(Blocks.STONE).jumpVelocityMultiplier(5).strength(2.0f,2.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).jumpVelocityMultiplier(3).requiresTool().strength(3.0f,3.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).jumpVelocityMultiplier(3).requiresTool().strength(4.0f,4.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).jumpVelocityMultiplier(3).requiresTool().strength(4.0f,4.0f))
	};
	public static final Item[] items = {
			new BlockItem(blocks[0],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[1],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[2],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[3],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[4],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[5],new FabricItemSettings().group(ITEM_GROUP))
	};
	public static final String[] blocknames = {"moon_sand","moon_stone","moon_iron_ore","moon_gold_ore","teleporter_frame","moon_redstone_ore"};
	private static final String[] configurenames = {"moon_sand","moon_iron_ore","moon_gold_ore","moon_redstone_ore"};
	private static final ConfiguredFeature<?, ?>[] configuredFeatures = {
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[0].getDefaultState(),21)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[2].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[3].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[4].getDefaultState(),6))
	};

	public static PlacedFeature[] placedFeatures = {
			new PlacedFeature(RegistryEntry.of(configuredFeatures[0]),Arrays.asList(CountPlacementModifier.of(12),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[3]),Arrays.asList(CountPlacementModifier.of(8),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(32))))
	};

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
		CustomPortalBuilder.beginPortal().frameBlock(blocks[4]).lightWithItem(Items.DIAMOND).destDimID(new Identifier(MODID,"moon")).tintColor(Color.WHITE.getRGB()).registerPortal();
	}
}
