package com.brainsmash.broken_world;

import com.brainsmash.broken_world.blocks.TeleporterController;
import com.brainsmash.broken_world.blocks.entity.TeleporterControllerEntity;
import com.brainsmash.broken_world.blocks.fluid.AcidFluid;
import com.brainsmash.broken_world.blocks.fluid.IFluidBlock;
import com.brainsmash.broken_world.blocks.fluid.OilFluid;
import com.brainsmash.broken_world.blocks.fluid.PollutedWaterFluid;
import com.brainsmash.broken_world.screenhandlers.descriptions.TeleporterControllerGuiDescription;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "broken_world";
	private static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "itemgroup"), new Supplier<>() {
		@Override
		public ItemStack get() {
			return new ItemStack(blockitems[0]);
		}
	});

	public static final FlowableFluid[] still_fluid = {
			new OilFluid.Still(),
			new PollutedWaterFluid.Still(),
			new AcidFluid.Still()
	};

	public static final FlowableFluid[] flowing_fluid = {
			new OilFluid.Flowing(),
			new PollutedWaterFluid.Flowing(),
			new AcidFluid.Flowing()
	};

	public static final Block[] fluid_blocks = {
			new IFluidBlock(still_fluid[0], FabricBlockSettings.copyOf(Blocks.WATER).velocityMultiplier(0.1f).jumpVelocityMultiplier(0.1f)),
			new FluidBlock(still_fluid[1], FabricBlockSettings.copyOf(Blocks.WATER)),
			new IFluidBlock(still_fluid[2], FabricBlockSettings.copyOf(Blocks.WATER))
	};

	public static final Item[] bucket_item = {
			new BucketItem(still_fluid[0], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
			new BucketItem(still_fluid[1], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
			new BucketItem(still_fluid[2], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP))
	};

	public static final Block[] blocks = {
			new FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).strength(1.0f)),
			new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(2.0f,2.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(3.0f,3.0f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(5.0f,4.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(5.0f,4.0f)),
			new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f,2.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,3.0f)),
			new TeleporterController(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,3.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(6.0f,6.0f)),
			new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,10f)),
			new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f,2.0f)),
			new Block(FabricBlockSettings.of(Material.SOIL).sounds(BlockSoundGroup.MUD).strength(2.0f,2.0f))
	};
	public static final Item[] blockitems = {
			new BlockItem(blocks[0],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[1],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[2],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[3],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[4],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[5],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[6],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[7],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[8],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[9],new FabricItemSettings()),
			new BlockItem(blocks[10],new FabricItemSettings()),
			new BlockItem(blocks[11],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[12],new FabricItemSettings()),
			new BlockItem(blocks[13],new FabricItemSettings().group(ITEM_GROUP)),
			new BlockItem(blocks[14],new FabricItemSettings().group(ITEM_GROUP))
	};

	public static final Item[] items = {
			new Item(new FabricItemSettings().group(ITEM_GROUP)),
			new Item(new FabricItemSettings().group(ITEM_GROUP))
	};

	public static final Block[] baseblock = {};

	public static final String[] blocknames = {"moon_sand","moon_stone","moon_iron_ore","moon_gold_ore","teleporter_frame","moon_redstone_ore",
			"moon_sandstone","rusty_metal","teleporter_controller","moon_teleporter_frame","metallic_teleporter_frame","tungsten_ore","lush_teleporter_frame",
			"sulfuric_stone","sulfuric_soil"
	};

	public static final String[] fluidnames = {"oil","polluted_water","acid"};
	public static final Color[] fluidColor = {Color.BLACK,new Color(0,10,100),new Color(210,180,0)};
	public static final String[] itemnames = {"titanium_ingot","tungsten_ingot"};
	private static final String[] configurenames = {"moon_sand","moon_iron_ore","moon_gold_ore","moon_redstone_ore","tungsten_ore"};
	public static final List<String> noAirDimension = Arrays.asList("broken_world.moon_type");
	public static final List<String> noCloudDimension = Arrays.asList("broken_world.moon_type");
	private static final ConfiguredFeature<?, ?>[] configuredFeatures = {
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[0].getDefaultState(),21)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[2].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[3].getDefaultState(),9)),
			new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[5].getDefaultState(),6)),
			new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[11].getDefaultState(),8))
	};

	private static final PlacedFeature[] placedFeatures = {
			new PlacedFeature(RegistryEntry.of(configuredFeatures[0]),Arrays.asList(CountPlacementModifier.of(12),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[3]),Arrays.asList(CountPlacementModifier.of(8),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(32)))),
			new PlacedFeature(RegistryEntry.of(configuredFeatures[4]),Arrays.asList(CountPlacementModifier.of(20),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(64))))
	};


	public static ConcurrentHashMap<String, PortalLink> dimensions = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Double> dimensionGravity = new ConcurrentHashMap<>();
	public static BlockEntityType<TeleporterControllerEntity> TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE;
	//public static final ScreenHandlerType<TeleporterControllerScreenHandler> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(new Identifier(MODID,"teleporter_controller"), TeleporterControllerScreenHandler::new);
	public static final ScreenHandlerType<TeleporterControllerGuiDescription> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"teleport_controller"),new ScreenHandlerType<>((syncId, inventory) -> new TeleporterControllerGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));


	@Override
	public void onInitialize() {
		for(int i = 0;i<blocks.length;i++){
			Registry.register(Registry.BLOCK, new Identifier(MODID, blocknames[i]), blocks[i]);
			Registry.register(Registry.ITEM, new Identifier(MODID, blocknames[i]), blockitems[i]);
		}
		for(int i = 0;i<configuredFeatures.length;i++){
			Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier(MODID, configurenames[i]), configuredFeatures[i]);
			Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MODID, configurenames[i]),placedFeatures[i]);
		}
		for(int i = 0;i<items.length;i++){
			Registry.register(Registry.ITEM, new Identifier(MODID,itemnames[i]),items[i]);
		}
		for(int i = 0;i<still_fluid.length;i++) {
			Registry.register(Registry.FLUID, new Identifier(MODID, fluidnames[i]), still_fluid[i]);
			Registry.register(Registry.FLUID, new Identifier(MODID, "flowing_"+fluidnames[i]), flowing_fluid[i]);
			Registry.register(Registry.ITEM,new Identifier(MODID, fluidnames[i] + "_bucket"),bucket_item[i]);
			Registry.register(Registry.BLOCK,new Identifier(MODID,fluidnames[i]),fluid_blocks[i]);
		}

		CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[4]).destDimID(new Identifier("minecraft","overworld")).tintColor(Color.BLUE.getRGB()).registerPortal();
		CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[9]).destDimID(new Identifier(MODID,"moon")).tintColor(Color.WHITE.getRGB()).registerPortal();
		CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[10]).destDimID(new Identifier(MODID,"metallic")).tintColor(Color.ORANGE.getRGB()).registerPortal();
		CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[12]).destDimID(new Identifier(MODID,"lush")).tintColor(Color.GREEN.getRGB()).registerPortal();

		dimensions.put("broken_world:moon",new PortalLink(new Identifier(MODID,blocknames[9]),new Identifier(MODID,"moon"),Color.WHITE.getRGB()));
		dimensions.put("broken_world:metallic",new PortalLink(new Identifier(MODID,blocknames[10]),new Identifier(MODID,"metallic"),Color.ORANGE.getRGB()));
		dimensions.put("broken_world:lush",new PortalLink(new Identifier(MODID,blocknames[12]),new Identifier(MODID,"lush"),Color.GREEN.getRGB()));

		dimensionGravity.put("broken_world.moon_type",0.1);
		dimensionGravity.put("broken_world.metallic_type",0.8);

		TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"teleporter_controller"), FabricBlockEntityTypeBuilder.create(TeleporterControllerEntity::new,blocks[8]).build());

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,RegistryKey.of(Registry.PLACED_FEATURE_KEY,new Identifier(MODID, "tungsten_ore")));
	}
}
