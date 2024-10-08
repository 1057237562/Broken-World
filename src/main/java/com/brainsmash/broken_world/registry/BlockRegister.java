package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.blocks.*;
import com.brainsmash.broken_world.blocks.electric.*;
import com.brainsmash.broken_world.blocks.electric.base.BatteryBlock;
import com.brainsmash.broken_world.blocks.electric.base.CableBlock;
import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.electric.generator.*;
import com.brainsmash.broken_world.blocks.entity.CloneVatBlockEntity;
import com.brainsmash.broken_world.blocks.entity.RollingDoorBlockEntity;
import com.brainsmash.broken_world.blocks.entity.UVBlockEntity;
import com.brainsmash.broken_world.blocks.entity.WoodenPipeBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.*;
import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.generator.*;
import com.brainsmash.broken_world.blocks.entity.magical.*;
import com.brainsmash.broken_world.blocks.gen.RubberSaplingGenerator;
import com.brainsmash.broken_world.blocks.magical.*;
import com.brainsmash.broken_world.blocks.magical.multiblock.ManaGeneratorMultiblock;
import com.brainsmash.broken_world.blocks.model.BottomTopBlock;
import com.brainsmash.broken_world.blocks.model.TeleporterFrameBlock;
import com.brainsmash.broken_world.blocks.multiblock.MultiblockUtil;
import com.brainsmash.broken_world.blocks.ores.MagnetiteBlock;
import com.brainsmash.broken_world.blocks.render.entity.*;
import com.brainsmash.broken_world.items.magical.MagicalSpawnerItem;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
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

    private static final AbstractBlock.Settings STANDARD_BLOCK = FabricBlockSettings.of(Material.METAL).sounds(
            BlockSoundGroup.METAL).strength(3.0f, 3.0f);

    public static final Block[] blocks = {
            // 0
            new FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).strength(1.0f)),
            new Block(FabricBlockSettings.copyOf(Blocks.STONE)),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE)),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.GOLD_ORE)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                    10f)),
            new RedstoneOreBlock(
                    FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(5.0f,
                            4.0f)),
            new BottomTopBlock(
                    AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(
                            2.0f, 2.0f)),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,
                    3.0f)),
            new TeleporterController(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f,
                            3.0f)),
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            // 10
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            new OreBlock(
                    FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(6.0f,
                            6.0f)),
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).requiresTool().strength(2.0f,
                    2.0f)),
            new Block(FabricBlockSettings.of(Material.SOIL).sounds(BlockSoundGroup.MUD).strength(2.0f, 2.0f)),
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.4375, false, 8),
            new CreativeBatteryBlock(
                    FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(3.0f,
                            3.0f)),
            new CreativeGeneratorBlock(
                    FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(100.0f,
                            100.0f).luminance(6)),
            new ConsumerBlock(STANDARD_BLOCK),
            // 20
            new GeneratorBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(2.0f, 2.0f)),
            new CrusherBlock(STANDARD_BLOCK),
            new SolarPanelBlock(
                    FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(1.0f,
                            2.0f)),
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            new SifterBlock(STANDARD_BLOCK.nonOpaque()),
            new ScannerBlock(STANDARD_BLOCK),
            new MinerBlock(STANDARD_BLOCK.nonOpaque()),
            new ChunkloaderBlock(STANDARD_BLOCK.nonOpaque()),
            new TeleportPlatformBlock(
                    FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.METAL).strength(3.0f,
                            3.0f)),
            new ThermalGeneratorBlock(STANDARD_BLOCK),
            // 30
            new PumpBlock(STANDARD_BLOCK),
            new HydroGeneratorBlock(STANDARD_BLOCK),
            new WindTurbineBlock(STANDARD_BLOCK),
            new AdvancedFurnaceBlock(STANDARD_BLOCK),
            new FabricatorBlock(STANDARD_BLOCK),
            new FabricatorExtensionBlock(STANDARD_BLOCK),
            new CentrifugeBlock(STANDARD_BLOCK.nonOpaque()),
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f,
                    3.0f).velocityMultiplier(1.1f).slipperiness(0.45f)),
            new MagnetiteBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE)),
            new Block(FabricBlockSettings.copyOf(Blocks.GLASS).strength(2.0f, 15.0f)),
            // 40
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            new Block(FabricBlockSettings.copyOf(Blocks.DIRT)),
            new Block(FabricBlockSettings.copyOf(Blocks.STONE)),
            new Block(FabricBlockSettings.copyOf(Blocks.BRICKS)),
            new RollingDoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_DOOR)),
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR).sounds(BlockSoundGroup.GLASS).nonOpaque()),
            new CloneVatBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.GLASS).nonOpaque()),
            new GlitchBlock(FabricBlockSettings.copyOf(Blocks.STONE)),
            new CodeBlock(FabricBlockSettings.copyOf(Blocks.STONE)),
            new DataUploadNodeBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            // 50
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            new SpawnPointerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).dropsNothing()),
            new GlassBlock(FabricBlockSettings.copyOf(Blocks.BLUE_ICE).nonOpaque().luminance(2)),
            new Block(FabricBlockSettings.copyOf(Blocks.STONE)),
            new Block(FabricBlockSettings.copyOf(Blocks.SNOW_BLOCK).luminance(11)),
            new Block(FabricBlockSettings.copyOf(Blocks.MAGMA_BLOCK)),
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            new TeleporterFrameBlock(
                    FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).dropsNothing().strength(2.0f,
                            10f)),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE)),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.COPPER_ORE)),
            // 60
            new InfusedCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque()),
            new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)),
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            new CompressorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()),
            new AssemblerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()),
            new ElectrolyzerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()),
            new ReactionKettleBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()),
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)),
            new RubberLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)),
            new CutRubberLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)),
            // 70
            new RubberLeaves(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)),
            new SaplingBlock(new RubberSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)),
            new ExtractorBlock(STANDARD_BLOCK),
            new WeaponryBlock(STANDARD_BLOCK),
            new WoodenPipeBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).nonOpaque()),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.375, false, 16),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.25, false, 32),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.4375f, true, 8),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.375f, true, 16),
            new CableBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.0f, 2.0f),
                    0.25f, true, 32),
            // 80
            new UVBlock(STANDARD_BLOCK),
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)),
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)),
            new RefineryBlock(STANDARD_BLOCK),
            new GasCollectorBlock(STANDARD_BLOCK),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE)),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE)),
            new BatteryBlock(STANDARD_BLOCK),
            new OreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE)),
            new ArcaneLectern(FabricBlockSettings.copyOf(Blocks.LECTERN)),
            // 90
            new MagicalSpawner(FabricBlockSettings.copyOf(Blocks.SPAWNER).nonOpaque()),
            new ColliderControllerBlock(STANDARD_BLOCK),
            new ColliderCoilBlock(STANDARD_BLOCK),
            new MortarBlock(STANDARD_BLOCK),
            new CrucibleBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).mapColor(MapColor.PURPLE),
                    CrucibleBehavior.CRUCIBLE_BEHAVIOR),
            new StoneBaseBlock(FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque(), false),
            new PillarBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.PALE_YELLOW).requiresTool().strength(
                    3.0f).sounds(BlockSoundGroup.BONE)),
            new XpCropBlock(),
            new XpHopper(FabricBlockSettings.copyOf(Blocks.HOPPER)),
            new LuminInjector(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque()),
            // 100
            new StoneBaseBlock(FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque(), true),
            new DimInfuser(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque()),
            new InfusingTable(FabricBlockSettings.copyOf(Blocks.STONE)),
            new XpContainer(FabricBlockSettings.copyOf(Blocks.CAULDRON)),

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
            new BlockItem(blocks[21], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[22], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[23], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[24], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[25], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[26], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[27], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[28], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[29], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[30], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[31], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[32], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[33], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[34], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[35], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[36], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[37], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[38], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[39], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[40], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[41], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[42], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[43], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[44], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[45], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[46], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[47], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[48], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[49], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[50], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[51], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[52], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[53], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[54], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[55], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[56], new FabricItemSettings()),
            new BlockItem(blocks[57], new FabricItemSettings()),
            new BlockItem(blocks[58], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[59], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[60], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[61], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[62], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[63], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[64], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[65], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[66], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[67], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[68], new FabricItemSettings()),
            new BlockItem(blocks[69], new FabricItemSettings()),
            new BlockItem(blocks[70], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[71], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[72], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[73], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[74], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[75], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[76], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[77], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[78], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[79], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[80], new FabricItemSettings()),
            new BlockItem(blocks[81], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[82], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[83], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[84], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[85], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[86], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[87], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[88], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[89], new FabricItemSettings().group(ITEM_GROUP)),
            new MagicalSpawnerItem(blocks[90], new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new BlockItem(blocks[91], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[92], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[93], new FabricItemSettings().group(ITEM_GROUP)),
            null,
            new BlockItem(blocks[95], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[96], new FabricItemSettings().group(ITEM_GROUP)),
            null,
            new BlockItem(blocks[98], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[99], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[100], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[101], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[102], new FabricItemSettings().group(ITEM_GROUP)),
            new BlockItem(blocks[103], new FabricItemSettings().group(ITEM_GROUP)),
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
            "advanced_furnace",
            "fabricator",
            "fabricator_extension",
            "centrifuge",
            "road",
            "magnetite",
            "reinforced_glass",
            "machine_shell",
            "charred_dirt",
            "cracked_concrete",
            "grassy_bricks",
            "rolling_door",
            "glass_door",
            "clone_vat",
            "glitch",
            "code",
            "data_upload_node",
            "core",
            "spawn_pointer",
            "isotropic_ice",
            "magnetic_stone",
            "solid_plasma",
            "volcanic_stone",
            "aurora_teleporter_frame",
            "floating_teleporter_frame",
            "kyanite_ore",
            "tin_ore",
            "infused_crystal",
            "kyanite_block",
            "tungsten_block",
            "compressor",
            "assembler",
            "electrolyzer",
            "reaction_kettle",
            "rubber_log",
            "natural_rubber_log",
            "cut_rubber_log",
            "rubber_leaves",
            "rubber_sapling",
            "extractor",
            "weaponry",
            "wooden_pipe",
            "double_copper_cable",
            "quad_copper_cable",
            "covered_copper_cable",
            "covered_double_copper_cable",
            "covered_quad_copper_cable",
            "uv",
            "rubber_planks",
            "steel_shell",
            "refinery",
            "gas_collector",
            "sulfur_ore",
            "aluminum_ore",
            "battery",
            "lead_ore",
            "arcane_lectern",
            "magical_spawner",
            "collider_controller",
            "collider_coil",
            "mortar",
            "crucible",
            "stone_base",
            "compressed_bone_block",
            "xp_crop",
            "xp_hopper",
            "lumin_injector",
            "black_stone_base",
            "dim_infuser",
            "infusing_table",
            "xp_container",
    };

    private static final ConfiguredFeature<?, ?>[] configuredFeatures = {
            new ConfiguredFeature<>(Feature.ORE,
                    new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, blocks[0].getDefaultState(),
                            21)),
            new ConfiguredFeature<>(Feature.ORE,
                    new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, blocks[2].getDefaultState(), 9)),
            new ConfiguredFeature<>(Feature.ORE,
                    new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, blocks[3].getDefaultState(), 9)),
            new ConfiguredFeature<>(Feature.ORE,
                    new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, blocks[5].getDefaultState(), 6)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.TUNGSTEN_ORE.ordinal()].getDefaultState(), 8)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.MAGNETITE.ordinal()].getDefaultState(), 4)),
            new ConfiguredFeature<>(Feature.ORE,
                    new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, blocks[58].getDefaultState(),
                            6)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.TIN_ORE.ordinal()].getDefaultState(), 9)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.SULFUR_ORE.ordinal()].getDefaultState(), 4)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.ALUMINUM_ORE.ordinal()].getDefaultState(), 6)),
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    blocks[BlockRegistry.LEAD_ORE.ordinal()].getDefaultState(), 6)),
    };

    private static final PlacedFeature[] placedFeatures = {
            new PlacedFeature(RegistryEntry.of(configuredFeatures[0]),
                    Arrays.asList(CountPlacementModifier.of(12), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[1]),
                    Arrays.asList(CountPlacementModifier.of(24), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[2]),
                    Arrays.asList(CountPlacementModifier.of(24), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[3]),
                    Arrays.asList(CountPlacementModifier.of(8), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(32)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[4]),
                    Arrays.asList(CountPlacementModifier.of(6), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(32)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[5]),
                    Arrays.asList(CountPlacementModifier.of(8), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(16)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[6]),
                    Arrays.asList(CountPlacementModifier.of(32), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.fixed(64), YOffset.TOP))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[7]),
                    Arrays.asList(CountPlacementModifier.of(20), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[8]),
                    Arrays.asList(CountPlacementModifier.of(10), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[9]),
                    Arrays.asList(CountPlacementModifier.of(10), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.fixed(8), YOffset.fixed(64)))),
            new PlacedFeature(RegistryEntry.of(configuredFeatures[10]),
                    Arrays.asList(CountPlacementModifier.of(6), SquarePlacementModifier.of(),
                            HeightRangePlacementModifier.uniform(YOffset.fixed(8), YOffset.fixed(64)))),
    };

    private static final String[] configurenames = {
            "moon_sand",
            "moon_iron_ore",
            "moon_gold_ore",
            "moon_redstone_ore",
            "tungsten_ore",
            "magnetite",
            "kyanite_ore",
            "tin_ore",
            "sulfur_ore",
            "aluminum_ore",
            "lead_ore"
    };

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
    public static BlockEntityType<FabricatorBlockEntity> FABRICATOR_ENTITY_TYPE;
    public static BlockEntityType<FabricatorExtensionBlockEntity> FABRICATOR_EXTENSION_ENTITY_TYPE;
    public static BlockEntityType<CentrifugeBlockEntity> CENTRIFUGE_ENTITY_TYPE;
    public static BlockEntityType<RollingDoorBlockEntity> ROLLING_DOOR_ENTITY_TYPE;
    public static BlockEntityType<CloneVatBlockEntity> CLONE_VAT_ENTITY_TYPE;
    public static BlockEntityType<InfusedCrystalBlockEntity> INFUSED_CRYSTAL_ENTITY_TYPE;

    public static BlockEntityType<CompressorBlockEntity> COMPRESSOR_ENTITY_TYPE;
    public static BlockEntityType<AssemblerBlockEntity> ASSEMBLER_ENTITY_TYPE;
    public static BlockEntityType<ElectrolyzerBlockEntity> ELECTROLYZER_ENTITY_TYPE;
    public static BlockEntityType<ReactionKettleBlockEntity> REACTION_KETTLE_ENTITY_TYPE;
    public static BlockEntityType<ExtractorBlockEntity> EXTRACTOR_ENTITY_TYPE;
    public static BlockEntityType<WeaponryBlockEntity> WEAPONRY_ENTITY_TYPE;
    public static BlockEntityType<WoodenPipeBlockEntity> WOODEN_PIPE_ENTITY_TYPE;
    public static BlockEntityType<UVBlockEntity> UV_ENTITY_TYPE;
    public static BlockEntityType<GasCollectorBlockEntity> GAS_COLLECTOR_ENTITY_TYPE;
    public static BlockEntityType<RefineryBlockEntity> REFINERY_ENTITY_TYPE;
    public static BlockEntityType<ArcaneLecternEntity> ARCANE_LECTERN_ENTITY_TYPE;
    public static BlockEntityType<MagicalSpawnerEntity> MAGICAL_SPAWNER_ENTITY_TYPE;
    public static BlockEntityType<ColliderControllerBlockEntity> COLLIDER_CONTROLLER_ENTITY_TYPE;
    public static BlockEntityType<ColliderCoilBlockEntity> COLLIDER_COIL_ENTITY_TYPE;
    public static BlockEntityType<MortarBlockEntity> MORTAR_ENTITY_TYPE;
    public static BlockEntityType<CrucibleBlockEntity> CRUCIBLE_ENTITY_TYPE;
    public static BlockEntityType<StoneBaseBlockEntity> STONE_BASE_ENTITY_TYPE;
    public static BlockEntityType<StoneBaseBlockEntity> BLACK_STONE_BASE_ENTITY_TYPE;
    public static BlockEntityType<XpHopperEntity> XP_HOPPER_ENTITY_TYPE;
    public static BlockEntityType<LuminInjectorEntity> LUMIN_INJECTOR_ENTITY_TYPE;
    public static BlockEntityType<DimInfuserEntity> DIM_INFUSER_ENTITY_TYPE;
    public static BlockEntityType<InfusingTableEntity> INFUSING_TABLE_ENTITY_TYPE;
    public static BlockEntityType<XpContainerEntity> XP_CONTAINER_ENTITY_TYPE;

    public static void registerBlocks() {
        for (int i = 0; i < blocks.length; i++) {
            Registry.register(Registry.BLOCK, new Identifier(MODID, blocknames[i]), blocks[i]);
            if (blockitems[i] != null)
                Registry.register(Registry.ITEM, new Identifier(MODID, blocknames[i]), blockitems[i]);
        }
        for (int i = 0; i < configuredFeatures.length; i++) {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MODID, configurenames[i]),
                    configuredFeatures[i]);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MODID, configurenames[i]),
                    placedFeatures[i]);
        }

        TELEPORTER_CONTROLLER_ENTITY_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "teleporter_controller"),
                FabricBlockEntityTypeBuilder.create(TeleporterControllerBlockEntity::new, blocks[8]).build());
        CABLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "cable"),
                FabricBlockEntityTypeBuilder.create(CableBlockEntity::new, blocks[16]).build());
        CREATIVE_BATTERY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "creative_battery"),
                FabricBlockEntityTypeBuilder.create(CreativeBatteryBlockEntity::new, blocks[17]).build());
        CREATIVE_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "creative_generator"),
                FabricBlockEntityTypeBuilder.create(CreativeGeneratorBlockEntity::new, blocks[18]).build());
        CONSUMER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "basic_machine"),
                FabricBlockEntityTypeBuilder.create(ConsumerBlockEntity::new, blocks[19]).build());
        GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "generator"),
                FabricBlockEntityTypeBuilder.create(GeneratorEntity::new, blocks[20]).build());
        CRUSHER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "crusher"),
                FabricBlockEntityTypeBuilder.create(CrusherBlockEntity::new, blocks[21]).build());
        SOLAR_PANEL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "solar_panel"),
                FabricBlockEntityTypeBuilder.create(SolarPanelEntity::new, blocks[22]).build());
        SIFTER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "sifter"),
                FabricBlockEntityTypeBuilder.create(SifterBlockEntity::new, blocks[24]).build());
        SCANNER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "scanner"),
                FabricBlockEntityTypeBuilder.create(ScannerBlockEntity::new, blocks[25]).build());
        MINER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "miner"),
                FabricBlockEntityTypeBuilder.create(MinerBlockEntity::new, blocks[26]).build());
        LOADER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "chunkloader"),
                FabricBlockEntityTypeBuilder.create(ChunkloaderBlockEntity::new, blocks[27]).build());
        TELEPORT_PLATFORM_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "teleport_platform"),
                FabricBlockEntityTypeBuilder.create(TeleportPlatformBlockEntity::new, blocks[28]).build());
        THERMAL_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "thermal_generator"),
                FabricBlockEntityTypeBuilder.create(ThermalGeneratorEntity::new, blocks[29]).build());
        FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> entity.fluidStorage,
                THERMAL_GENERATOR_ENTITY_TYPE);
        PUMP_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "pump"),
                FabricBlockEntityTypeBuilder.create(PumpBlockEntity::new, blocks[30]).build());
        FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> entity.fluidStorage, PUMP_ENTITY_TYPE);
        HYDRO_GENERATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "hydro_generator"),
                FabricBlockEntityTypeBuilder.create(HydroGeneratorEntity::new, blocks[31]).build());
        WIND_TURBINE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "wind_turbine"),
                FabricBlockEntityTypeBuilder.create(WindTurbineEntity::new, blocks[32]).build());
        ADVANCED_FURNACE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "advanced_furnace"),
                FabricBlockEntityTypeBuilder.create(AdvancedFurnaceBlockEntity::new, blocks[33]).build());
        FABRICATOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "fabricator"),
                FabricBlockEntityTypeBuilder.create(FabricatorBlockEntity::new, blocks[34]).build());
        FABRICATOR_EXTENSION_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "fabricator_extension"),
                FabricBlockEntityTypeBuilder.create(FabricatorExtensionBlockEntity::new, blocks[35]).build());
        CENTRIFUGE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "centrifuge"),
                FabricBlockEntityTypeBuilder.create(CentrifugeBlockEntity::new, blocks[36]).build());
        FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> entity.fluidStorage, CENTRIFUGE_ENTITY_TYPE);

        ROLLING_DOOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "rolling_door"),
                FabricBlockEntityTypeBuilder.create(RollingDoorBlockEntity::new, blocks[44]).build());
        CLONE_VAT_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "clone_vat"),
                FabricBlockEntityTypeBuilder.create(CloneVatBlockEntity::new, blocks[46]).build());

        INFUSED_CRYSTAL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "infused_crystal"),
                FabricBlockEntityTypeBuilder.create(InfusedCrystalBlockEntity::new, blocks[60]).build());
        COMPRESSOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "compressor"),
                FabricBlockEntityTypeBuilder.create(CompressorBlockEntity::new, blocks[63]).build());
        ASSEMBLER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "assembler"),
                FabricBlockEntityTypeBuilder.create(AssemblerBlockEntity::new, blocks[64]).build());
        ELECTROLYZER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "electrolyzer"),
                FabricBlockEntityTypeBuilder.create(ElectrolyzerBlockEntity::new, blocks[65]).build());
        REACTION_KETTLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "reaction_kettle"),
                FabricBlockEntityTypeBuilder.create(ReactionKettleBlockEntity::new, blocks[66]).build());
        EXTRACTOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "extractor"),
                FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, blocks[72]).build());
        WEAPONRY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "weaponry"),
                FabricBlockEntityTypeBuilder.create(WeaponryBlockEntity::new, blocks[73]).build());
        WOODEN_PIPE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "wooden_pipe"),
                FabricBlockEntityTypeBuilder.create(WoodenPipeBlockEntity::new, blocks[74]).build());
        FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> entity.fluidStorage, WOODEN_PIPE_ENTITY_TYPE);
        UV_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "uv"),
                FabricBlockEntityTypeBuilder.create(UVBlockEntity::new, blocks[80]).build());
        GAS_COLLECTOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "gas_collector"),
                FabricBlockEntityTypeBuilder.create(GasCollectorBlockEntity::new, blocks[84]).build());
        REFINERY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "refinery"),
                FabricBlockEntityTypeBuilder.create(RefineryBlockEntity::new, blocks[83]).build());
        BATTERY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "battery"),
                FabricBlockEntityTypeBuilder.create(BatteryBlockEntity::new, blocks[87]).build());
        ARCANE_LECTERN_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "arcane_lectern"),
                FabricBlockEntityTypeBuilder.create(ArcaneLecternEntity::new, blocks[89]).build());
        MAGICAL_SPAWNER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "magical_spawner"),
                FabricBlockEntityTypeBuilder.create(MagicalSpawnerEntity::new, blocks[90]).build());
        FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> entity.xpStorage, MAGICAL_SPAWNER_ENTITY_TYPE);

        MultiblockUtil.registerMultiblock(new Identifier(MODID, "mana_generator"), ManaGeneratorMultiblock::new);
        COLLIDER_CONTROLLER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "collider_controller"),
                FabricBlockEntityTypeBuilder.create(ColliderControllerBlockEntity::new,
                        get(BlockRegistry.COLLIDER_CONTROLLER)).build());
        COLLIDER_COIL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "collider_coil"),
                FabricBlockEntityTypeBuilder.create(ColliderCoilBlockEntity::new,
                        get(BlockRegistry.COLLIDER_COIL)).build());
        MORTAR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "mortar"),
                FabricBlockEntityTypeBuilder.create(MortarBlockEntity::new, get(BlockRegistry.MORTAR)).build());
        CRUCIBLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "crucible"),
                FabricBlockEntityTypeBuilder.create(CrucibleBlockEntity::new, get(BlockRegistry.CRUCIBLE)).build());
        STONE_BASE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "stone_base"),
                FabricBlockEntityTypeBuilder.create((pos, block) -> new StoneBaseBlockEntity(pos, block, false),
                        get(BlockRegistry.STONE_BASE)).build());
        BLACK_STONE_BASE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "black_stone_base"),
                FabricBlockEntityTypeBuilder.create((pos, block) -> new StoneBaseBlockEntity(pos, block, true),
                        get(BlockRegistry.BLACK_STONE_BASE)).build());
        XP_HOPPER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "xp_hopper"),
                FabricBlockEntityTypeBuilder.create(XpHopperEntity::new, get(BlockRegistry.XP_HOPPER)).build());
        LUMIN_INJECTOR_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "lumin_injector"), FabricBlockEntityTypeBuilder.create(LuminInjectorEntity::new,
                        get(BlockRegistry.LUMIN_INJECTOR)).build());
        DIM_INFUSER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "dim_infuser"),
                FabricBlockEntityTypeBuilder.create(DimInfuserEntity::new, get(BlockRegistry.DIM_INFUSER)).build());
        INFUSING_TABLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MODID, "infusing_table"), FabricBlockEntityTypeBuilder.create(InfusingTableEntity::new,
                        get(BlockRegistry.INFUSING_TABLE)).build());
        XP_CONTAINER_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "xp_container"),
                FabricBlockEntityTypeBuilder.create(XpContainerEntity::new, get(BlockRegistry.XP_CONTAINER)).build());
    }

    public static void registBlocksClientSide() {
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CREATIVE_BATTERY.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CREATIVE_GENERATOR.ordinal()],
                RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.SCANNER.ordinal()], RenderLayer.getSolid());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.WIND_TURBINE.ordinal()], RenderLayer.getSolid());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.SIFTER.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.TELEPORT_PLATFORM.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CHUNKLOADER.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.MINER.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.REINFORCED_GLASS.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.GLASS_DOOR.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CLONE_VAT.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.ISOTROPIC_ICE.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.CENTRIFUGE.ordinal()], RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.INFUSED_CRYSTAL.ordinal()], RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.WEAPONRY.ordinal()], RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.MAGICAL_SPAWNER.ordinal()],
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(blocks[BlockRegistry.XP_CROP.ordinal()], RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.get(BlockRegistry.XP_HOPPER), RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.get(BlockRegistry.LUMIN_INJECTOR),
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.get(BlockRegistry.DIM_INFUSER),
                RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.get(BlockRegistry.XP_CONTAINER),
                RenderLayer.getTranslucent());
        EntityModelLayerRegistry.registerModelLayer(CreativeGeneratorBlockEntityRenderer.CREATIVE_GENERATOR,
                CreativeGeneratorBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WindTurbineEntityRenderer.WIND_TURBINE,
                WindTurbineEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CompressorBlockEntityRenderer.COMPRESSOR,
                CompressorBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CentrifugeBlockEntityRenderer.CENTRIFUGE,
                CentrifugeBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(InfusedCrystalBlockEntityRenderer.INFUSED_CRYSTAL,
                InfusedCrystalBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WeaponryBlockEntityRenderer.WEAPONRY,
                WeaponryBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UVBlockEntityRenderer.UV,
                UVBlockEntityRenderer::getTexturedModelData);
        BlockEntityRendererRegistry.register(CREATIVE_BATTERY_ENTITY_TYPE, CreativeBatteryBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CREATIVE_GENERATOR_ENTITY_TYPE, CreativeGeneratorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(SCANNER_ENTITY_TYPE, ScannerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(WIND_TURBINE_ENTITY_TYPE, WindTurbineEntityRenderer::new);
        BlockEntityRendererRegistry.register(SIFTER_ENTITY_TYPE, SifterBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CLONE_VAT_ENTITY_TYPE, CloneVatBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(COMPRESSOR_ENTITY_TYPE, CompressorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CENTRIFUGE_ENTITY_TYPE, CentrifugeBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(INFUSED_CRYSTAL_ENTITY_TYPE, InfusedCrystalBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(WEAPONRY_ENTITY_TYPE, WeaponryBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(UV_ENTITY_TYPE, UVBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(MORTAR_ENTITY_TYPE, MortarBlockEnityRenderer::new);
        BlockEntityRendererRegistry.register(STONE_BASE_ENTITY_TYPE, StoneBaseBlockEnityRenderer::new);
        BlockEntityRendererRegistry.register(XP_HOPPER_ENTITY_TYPE, XpContainerEntityRenderer::new);
        BlockEntityRendererRegistry.register(MAGICAL_SPAWNER_ENTITY_TYPE, XpContainerEntityRenderer::new);
        BlockEntityRendererRegistry.register(LUMIN_INJECTOR_ENTITY_TYPE, LuminInjectorBlockEnityRenderer::new);
        BlockEntityRendererRegistry.register(BLACK_STONE_BASE_ENTITY_TYPE, StoneBaseBlockEnityRenderer::new);
        BlockEntityRendererRegistry.register(DIM_INFUSER_ENTITY_TYPE, DimInfuserBlockEnityRenderer::new);
        BlockEntityRendererRegistry.register(XP_CONTAINER_ENTITY_TYPE, XpContainerEntityRenderer::new);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null && world.getBlockEntity(
                    pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                return crucibleBlockEntity.getFluidColor();
            }
            return 0x7442FF;
        }, get(BlockRegistry.CRUCIBLE));

        registTreeColor();
    }

    public static void registTreeColor() {
        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world,
                        pos) : FoliageColors.getDefaultColor(), blocks[BlockRegistry.RUBBER_LEAVES.ordinal()]);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getDefaultColor(),
                blockitems[BlockRegistry.RUBBER_LEAVES.ordinal()]);
    }

    public static Block get(BlockRegistry block) {
        return blocks[block.ordinal()];
    }
}
