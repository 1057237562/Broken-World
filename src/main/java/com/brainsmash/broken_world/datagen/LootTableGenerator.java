package com.brainsmash.broken_world.datagen;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class LootTableGenerator extends SimpleFabricLootTableProvider {
    public LootTableGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
        for (int i = 42; i < BlockRegister.blocks.length; i++) {
            identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, BlockRegister.blocknames[i]),
                    BlockLootTableGenerator.drops(BlockRegister.blocks[i],
                            BlockRegister.blockitems[i],
                            ConstantLootNumberProvider.create(1f)));
        }
    }
}