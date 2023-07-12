package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class TreeRegister {
    public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> RUBBER;

    public static void registTrees() {
        RUBBER = ConfiguredFeatures.register("rubber", Feature.TREE, rubber().build());
    }

    private static TreeFeatureConfig.Builder builder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius) {
        return new TreeFeatureConfig.Builder(BlockStateProvider.of(log),
                new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
                BlockStateProvider.of(leaves),
                new MegaPineFoliagePlacer(ConstantIntProvider.create(radius), ConstantIntProvider.create(1),
                        ConstantIntProvider.create(4)), new TwoLayersFeatureSize(1, 0, 1));
    }

    private static TreeFeatureConfig.Builder rubber() {
        return builder(BlockRegister.blocks[BlockRegistry.NATURAL_RUBBER_LOG.ordinal()],
                BlockRegister.blocks[BlockRegistry.RUBBER_LEAVES.ordinal()], 7, 1, 0, 1).ignoreVines();
    }
}
