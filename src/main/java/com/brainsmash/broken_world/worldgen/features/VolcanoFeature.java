package com.brainsmash.broken_world.worldgen.features;

import com.brainsmash.broken_world.Main;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;
import java.util.Random;

public class VolcanoFeature extends Feature<VolcanoFeatureConfig> {
    private static boolean WARNED_BLOCK_NOT_FOUND = false;
    private static final Identifier MATERIAL_ID = new Identifier(Main.MODID, "volcanic_stone");
    public static final Identifier ID = new Identifier(Main.MODID, "volcano");
    private static final double CRATER_RADIUS = 0.3;
    private static final double LAVA_HEIGHT = 0.5;

    public VolcanoFeature(Codec<VolcanoFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<VolcanoFeatureConfig> context) {
        final Optional<Block> materialOptional = RegistryKeys.BLOCK.getOrEmpty(MATERIAL_ID);
        if (materialOptional.isEmpty()) {
            if (!WARNED_BLOCK_NOT_FOUND) {
                LogUtils.getLogger().error(
                        "Volcano feature generation not possible: " + MATERIAL_ID + " not registered. ");
                WARNED_BLOCK_NOT_FOUND = true;
            }
            return false;
        }
        Block material = materialOptional.get();

        int R = radius(context);
        if (R == -1) return false;
        if (!terrainPlainEnough(context, R)) return false;

        BlockPos o = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        for (int dx = -R; dx <= R; dx++) {
            for (int dz = -R; dz <= R; dz++) {
                BlockPos.Mutable p = o.mutableCopy();
                p.move(dx, 0, dz);
                int h = 0;
                if (!(!world.getBlockState(p).isAir() && world.getBlockState(p.up()).isAir())) {
                    while (world.getBlockState(p).isAir() && h >= -R) {
                        p.move(0, -1, 0);
                        h--;
                    }
                    while (!world.getBlockState(p.up()).isAir() && h <= R) {
                        p.move(0, 1, 0);
                        h++;
                    }
                }

                double r = Math.sqrt(Math.pow(p.getX() - o.getX(), 2) + Math.pow(p.getZ() - o.getZ(), 2));
                int H = (int) Math.round(R * height(r / R));
                H += Math.round(h * Math.min(r / R, 1));
                while (h < H) {
                    p.move(0, 1, 0);
                    h++;
                    world.setBlockState(p, material.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                if (r / R > CRATER_RADIUS) continue;
                while (h > H) {
                    world.setBlockState(p, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    p.move(0, -1, 0);
                    h--;
                }
                p.move(0, 1, 0);
                while (o.getY() + LAVA_HEIGHT * R - p.getY() > 0) {
                    world.setBlockState(p, Blocks.LAVA.getDefaultState(), Block.NOTIFY_LISTENERS);
                    p.move(0, 1, 0);
                }
            }
        }

        return true;
    }

    int radius(FeatureContext<VolcanoFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        VolcanoFeatureConfig config = context.getConfig();
        final int gridWidth = config.gridWidth();
        final int gridPadding = config.gridPadding();

        int blockX = pos.getX();
        int blockZ = pos.getZ();

        int chunkX = blockX >= 0 ? blockX / 16 : (blockX - 16) / 16;
        int chunkZ = blockZ >= 0 ? blockZ / 16 : (blockZ - 16) / 16;

        int gridX = chunkX >= 0 ? chunkX / gridWidth : (chunkX - gridWidth) / gridWidth;
        int gridZ = chunkZ >= 0 ? chunkZ / gridWidth : (chunkZ - gridWidth) / gridWidth;

        // Because Random uses 48-bit seed.
        long seed = Math.abs(gridX) * 16777216L; // 16,777,216 = 2^24
        seed += Math.abs(gridZ) % 16777216;
        Random random = new Random(seed);
        if (blockX < 0) random.nextInt();
        if (blockZ < 0) {
            random.nextInt();
            random.nextInt();
        }
        int allowedChunkX = gridX * gridWidth + gridPadding + Math.abs(
                random.nextInt()) % (gridWidth - 2 * gridPadding + 1);
        int allowedChunkZ = gridZ * gridWidth + gridPadding + Math.abs(
                random.nextInt()) % (gridWidth - 2 * gridPadding + 1);
        if (chunkX != allowedChunkX) return -1;
        if (chunkZ != allowedChunkZ) return -1;
        return config.minRadius() + Math.abs(random.nextInt()) % (config.maxRadius() - config.minRadius() + 1);
    }

    double height(double r) {
        return 0.65 * Math.exp(-7.4 * Math.pow(r - 0.3, 2)) - 0.5 * Math.exp(-Math.pow(6 * r, 2));
    }

    boolean terrainPlainEnough(FeatureContext<VolcanoFeatureConfig> context, int radius) {
        VolcanoFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        int[][] directions = {
                {
                        0,
                        -1
                },
                {
                        1,
                        -1
                },
                {
                        1,
                        0
                },
                {
                        1,
                        1
                },
                {
                        0,
                        1
                },
                {
                        -1,
                        1
                },
                {
                        -1,
                        0
                },
                {
                        -1,
                        -1
                }
        };
        BlockPos.Mutable[] checkPoints = new BlockPos.Mutable[directions.length];
        for (int i = 0; i < directions.length; i++) {
            checkPoints[i] = pos.mutableCopy();
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < directions.length; j++) {
                BlockPos.Mutable cp = checkPoints[j];
                cp.move(directions[j][0] * radius / 2, 0, directions[j][1] * radius / 2);
                int cnt = 0;
                boolean thresholdExceeded = false;
                while (!(!world.getBlockState(cp).isAir() && world.getBlockState(cp.up()).isAir())) {
                    if (world.getBlockState(cp).isAir()) {
                        cp.move(0, -1, 0);
                        cnt--;
                    } else {
                        cp.move(0, 1, 0);
                        cnt++;
                    }
                    double distance = (double) radius / 2 * Math.sqrt(
                            Math.pow(directions[j][0], 2) + Math.pow(directions[j][1], 2));
                    if (Math.abs(cnt / distance) > config.kThreshold()) {
                        thresholdExceeded = true;
                        break;
                    }
                }
                if (thresholdExceeded) return false;
            }
        }
        return true;
    }

    public static void register() {
        Registry.register(Registry.FEATURE, ID, new VolcanoFeature(VolcanoFeatureConfig.CODEC_HOLDER.codec()));
    }
}
