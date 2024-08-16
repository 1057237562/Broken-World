package com.brainsmash.broken_world;

import com.brainsmash.broken_world.blocks.magical.CrucibleBehavior;
import com.brainsmash.broken_world.blocks.multiblock.MultiblockResourceReloadListener;
import com.brainsmash.broken_world.blocks.multiblock.MultiblockUtil;
import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.entity.vehicle.VehicleEntity;
import com.brainsmash.broken_world.items.weapons.guns.GunItem;
import com.brainsmash.broken_world.recipe.*;
import com.brainsmash.broken_world.registry.*;
import com.brainsmash.broken_world.registry.enums.OreTypeRegistry;
import com.brainsmash.broken_world.resourceloader.GasResourceLoader;
import com.brainsmash.broken_world.screenhandlers.descriptions.*;
import com.brainsmash.broken_world.util.BonusHelper;
import com.brainsmash.broken_world.util.EntityHelper;
import com.brainsmash.broken_world.worldgen.BWDensityFunctionTypes;
import com.brainsmash.broken_world.worldgen.features.VolcanoFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final String MODID = "broken_world";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


    public static final ScreenHandlerType<TeleporterControllerGuiDescription> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "teleport_controller"),
            new ExtendedScreenHandlerType<>(TeleporterControllerGuiDescription::new));
    public static final ScreenHandlerType<BatteryGuiDescription> BATTERY_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "creative_battery"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new BatteryGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<GeneratorGuiDescription> GENERATOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "generator"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new GeneratorGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<CrusherGuiDescription> CRUSHER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "crusher"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new CrusherGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<SifterGuiDescription> SIFTER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "sifter"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new SifterGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<MinerGuiDescription> MINER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "miner"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new MinerGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ExtendedScreenHandlerType<TeleportPlatformGuiDescription> TELEPORT_PLATFORM_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "teleport_platform"),
            new ExtendedScreenHandlerType<>(TeleportPlatformGuiDescription::new));
    public static final ScreenHandlerType<ThermalGeneratorGuiDescription> THERMAL_GENERATOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "thermal_generator"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new ThermalGeneratorGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<PumpGuiDescription> PUMP_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "pump"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new PumpGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<AdvancedFurnaceGuiDescription> ADVANCED_FURNACE_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "advanced_furnace"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new AdvancedFurnaceGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));

    public static final ScreenHandlerType<AssemblerGuiDescription> ASSEMBLER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "assembler"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new AssemblerGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));

    public static final ScreenHandlerType<CompressorGuiDescription> COMPRESSOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "compressor"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new CompressorGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<FabricatorGuiDescription> FABRICATOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "fabricator"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new FabricatorGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<CentrifugeGuiDescription> CENTRIFUGE_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "centrifuge"),
            new ExtendedScreenHandlerType<>(CentrifugeGuiDescription::new));
    public static final ScreenHandlerType<ExtractorGuiDescription> EXTRACTOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "extractor"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new ExtractorGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<GasCollectorGuiDescription> GAS_COLLECTOR_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "gas_collector"),
            new ExtendedScreenHandlerType<>(GasCollectorGuiDescription::new));

    public static final ScreenHandlerType<RefineryGuiDescription> REFINERY_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "refinery"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new RefineryGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<WeaponryGuiDescription> WEAPONRY_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "weaponry"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new WeaponryGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));

    public static final ScreenHandlerType<ReactionKettleGuiDescription> REACTION_KETTLE_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "reaction_kettle"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new ReactionKettleGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<ElectrolyzerGuiDescription> ELECTROLYZER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "electrolyzer"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new ElectrolyzerGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<ColliderControllerGuiDescription> COLLIDER_CONTROLLER_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "collider_controller"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new ColliderControllerGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));
    public static final ScreenHandlerType<InfusingTableGuiDescription> INFUSING_TABLE_GUI_DESCRIPTION = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "infusing_table"), new ScreenHandlerType<>(
                    ((syncId, playerInventory) -> new InfusingTableGuiDescription(syncId, playerInventory,
                            ScreenHandlerContext.EMPTY))));


    public static final ScreenHandlerType<WandGuiDescription> ROOKIE_WAND_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "rookie_wand"),
            new ScreenHandlerType<>(WandGuiDescription::createRookieWand));
    public static final ScreenHandlerType<WandGuiDescription> EXPERT_WAND_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "expert_wand"),
            new ScreenHandlerType<>(WandGuiDescription::createExpertWand));
    public static final ScreenHandlerType<WandGuiDescription> MASTER_WAND_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "master_wand"),
            new ScreenHandlerType<>(WandGuiDescription::createMasterWand));
    public static final ScreenHandlerType<WandGuiDescription> GRANDMASTER_WAND_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER, new Identifier(MODID, "grandmaster_wand"),
            new ScreenHandlerType<>(WandGuiDescription::createGrandMasterWand));

    @Override
    public void onInitialize() {
        BlockRegister.registerBlocks();
        ItemRegister.registerItem();
        FluidRegister.registerFluid();
        DimensionRegister.registerDimension();
        EntityRegister.registerEntities();
        EntityRegister.registSpawnRegistration();
        TreeRegister.registerTrees();
        PointOfInterestRegister.registerPlacesOfInterest();
        SoundRegister.registerSoundEvents();
        EnchantmentRegister.registerEnchantments();
        ParticleRegister.registerParticles();
        CommandRegister.registerArguments();
        CommandRegister.registerCommands();

        MultiblockUtil.registerMultiblock();

        AdvancedFurnaceRecipe.registerAdvancedFurnaceRecipe();
        CrusherRecipe.registerCrusherRecipes();
        SifterRecipe.registerSifterRecipes();
        CentrifugeRecipe.registerCentrifugeRecipes();
        AssemblerRecipe.registerAssemblerRecipes();
        CompressorRecipe.registerCompressorRecipes();
        ExtractorRecipe.registerExtractorRecipes();
        GasCollectorRecipe.registerGasCollectorRecipes();
        RefineryRecipe.registerRefineryRecipes();
        ReactionRecipe.registerReactionRecipes();
        ElectrolyzerRecipe.registerElectrolyzerRecipes();
        GrindRecipe.registerGrindingRecipe();
        FabricatorRecipe.register();
        WeaponryRecipe.register();
        ColliderRecipe.register();
        LuminInjectorRecipe.register();

        CrucibleBehavior.registerBehaviour();

        GasResourceLoader.register();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
                new MultiblockResourceReloadListener());

        OreTypeRegistry.registerOreType();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "tungsten_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "magnetite")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "tin_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "sulfur_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "aluminum_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "lead_ore")));
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "rubber_tree")));

        BWDensityFunctionTypes.register();
        VolcanoFeature.register();

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "fire_key_pressed"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.getMainHandStack().getItem() instanceof GunItem gunItem) {
                        gunItem.fire(player.world, player);
                        if (player.getOffHandStack().getItem() instanceof GunItem gunItem1) {
                            gunItem1.fire(player.world, player);
                        }
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "fire_key_hold"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.getMainHandStack().getItem() instanceof GunItem gunItem) {
                        gunItem.fireTick(player.world, player);
                        if (player.getOffHandStack().getItem() instanceof GunItem gunItem1) {
                            gunItem1.fireTick(player.world, player);
                        }
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "crawl_key_hold"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.world.isSpaceEmpty(player,
                            EntityHelper.calculateBoundsForPose(player, EntityPose.SWIMMING).contract(1.0E-7))) {
                        player.setPose(EntityPose.SWIMMING);
                        ((PlayerDataExtension) player).forceSetFlag(2, true);
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "jump_key_hold"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.getRootVehicle() instanceof VehicleEntity vehicle) {
                        vehicle.upwardSpeed = 0.1f;
                    }
                    if (!player.getAbilities().flying) {
                        if (player instanceof EntityDataExtension dataExtension) {
                            if (dataExtension.getData() instanceof NbtCompound nbtCompound) {
                                if (BonusHelper.getBoolean(nbtCompound, "jet")) {
                                    if (player.getVelocity().y < 0.7f)
                                        player.addVelocity(0, Math.min(0.7f - player.getVelocity().y, 0.3), 0);
                                    player.fallDistance = 0;
                                }
                            }
                        }
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "sneak_key_hold"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.getRootVehicle() instanceof VehicleEntity vehicle) {
                        vehicle.upwardSpeed = -0.1f;
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "dismount_key_press"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    if (player.getRootVehicle() instanceof VehicleEntity vehicleEntity) {
                        player.setPosition(vehicleEntity.updatePassengerForDismount(player));
                        player.dismountVehicle();
                    }
                }));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "reload_key_press"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    ItemStack itemStack = player.getMainHandStack();
                    if (itemStack.getItem() instanceof GunItem gunItem) {
                        gunItem.reload(itemStack);
                    }
                }));
    }
}
