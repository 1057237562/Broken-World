package com.brainsmash.broken_world;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class IModelGenerator extends FabricModelProvider {
    public IModelGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        /*for(Block block: Main.baseblock) {
            blockStateModelGenerator.registerSimpleCubeAll(block);
        }*/
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        /*for(Item item: Main.items){
            itemModelGenerator.register(item, Models.GENERATED);
        }*/
    }
}
