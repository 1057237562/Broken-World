package com.brainsmash.broken_world.worldgen;

import com.brainsmash.broken_world.Main;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public class CraterDensityFunction implements DensityFunction.Base {
    public static final CodecHolder<CraterDensityFunction> CODEC_HOLDER =
            CodecHolder.of(
                    MapCodec.unit(new CraterDensityFunction(0L))
            );
    private static final double THRESHOLD = 0.985F;
    private static final double NOISE_SCALE = 0.035F;
    private static final int RADIUS = 5;
    private final SimplexNoiseSampler sampler;
    public static final Identifier ID = new Identifier(Main.MODID, "crater");


    public CraterDensityFunction(long seed) {
        CheckedRandom random = new CheckedRandom(seed);
        random.skip(5648);
        this.sampler = new SimplexNoiseSampler(random);
    }

    private static double height(double r){
        return (1- Math.pow(r/2, 4))*Math.exp(-25*r*r/49)
                + 5*Math.exp(-MathHelper.square((r-15)/3))
                - Math.exp(-MathHelper.square((r-5)/5));
    }

    @Override
    public double sample(NoisePos pos) {
        double r = -1;
        int dz=0, dx=0;
        for(dz = -RADIUS; dz <= RADIUS; dz++){
            int i = (int) Math.round(Math.sqrt(RADIUS*RADIUS - dz*dz));
            for(dx = -i; dx <= i; dx++){
                double noise = sampler.sample((pos.blockX()+dx)*NOISE_SCALE, (pos.blockZ()+dz)*NOISE_SCALE);
                if(noise > THRESHOLD){
                    r = Math.sqrt(dx*dx+dz*dz);
                    break;
                }
            }
            if(r >= 0)
                break;
        }
        if(r >= 0) {
            double val = height(r*5);
            System.out.println("Valid crater noise pos: " + noisePosString(pos) + ", r: " + r + ", v: "+val+", dx: "+dx+", dz: "+dz);
            return val;
        }
        return 0;
    }

    private String noisePosString(NoisePos pos){
        return "x: "+pos.blockX()+", z: "+pos.blockZ();
    }

    @Override
    public double minValue() {
        return -1;
    }

    @Override
    public double maxValue() {
        return 5;
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }

    public static void register(){
        Registry.register(Registry.DENSITY_FUNCTION_TYPE, ID, CODEC_HOLDER.codec());
    }
}
