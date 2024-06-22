package com.brainsmash.broken_world.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class LootTableGenerator extends SimpleFabricLootTableProvider {

    public LootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
//        for (int i = 0; i < BlockRegister.blocks.length; i++) {
//            //if (BlockRegister.blocks[i] instanceof OreBlock) continue;
//            if (BlockRegister.blocks[i] instanceof TeleporterFrameBlock) continue;
//            if (BlockRegister.blocks[i] instanceof LogBlock log) {
//                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
//                        BlockLootTableGenerator.drops(log.getParent(), log.getParent().asItem(),
//                                ConstantLootNumberProvider.create(1f)));
//                continue;
//            }
//            if (BlockRegister.blocks[i] instanceof LootLeavesBlock loot) {
//                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
//                        BlockLootTableGenerator.leavesDrop(loot, loot.getDrops(), 0.05f, 0.0625f, 0.083333336f, 0.1f));
//                continue;
//            }
//            identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
//                    BlockLootTableGenerator.drops(BlockRegister.blockitems[i]));
//        }
    }
}
