package com.brainsmash.broken_world;

import com.brainsmash.broken_world.client.render.block.entity.*;
import com.brainsmash.broken_world.screens.cotton.BatteryScreen;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.block.Block;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, TeleporterControllerScreen::new);
        HandledScreens.register(Main.BATTERY_GUI_DESCRIPTION, BatteryScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(
                Registry.BLOCK.get(new Identifier(Main.MODID, "creative_battery")),
                RenderLayer.getTranslucent()
        );
        BlockRenderLayerMap.INSTANCE.putBlock(
                Registry.BLOCK.get(new Identifier(Main.MODID, "creative_generator")),
                RenderLayer.getCutout()
        );
        EntityModelLayerRegistry.registerModelLayer(
                CreativeGeneratorBlockEntityRenderer.CREATIVE_GENERATOR,
                CreativeGeneratorBlockEntityRenderer::getTexturedModelData
        );
        BlockEntityRendererRegistry.register(Main.CREATIVE_BATTERY_ENTITY_TYPE, CreativeBatteryBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(Main.CREATIVE_GENERATOR_ENTITY_TYPE, CreativeGeneratorBlockEntityRenderer::new);


        for(int i = 0;i < Main.still_fluid.length;i++) {
            FluidRenderHandlerRegistry.INSTANCE.register(Main.still_fluid[i], Main.flowing_fluid[i], new SimpleFluidRenderHandler(
                    new Identifier("minecraft:block/water_still"),
                    new Identifier("minecraft:block/water_flow"),
                    Main.fluidColor[i].getRGB()
            ));

            BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Main.still_fluid[i], Main.flowing_fluid[i]);
        }
    }
}
