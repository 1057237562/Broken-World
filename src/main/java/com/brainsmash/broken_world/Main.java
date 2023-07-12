package com.brainsmash.broken_world;

import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.guns.GunItem;
import com.brainsmash.broken_world.recipe.*;
import com.brainsmash.broken_world.registry.*;
import com.brainsmash.broken_world.registry.enums.OreTypeRegistry;
import com.brainsmash.broken_world.screenhandlers.descriptions.*;
import com.brainsmash.broken_world.util.BonusHelper;
import com.brainsmash.broken_world.util.EntityHelper;
import com.brainsmash.broken_world.worldgen.BWDensityFunctionTypes;
import com.brainsmash.broken_world.worldgen.features.VolcanoFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;

public class Main implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "broken_world";

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
        BlockRegister.RegistBlocks();
        ItemRegister.registItem();
        FluidRegister.RegistFluid();
        DimensionRegister.RegistDimension();
        EntityRegister.registEntities();
        EntityRegister.registSpawnRegistration();
        TreeRegister.registTrees();
        PointOfInterestRegister.registerPlacesOfInterest();

        AdvancedFurnaceRecipe.registAdvancedFurnaceRecipe();
        CrusherRecipe.registCrusherRecipes();
        SifterRecipe.registSifterRecipes();
        CentrifugeRecipe.registCentrifugeRecipes();
        AssemblerRecipe.registAssemblerRecipe();
        CompressorRecipe.registCompressorRecipes();

        OreTypeRegistry.RegistOreType();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "tungsten_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "magnetite")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(MODID, "tin_ore")));

        FabricatorRecipe.register();

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
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(MODID, "reload_key_press"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    ItemStack itemStack = player.getMainHandStack();
                    if (itemStack.getItem() instanceof GunItem gunItem) {
                        gunItem.reload(itemStack);
                    }
                }));
    }
}
