package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.blocks.client.render.entity.*;
import com.brainsmash.broken_world.blocks.electric.TeleporterController;
import com.brainsmash.broken_world.blocks.electric.*;
import com.brainsmash.broken_world.blocks.electric.base.CableBlock;
import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.electric.generator.*;
import com.brainsmash.broken_world.blocks.entity.electric.TeleporterControllerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.*;
import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.generator.*;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

import java.util.Arrays;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.ItemRegister.ITEM_GROUP;

public class BlockRegister {

    public static final Block[] blocks = {
            new FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).strength(1.0f)),
            new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(2.0f, 2.0f)),
            new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(3.0f, 3.0f)),
            new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(5.0f, 4.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f, 10f)),
            new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(5.0f, 4.0f)),
            new Block(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f, 2.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f, 3.0f)),
            new TeleporterController(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f, 3.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f, 10f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f, 10f)),
            new OreBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(6.0f, 6.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f, 10f)),
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f, 2.0f)),
            new Block(FabricBlockSettings.of(Material.SOIL).sounds(BlockSoundGroup.MUD).strength(2.0f, 2.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f, 10f)),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(2.0f, 2.0f)),
            new CreativeBatteryBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(3.0f, 3.0f)),
            new CreativeGeneratorBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(100.0f, 100.0f).luminance(6)),
            new ConsumerBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f, 3.0f)),
            new GeneratorBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(2.0f, 2.0f)),
            new CrusherBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new SolarPanelBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(1.0f,2.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(2.0f,2.0f)),
            new SifterBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new ScannerBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new MinerBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new ChunkloaderBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new TeleportPlatformBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new ThermalGeneratorBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new PumpBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new HydroGeneratorBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new WindTurbineBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
            new AdvancedFurnaceBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,3.0f)),
    };
    public static final Item[] blockitems = {
            new BlockItem(blocks[0], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[1], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[2], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[3], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[4], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[5], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[6], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[7], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[8], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[9], new FabricItemSettings()),
            new BlockItem(blocks[10], new FabricItemSettings()),
            new BlockItem(blocks[11], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[12], new FabricItemSettings()),
            new BlockItem(blocks[13], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[14], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[15], new FabricItemSettings()),
            new BlockItem(blocks[16], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[17], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[18], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[19], new FabricItemSettings()),
            new BlockItem(blocks[20], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[21],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[22],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[23],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[24],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[25],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[26],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[27],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[28],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[29],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[30],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[31],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[32],new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[33],new FabricItemSettings().group(ITEM_GROUP)),
    };

    public static final String[] blocknames = {
            "moon_sand",
            "moon_stone",
            "moon_iron_ore",
            "moon_gold_ore",
            "teleporter_frame",
            "moon_redstone_ore",
            "moon_sandstone",
            "rusty_metal",
            "teleporter_controller",
            "moon_teleporter_frame",
            "metallic_teleporter_frame",
            "tungsten_ore",
            "lush_teleporter_frame",
            "sulfuric_stone",
            "sulfuric_soil",
            "sulfuric_teleporter_frame",
            "copper_cable",
            "creative_battery",
            "creative_generator",
            "basic_machine",
            "generator",
            "crusher",
            "solar_panel",
            "iron_sheet_box",
            "sifter",
            "scanner",
            "miner",
            "chunkloader",
            "teleport_platform",
            "thermal_generator",
            "pump",
            "hydro_generator",
            "wind_turbine",
            "advanced_furnace"
    };

    private static final ConfiguredFeature<?, ?>[] configuredFeatures = {
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[0].getDefaultState(),21)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[2].getDefaultState(),9)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[3].getDefaultState(),9)),
            new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[5].getDefaultState(),6)),
            new ConfiguredFeature<>(Feature.ORE,new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,blocks[11].getDefaultState(),8))
    };

    private static final PlacedFeature[] placedFeatures = {
            new PlacedFeature(RegistryEntry.of(configuredFeatures[0]), Arrays.asList(CountPlacementModifier.of(12), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),Arrays.asList(CountPlacementModifier.of(24),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[3]),Arrays.asList(CountPlacementModifier.of(8),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(32)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[4]),Arrays.asList(CountPlacementModifier.of(20),SquarePlacementModifier.of(),HeightRangePlacementModifier.uniform(YOffset.getBottom(),YOffset.fixed(64))))
    };

    private static final String[] configurenames = {"moon_sand","moon_iron_ore","moon_gold_ore","moon_redstone_ore","tungsten_ore"};

    public static BlockEntityType<TeleporterControllerBlockEntity> TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<CableBlockEntity> CABLE_ENTITY_TYPE;
    public static BlockEntityType<BatteryBlockEntity> BATTERY_ENTITY_TYPE;
    public static BlockEntityType<CreativeBatteryBlockEntity> CREATIVE_BATTERY_ENTITY_TYPE;
    public static BlockEntityType<CreativeGeneratorBlockEntity> CREATIVE_GENERATOR_ENTITY_TYPE;
    public static BlockEntityType<ConsumerBlockEntity> CONSUMER_ENTITY_TYPE;
    public static BlockEntityType<PowerBlockEntity> POWER_ENTITY_TYPE;
    public static BlockEntityType<GeneratorEntity> GENERATOR_ENTITY_TYPE;
    public static BlockEntityType<CrusherBlockEntity> CRUSHER_ENTITY_TYPE;
    public static BlockEntityType<SolarPanelEntity> SOLAR_PANEL_ENTITY_TYPE;
    public static BlockEntityType<SifterBlockEntity> SIFTER_ENTITY_TYPE;
    public static BlockEntityType<ScannerBlockEntity> SCANNER_ENTITY_TYPE;
    public static BlockEntityType<MinerBlockEntity> MINER_ENTITY_TYPE;
    public static BlockEntityType<ChunkloaderBlockEntity> LOADER_ENTITY_TYPE;
    public static BlockEntityType<TeleportPlatformBlockEntity> TELEPORT_PLATFORM_ENTITY_TYPE;
    public static BlockEntityType<ThermalGeneratorEntity> THERMAL_GENERATOR_ENTITY_TYPE;
    public static BlockEntityType<PumpBlockEntity> PUMP_ENTITY_TYPE;
    public static BlockEntityType<HydroGeneratorEntity> HYDRO_GENERATOR_ENTITY_TYPE;
    public static BlockEntityType<WindTurbineEntity> WIND_TURBINE_ENTITY_TYPE;
    public static BlockEntityType<AdvancedFurnaceBlockEntity> ADVANCED_FURNACE_ENTITY_TYPE;

    public static void RegistBlocks() {
        for (int i = 0; i < blocks.length; i++) {
            Registry.register(Registry.BLOCK, new Identifier(MODID, blocknames[i]), blocks[i]);
            Registry.register(Registry.ITEM, new Identifier(MODID, blocknames[i]), blockitems[i]);
        }
        for (int i = 0; i < configuredFeatures.length; i++) {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MODID, configurenames[i]), configuredFeatures[i]);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MODID, configurenames[i]), placedFeatures[i]);
        }

        TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"teleporter_controller"), FabricBlockEntityTypeBuilder.create(TeleporterControllerBlockEntity::new,blocks[8]).build());
        CABLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"cable"),FabricBlockEntityTypeBuilder.create(CableBlockEntity::new,blocks[16]).build());
        CREATIVE_BATTERY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"creative_battery"),FabricBlockEntityTypeBuilder.create(CreativeBatteryBlockEntity::new,blocks[17]).build());
        CREATIVE_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID, "creative_generator"),FabricBlockEntityTypeBuilder.create(CreativeGeneratorBlockEntity::new,blocks[18]).build());
        CONSUMER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"basic_machine"), FabricBlockEntityTypeBuilder.create(ConsumerBlockEntity::new,blocks[19]).build());
        GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"generator"), FabricBlockEntityTypeBuilder.create(GeneratorEntity::new,blocks[20]).build());
        CRUSHER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"crusher"),FabricBlockEntityTypeBuilder.create(CrusherBlockEntity::new,blocks[21]).build());
        SOLAR_PANEL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"solar_panel"),FabricBlockEntityTypeBuilder.create(SolarPanelEntity::new,blocks[22]).build());
        SIFTER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"sifter"),FabricBlockEntityTypeBuilder.create(SifterBlockEntity::new,blocks[24]).build());
        SCANNER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"scanner"),FabricBlockEntityTypeBuilder.create(ScannerBlockEntity::new,blocks[25]).build());
        MINER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"miner"),FabricBlockEntityTypeBuilder.create(MinerBlockEntity::new,blocks[26]).build());
        LOADER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"chunkloader"),FabricBlockEntityTypeBuilder.create(ChunkloaderBlockEntity::new,blocks[27]).build());
        TELEPORT_PLATFORM_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"teleport_platform"),FabricBlockEntityTypeBuilder.create(TeleportPlatformBlockEntity::new,blocks[28]).build());
        THERMAL_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"thermal_generator"),FabricBlockEntityTypeBuilder.create(ThermalGeneratorEntity::new,blocks[29]).build());
        PUMP_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"pump"),FabricBlockEntityTypeBuilder.create(PumpBlockEntity::new,blocks[30]).build());
        HYDRO_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"hydro_generator"),FabricBlockEntityTypeBuilder.create(HydroGeneratorEntity::new,blocks[31]).build());
        WIND_TURBINE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"wind_turbine"),FabricBlockEntityTypeBuilder.create(WindTurbineEntity::new,blocks[32]).build());
        ADVANCED_FURNACE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MODID,"advanced_furnace"),FabricBlockEntityTypeBuilder.create(AdvancedFurnaceBlockEntity::new,blocks[33]).build());
    }

    public static void RegistBlocksClientSide() {
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CREATIVE_BATTERY.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CREATIVE_GENERATOR.ordinal()], RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.SCANNER.ordinal()], RenderLayer.getSolid());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.WIND_TURBINE.ordinal()],RenderLayer.getSolid());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.SIFTER.ordinal()],RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.TELEPORT_PLATFORM.ordinal()],RenderLayer.getTranslucent());
        EntityModelLayerRegistry.registerModelLayer(CreativeGeneratorBlockEntityRenderer.CREATIVE_GENERATOR, CreativeGeneratorBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WindTurbineEntityRenderer.WIND_TURBINE,WindTurbineEntityRenderer::getTexturedModelData);
        BlockEntityRendererRegistry.register(CREATIVE_BATTERY_ENTITY_TYPE, CreativeBatteryBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CREATIVE_GENERATOR_ENTITY_TYPE, CreativeGeneratorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(SCANNER_ENTITY_TYPE, ScannerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(WIND_TURBINE_ENTITY_TYPE, WindTurbineEntityRenderer::new);
        BlockEntityRendererRegistry.register(SIFTER_ENTITY_TYPE, SifterBlockEntityRenderer::new);
    }
}
