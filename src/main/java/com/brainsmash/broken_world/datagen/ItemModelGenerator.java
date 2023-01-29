package com.brainsmash.broken_world.datagen;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ItemModelGenerator extends FabricModelProvider {
    public ItemModelGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (int i = 0; i < BlockRegister.blocks.length; i++) {
            if (BlockRegister.blocks[i].getStateManager().getProperties().isEmpty()) {
                blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.blocks[i]);
            }
        }
    }

    @Override
    public void generateItemModels(net.minecraft.data.client.ItemModelGenerator itemModelGenerator) {
        for (int i = 0; i < ItemRegister.items.length; i++) {
            itemModelGenerator.register(ItemRegister.items[i], Models.GENERATED);
        }
        for (int i = 0; i < ItemRegister.bucket_item.length; i++) {
            itemModelGenerator.register(ItemRegister.bucket_item[i], Models.GENERATED);
        }
        for (int i = 0; i < BlockRegister.blocks.length; i++) {
            itemModelGenerator.register(BlockRegister.blockitems[i],
                    new Model(Optional.of(new Identifier(Main.MODID, BlockRegister.blocknames[i])), Optional.empty()));
        }
    }
}
