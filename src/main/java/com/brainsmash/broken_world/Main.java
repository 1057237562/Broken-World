package com.brainsmash.broken_world;

import com.brainsmash.broken_world.blocks.TeleporterController;
import com.brainsmash.broken_world.blocks.electric.*;
import com.brainsmash.broken_world.blocks.electric.base.CableBlock;
import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.electric.CreativeGeneratorBlock;
import com.brainsmash.broken_world.blocks.entity.TeleporterControllerEntity;
import com.brainsmash.broken_world.blocks.entity.electric.*;
import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.fluid.AcidFluid;
import com.brainsmash.broken_world.blocks.fluid.IFluidBlock;
import com.brainsmash.broken_world.blocks.fluid.OilFluid;
import com.brainsmash.broken_world.blocks.fluid.PollutedWaterFluid;
import com.brainsmash.broken_world.items.BreathingEPP;
import com.brainsmash.broken_world.items.EmergencyTeleporter;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.DimensionRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.BatteryGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.GeneratorGuiDescription;
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

	public static final ScreenHandlerType<TeleporterControllerGuiDescription> TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"teleport_controller"),new ScreenHandlerType<>((syncId, inventory) -> new TeleporterControllerGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));
	public static final ScreenHandlerType<BatteryGuiDescription> BATTERY_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"creative_battery"),new ScreenHandlerType<>(((syncId, playerInventory) -> new BatteryGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));
	public static final ScreenHandlerType<GeneratorGuiDescription> GENERATOR_GUI_DESCRIPTION = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MODID,"generator"), new ScreenHandlerType<>(((syncId, playerInventory) -> new GeneratorGuiDescription(syncId,playerInventory,ScreenHandlerContext.EMPTY))));

	@Override
	public void onInitialize() {
		BlockRegister.RegistBlocks();
		ItemRegister.RegistItem();
		FluidRegister.RegistFluid();
		DimensionRegister.RegistDimension();

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,RegistryKey.of(Registry.PLACED_FEATURE_KEY,new Identifier(MODID, "tungsten_ore")));
	}
}
