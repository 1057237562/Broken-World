package com.brainsmash.broken_world;

import com.brainsmash.broken_world.blocks.TeleporterController;
import com.brainsmash.broken_world.blocks.entity.TeleporterControllerEntity;
import com.brainsmash.broken_world.screenhandlers.descriptions.TeleporterControllerGuiDescription;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
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
			new FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).strength(1.0f)),
			new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(2.0f,2.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(3.0f,3.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(4.0f,4.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(4.0f,4.0f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f,2.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,3.0f)),
			new TeleporterController(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,3.0f))
	};
	public static final Item[] items = {
			new BlockItem(blocks[0],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[1],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[2],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[3],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[4],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[5],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[6],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[7],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[8],new FabricItemSettings().group(ITEM_GROUP))
	};
	public static final String[] blocknames = {"moon_sand","moon_stone","moon_iron_ore","moon_gold_ore","teleporter_frame","moon_redstone_ore",
			"moon_sandstone","rusty_metal","teleporter_controller"
	};
	private static final String[] configurenames = {"moon_sand","moon_iron_ore","moon_gold_ore","moon_redstone_ore"};
	private static final ConfiguredFeature<?, ?>[] configuredFeatures = {
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[0].getDefaultState(),21)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[2].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[3].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[4].getDefaultState(),6))
	};

	private static final PlacedFeature[] placedFeatures = {
			new PlacedFeature(RegistryEntry.of(configuredFeatures[0]),Arrays.asList(CountPlacementModifier.of(12),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[3]),Arrays.asList(CountPlacementModifier.of(8),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(32))))
	};

	public static BlockEntityType<TeleporterControllerEntity> TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE;
	//public static final ScreenHandlerType<TeleporterControllerScreenHandler> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(new Identifier(MODID,"teleporter_controller"), TeleporterControllerScreenHandler::new);
	public static final ScreenHandlerType<TeleporterControllerGuiDescription> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"teleport_controller"),new ScreenHandlerType<>((syncId, inventory) -> new TeleporterControllerGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));
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
		CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[4]).destDimID(new Identifier(MODID,"moon")).tintColor(Color.WHITE.getRGB()).registerPortal();
		//CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[7]).lightWithItem(Items.GOLD_INGOT).destDimID(new Identifier(MODID,"metallic")).tintColor(Color.ORANGE.getRGB()).registerPortal();
		ServerTickEvents.START_WORLD_TICK.register(world -> {
			if(world.getDimensionKey().getValue().toTranslationKey().equals("broken_world.moon_type")){
				for(ServerPlayerEntity entity : world.getPlayers()){
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,50,1,false,false));
					entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,50,0,false,false));
				}
			}
		});

		TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"teleporter_controller"), FabricBlockEntityTypeBuilder.create(TeleporterControllerEntity::new,blocks[8]).build());
	}
}
