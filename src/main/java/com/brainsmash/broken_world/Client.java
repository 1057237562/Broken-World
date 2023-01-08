package com.brainsmash.broken_world;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.EntityRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.screens.cotton.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        HandledScreens.register(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, TeleporterControllerScreen::new);
        HandledScreens.register(Main.BATTERY_GUI_DESCRIPTION, BatteryScreen::new);
        HandledScreens.register(Main.GENERATOR_GUI_DESCRIPTION, GeneratorScreen::new);
        HandledScreens.register(Main.CRUSHER_GUI_DESCRIPTION, CrusherScreen::new);
        HandledScreens.register(Main.SHIFTER_GUI_DESCRIPTION, ShifterScreen::new);

        BlockRegister.RegistBlocksClientSide();
        EntityRegister.RegistEntitiesClientSide();

        FluidRegister.RegistFluidClientSide();
    }
}
