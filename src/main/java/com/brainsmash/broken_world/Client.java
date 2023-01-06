package com.brainsmash.broken_world;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.screens.cotton.BatteryScreen;
import com.brainsmash.broken_world.screens.cotton.GeneratorScreen;
import com.brainsmash.broken_world.screens.cotton.TeleporterControllerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

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
