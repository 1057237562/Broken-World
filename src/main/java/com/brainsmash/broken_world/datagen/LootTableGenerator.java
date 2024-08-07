package com.brainsmash.broken_world.datagen;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.LogBlock;
import com.brainsmash.broken_world.blocks.LootLeavesBlock;
import com.brainsmash.broken_world.blocks.XpCropBlock;
import com.brainsmash.broken_world.blocks.magical.CrucibleBlock;
import com.brainsmash.broken_world.blocks.model.TeleporterFrameBlock;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.OreBlock;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
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
            if (BlockRegister.blocks[i] instanceof CrucibleBlock) continue;
            if (BlockRegister.blocks[i] instanceof LogBlock log) {
                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
                        BlockLootTableGenerator.drops(log.getParent(), log.getParent().asItem(),
                                ConstantLootNumberProvider.create(1f)));
                continue;
            }
            if (BlockRegister.blocks[i] instanceof LootLeavesBlock loot) {
                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
                        BlockLootTableGenerator.leavesDrop(loot, loot.getDrops(), 0.05f, 0.0625f, 0.083333336f, 0.1f));
                continue;
            }
            if (i == BlockRegistry.XP_CROP.ordinal()) {
                BlockStatePropertyLootCondition.Builder isMature = BlockStatePropertyLootCondition.builder(BlockRegister.blocks[i]).properties(StatePredicate.Builder.create().exactMatch(XpCropBlock.AGE, XpCropBlock.MAX_AGE));
                Item seeds = ItemRegister.get(ItemRegistry.XP_CROP_SEEDS);
                identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
                        BlockLootTableGenerator.cropDrops(BlockRegister.blocks[i], seeds, seeds, isMature));
                continue;

            }
            identifierBuilderBiConsumer.accept(new Identifier(Main.MODID, "blocks/" + BlockRegister.blocknames[i]),
                    BlockLootTableGenerator.drops(BlockRegister.blockitems[i]));
        }
    }
}
