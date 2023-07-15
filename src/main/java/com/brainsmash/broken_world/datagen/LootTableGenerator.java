package com.brainsmash.broken_world.datagen;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.LogBlock;
import com.brainsmash.broken_world.blocks.LootLeavesBlock;
import com.brainsmash.broken_world.blocks.model.TeleporterFrameBlock;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.OreBlock;
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
        for (int i = 0; i < BlockRegister.blocks.length; i++) {
            if (BlockRegister.blocks[i] instanceof OreBlock) continue;
            if (BlockRegister.blocks[i] instanceof TeleporterFrameBlock) continue;
            if (BlockRegister.blocks[i] instanceof LogBlock log) {
                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "block/" + BlockRegister.blocknames[i]),
                        BlockLootTableGenerator.drops(log.getParent(), log.getParent().asItem(),
                                ConstantLootNumberProvider.create(1f)));
                continue;
            }
            if (BlockRegister.blocks[i] instanceof LootLeavesBlock loot) {
                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "block/" + BlockRegister.blocknames[i]),
                        BlockLootTableGenerator.leavesDrop(loot, loot.getDrops(), 0.05f, 0.0625f, 0.083333336f, 0.1f));
                continue;
            }
            identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "block/" + BlockRegister.blocknames[i]),
                    BlockLootTableGenerator.drops(BlockRegister.blocks[i], BlockRegister.blockitems[i],
                            ConstantLootNumberProvider.create(1f)));
        }
    }
}
