package com.brainsmash.broken_world;

import com.brainsmash.broken_world.client.render.block.entity.*;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.screens.cotton.BatteryScreen;
import com.brainsmash.broken_world.screens.cotton.GeneratorScreen;
import com.brainsmash.broken_world.screens.cotton.TeleporterControllerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, TeleporterControllerScreen::new);
        HandledScreens.register(Main.BATTERY_GUI_DESCRIPTION, BatteryScreen::new);
        HandledScreens.register(Main.GENERATOR_GUI_DESCRIPTION, GeneratorScreen::new);

        BlockRegister.RegistBlocksClientSide();

        FluidRegister.RegistFluidClientSide();
    }
}
