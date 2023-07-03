package com.brainsmash.broken_world.datagen;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.CloneVatBlock;
import com.brainsmash.broken_world.blocks.SpawnPointerBlock;
import com.brainsmash.broken_world.blocks.electric.base.BatteryBlock;
import com.brainsmash.broken_world.blocks.electric.base.CableBlock;
import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.electric.base.PowerBlock;
import com.brainsmash.broken_world.blocks.magical.InfusedCrystalBlock;
import com.brainsmash.broken_world.blocks.model.BottomTopBlock;
import com.brainsmash.broken_world.blocks.model.TeleporterFrameBlock;
import com.brainsmash.broken_world.items.magical.Wand;
import com.brainsmash.broken_world.items.weapons.guns.GunItem;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.DoorBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (int i = 0; i < BlockRegister.blocks.length; i++) {
            if (BlockRegister.blocks[i] instanceof TeleporterFrameBlock) {
                blockStateModelGenerator.registerParented(
                        BlockRegister.blocks[BlockRegistry.TELEPORTER_FRAME.ordinal()], BlockRegister.blocks[i]);
                continue;
            }
            if (BlockRegister.blocks[i] instanceof BottomTopBlock) {
                continue;
            }
            if (BlockRegister.blocks[i] instanceof SpawnPointerBlock) {
                continue;
            }
            if (BlockRegister.blocks[i] instanceof InfusedCrystalBlock) {
                continue;
            }
            if (BlockRegister.blocks[i] instanceof DoorBlock) {
                blockStateModelGenerator.registerDoor(BlockRegister.blocks[i]);
                continue;
            }
            if (BlockRegister.blocks[i].getStateManager().getProperties().isEmpty() && !(BlockRegister.blocks[i] instanceof BatteryBlock) && !(BlockRegister.blocks[i] instanceof PowerBlock) && !(BlockRegister.blocks[i] instanceof ConsumerBlock) && !(BlockRegister.blocks[i] instanceof CableBlock)) {
                blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.blocks[i]);
            }
        }
    }

    @Override
    public void generateItemModels(net.minecraft.data.client.ItemModelGenerator itemModelGenerator) {
        for (int i = 0; i < ItemRegister.items.length; i++) {
            if (ItemRegister.items[i] instanceof GunItem) {
                continue;
            }
            if (ItemRegister.items[i] instanceof Wand) {
                continue;
            }
            itemModelGenerator.register(ItemRegister.items[i], Models.GENERATED);
        }
        for (int i = 0; i < ItemRegister.bucket_item.length; i++) {
            itemModelGenerator.register(ItemRegister.bucket_item[i], Models.GENERATED);
        }
        for (Item tool : ItemRegister.toolsItem) {
            itemModelGenerator.register(tool, Models.GENERATED);
        }
        for (int i = 0; i < BlockRegister.blocks.length; i++) {
            if (BlockRegister.blocks[i] instanceof TeleporterFrameBlock) continue;
            if (BlockRegister.blocks[i] instanceof DoorBlock) continue;
            if (BlockRegister.blocks[i] instanceof CloneVatBlock || BlockRegister.blocks[i] instanceof CableBlock) {
                itemModelGenerator.register(BlockRegister.blockitems[i], Models.GENERATED);
                continue;
            }
            itemModelGenerator.register(BlockRegister.blockitems[i],
                    new Model(Optional.of(new Identifier(Main.MODID, "block/" + BlockRegister.blocknames[i])),
                            Optional.empty()));
        }
    }
}
