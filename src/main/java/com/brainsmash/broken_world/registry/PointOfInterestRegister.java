package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.Main;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;

public class PointOfInterestRegister {
    public static final Identifier WIND_TURBINE = new Identifier(Main.MODID, "wind_turbine");

    public static void registerPlacesOfInterest(){
        PointOfInterestHelper.register(WIND_TURBINE, 0, 1, BlockRegister.blocks[32]);
    }
}
