package com.brainsmash.broken_world.worldgen;

import com.brainsmash.broken_world.Main;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public class CraterDensityFunction implements DensityFunction.Base {
    public static final CodecHolder<CraterDensityFunction> CODEC_HOLDER =
            CodecHolder.of(
                    MapCodec.unit(new CraterDensityFunction(0L))
            );
    private static final float THRESHOLD = 0.98F;
    private static final float NOISE_SCALE = 0.035F;
    private final SimplexNoiseSampler sampler;
    public static final Identifier ID = new Identifier(Main.MODID, "crater");


    public CraterDensityFunction(long seed) {
        CheckedRandom random = new CheckedRandom(seed);
        random.skip(5648);
        this.sampler = new SimplexNoiseSampler(random);
    }

    @Override
    public double sample(NoisePos pos) {
        double val = sampler.sample(pos.blockX()*NOISE_SCALE, pos.blockZ()*NOISE_SCALE);
        return val > THRESHOLD ? 1 : -1;
    }

    @Override
    public double minValue() {
        return -1;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }

    public static void register(){
        Registry.register(Registry.DENSITY_FUNCTION_TYPE, ID, CODEC_HOLDER.codec());
    }
}
