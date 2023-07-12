package com.brainsmash.broken_world.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.feature.FeatureConfig;

public record VolcanoFeatureConfig(int gridWidth, int gridPadding, int minRadius, int maxRadius, double kThreshold) implements FeatureConfig {
    public static final MapCodec<VolcanoFeatureConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(1, 256).fieldOf("grid_width").forGetter(VolcanoFeatureConfig::gridWidth),
            Codec.intRange(1, 256).fieldOf("grid_padding").forGetter(VolcanoFeatureConfig::gridPadding),
            Codec.intRange(1, 256).fieldOf("min_radius").forGetter(VolcanoFeatureConfig::minRadius),
            Codec.intRange(1, 256).fieldOf("max_radius").forGetter(VolcanoFeatureConfig::maxRadius),
            Codec.doubleRange(0, 256).fieldOf("k_threshold").forGetter(VolcanoFeatureConfig::kThreshold)
    ).apply(instance, VolcanoFeatureConfig::new));

    public static final CodecHolder<VolcanoFeatureConfig> CODEC_HOLDER = CodecHolder.of(CODEC);
}
