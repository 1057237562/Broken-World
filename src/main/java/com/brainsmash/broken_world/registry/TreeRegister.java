package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SurfaceWaterDepthFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

import static com.brainsmash.broken_world.Main.MODID;

public class TreeRegister {

    public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> RUBBER_TREE;

    public static PlacedFeature RUBBER_TREE_PLACED_FEATURE;

    public static void registTrees() {
        RUBBER_TREE = ConfiguredFeatures.register("broken_world:rubber_tree", Feature.TREE, rubber().build());
        RUBBER_TREE_PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(RUBBER_TREE.value()),
                List.of(CountPlacementModifier.of(4), SquarePlacementModifier.of(),
                        SurfaceWaterDepthFilterPlacementModifier.of(0), PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                        PlacedFeatures.wouldSurvive(BlockRegister.blocks[BlockRegistry.RUBBER_SAPLING.ordinal()])));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MODID, "rubber_tree"),
                RUBBER_TREE_PLACED_FEATURE);
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
