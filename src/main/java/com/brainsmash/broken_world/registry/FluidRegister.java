package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.blocks.fluid.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.ItemRegister.bucket_item;

public class FluidRegister {

    public static final FlowableFluid[] still_fluid = {
            new OilFluid.Still(),
            new PollutedWaterFluid.Still(),
            new AcidFluid.Still(),
            new GasolineFluid.Still(),
            new AetherFluid.Still(),
            new LatexFluid.Still()
    };

    public static final FlowableFluid[] flowing_fluid = {
            new OilFluid.Flowing(),
            new PollutedWaterFluid.Flowing(),
            new AcidFluid.Flowing(),
            new GasolineFluid.Flowing(),
            new AetherFluid.Flowing(),
            new LatexFluid.Flowing()
    };

    public static final Block[] fluid_blocks = {
            new IFluidBlock(still_fluid[0],
                    FabricBlockSettings.copyOf(Blocks.WATER).velocityMultiplier(0.1f).jumpVelocityMultiplier(0.1f)),
            new FluidBlock(still_fluid[1], FabricBlockSettings.copyOf(Blocks.WATER)),
            new IFluidBlock(still_fluid[2], FabricBlockSettings.copyOf(Blocks.WATER)),
            new IFluidBlock(still_fluid[3], FabricBlockSettings.copyOf(Blocks.WATER)),
            new IFluidBlock(still_fluid[4], FabricBlockSettings.copyOf(Blocks.WATER)),
            new IFluidBlock(still_fluid[5],
                    FabricBlockSettings.copyOf(Blocks.WATER).velocityMultiplier(0.6f).jumpVelocityMultiplier(0.6f))
    };

    public static final String[] fluidnames = {
            "oil",
            "polluted_water",
            "acid",
            "gasoline",
            "aether",
            "latex"
    };
    public static final Color[] fluidColor = {
            Color.BLACK,
            new Color(0, 10, 100),
            new Color(210, 180, 0),
            new Color(255, 238, 153),
            new Color(187, 0, 255),
            new Color(240, 230, 230)
    };

    public static void registerFluid() {
        for (int i = 0; i < still_fluid.length; i++) {
            Registry.register(Registry.FLUID, new Identifier(MODID, fluidnames[i]), still_fluid[i]);
            Registry.register(Registry.FLUID, new Identifier(MODID, "flowing_" + fluidnames[i]), flowing_fluid[i]);
            Registry.register(Registry.ITEM, new Identifier(MODID, fluidnames[i] + "_bucket"), bucket_item[i]);
            Registry.register(RegistryKeys.BLOCK, new Identifier(MODID, fluidnames[i]), fluid_blocks[i]);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void RegistFluidClientSide() {
        for (int i = 0; i < still_fluid.length; i++) {
            FluidRenderHandlerRegistry.INSTANCE.register(still_fluid[i], flowing_fluid[i],
                    new SimpleFluidRenderHandler(new Identifier("minecraft:block/water_still"),
                            new Identifier("minecraft:block/water_flow"), fluidColor[i].getRGB()));

            BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still_fluid[i], flowing_fluid[i]);
        }
    }
}
