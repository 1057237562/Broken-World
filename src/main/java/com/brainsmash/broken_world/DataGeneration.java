package com.brainsmash.broken_world;

import com.brainsmash.broken_world.datagen.ItemModelGenerator;
import com.brainsmash.broken_world.datagen.LootTableGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(LootTableGenerator::new);
        fabricDataGenerator.addProvider(ItemModelGenerator::new);
    }
}
