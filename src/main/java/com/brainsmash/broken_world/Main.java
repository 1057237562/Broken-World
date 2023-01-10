package com.brainsmash.broken_world;

import com.brainsmash.broken_world.registry.*;
import com.brainsmash.broken_world.registry.enums.OreTypeRegistry;
import com.brainsmash.broken_world.screenhandlers.descriptions.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
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

	public static final ScreenHandlerType<TeleporterControllerGuiDescription> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"teleport_controller"),new ScreenHandlerType<>((syncId, inventory) -> new TeleporterControllerGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));
	public static final ScreenHandlerType<BatteryGuiDescription> BATTERY_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"creative_battery"),new ScreenHandlerType<>(((syncId, playerInventory) -> new BatteryGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));
	public static final ScreenHandlerType<GeneratorGuiDescription> GENERATOR_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"generator"), new ScreenHandlerType<>(((syncId, playerInventory) -> new GeneratorGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));
	public static final ScreenHandlerType<CrusherGuiDescription> CRUSHER_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"crusher"),new ScreenHandlerType<>(((syncId, playerInventory) -> new CrusherGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));
	public static final ScreenHandlerType<ShifterGuiDescription> SHIFTER_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"shifter"),new ScreenHandlerType<>(((syncId, playerInventory) -> new ShifterGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));
	public static final ScreenHandlerType<MinerGuiDescription> MINER_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"miner"),new ScreenHandlerType<>(((syncId, playerInventory) -> new MinerGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));

	@Override
	public void onInitialize() {
		BlockRegister.RegistBlocks();
		ItemRegister.RegistItem();
		FluidRegister.RegistFluid();
		DimensionRegister.RegistDimension();
		EntityRegister.RegistEntities();

		CrusherRegister.RegistCrusherRecipes();
		ShifterRegister.RegistShifterRecipes();

		OreTypeRegistry.RegistOreType();

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,RegistryKey.of(Registry.PLACED_FEATURE_KEY,new Identifier(MODID, "tungsten_ore")));
	}
}
