package com.brainsmash.broken_world.worldgen;

import com.brainsmash.broken_world.Main;
import com.mojang.datafixers.kinds.K1;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.slf4j.Logger;

public class CraterDensityFunction implements DensityFunction.Base, K1 {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CodecHolder<CraterDensityFunction> CODEC_HOLDER =
            CodecHolder.of(
                    MapCodec.unit(new CraterDensityFunction(0L))
            );
    private static final double THRESHOLD = 0.97D;
    private static final double NOISE_SCALE = 0.12D;
    private static final double SCALE = 0.1D;
    private static final int SEARCH_RADIUS = 15;
    private static final double CRATER_RADIUS = 11;
    private final SimplexNoiseSampler sampler;
    private final long seed;
    public static final Identifier ID = new Identifier(Main.MODID, "crater");


    public CraterDensityFunction(long seed) {
        this.seed = seed;
        System.out.println("Constructing CDF with seed" + seed);
        CheckedRandom random = new CheckedRandom(seed);
        random.skip(5648);
        this.sampler = new SimplexNoiseSampler(random);
    }

    private static double height(double r){
        r *= 22/CRATER_RADIUS;
        return (1- Math.pow(r/2, 4))*Math.exp(-25*r*r/49)
                + 5*Math.exp(-MathHelper.square((r-15)/3))
                - Math.exp(-MathHelper.square((r-5)/5));
    }

    public class Pos{
        private double x, y;
        public Pos(double x, double y){
            this.x = x;
            this.y = y;
        }
        public String toString(){
            return "{x: "+x+", y: "+y+"}";
        }
    }

    public double sample(double x, double z) {
        DefaultedList<Pos> list = DefaultedList.of();
        double r;
        int cnt = 0;
        int avrX = 0, avrZ = 0;
        for(int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++){
            int i = (int) Math.round(Math.sqrt(SEARCH_RADIUS * SEARCH_RADIUS - dz*dz));
            for(int dx = -i; dx <= i; dx++){
                double noise = sampler.sample((x+dx)*NOISE_SCALE, (z+dz)*NOISE_SCALE);
                if(noise > THRESHOLD){
                    avrX += dx;
                    avrZ += dz;
                    cnt++;
                    list.add(new Pos((x+dx)/SCALE, (z+dz)/SCALE));
                }
            }
        }
        if(cnt > 0) {
            r = Math.sqrt(avrX*avrX + avrZ*avrZ);
            double val = height(r/cnt);
            System.out.println(
                    "Valid crater noise pos: " + noisePosString(x/SCALE, z/SCALE) +
                    ", cnt: " + cnt +
                    ", r: " + r/cnt/SCALE +
                    ", v: "+val+
                    ", avrX: "+avrX/cnt/SCALE+
                    ", avrZ: "+avrZ/cnt/SCALE+
                    ", list: "+ list+
                    ", seed: "+seed
            );
            return val;
        }else{
            LOGGER.debug("No valid crater center found near " + noisePosString(x/SCALE, z/SCALE) + ", seed: "+seed);
            return 0;
        }
    }

    @Override
    public double sample(NoisePos pos){
        return sample(pos.blockX()*SCALE, pos.blockZ()*SCALE);
    }

    private String noisePosString(double x, double z){
        return "x: "+x+", z: "+z;
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
