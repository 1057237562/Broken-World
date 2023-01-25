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
    private static final double THRESHOLD = 0.99F;
    private static final double NOISE_SCALE = 0.018F;
    private static final int RADIUS = 20;
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
        r *= 2.5;
        return (1- Math.pow(r/2, 4))*Math.exp(-25*r*r/49)
                + 5*Math.exp(-MathHelper.square((r-15)/3))
                - Math.exp(-MathHelper.square((r-5)/5));
    }

    public class Pos{
        private int x, y;
        public Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
        public String toString(){
            return "{x: "+x+", y: "+y+"}";
        }
    }

    @Override
    public double sample(NoisePos pos) {
        DefaultedList<Pos> list = DefaultedList.of();
        double r = 0;
        int cnt = 0;
        int avrX = 0, avrZ = 0;
        for(int dz = -RADIUS; dz <= RADIUS; dz++){
            int i = (int) Math.round(Math.sqrt(RADIUS*RADIUS - dz*dz));
            for(int dx = -i; dx <= i; dx++){
                double noise = sampler.sample((pos.blockX()+dx)*NOISE_SCALE, (pos.blockZ()+dz)*NOISE_SCALE);
                if(noise != sampler.sample((pos.blockX()+dx)*NOISE_SCALE, (pos.blockZ()+dz)*NOISE_SCALE))
                    System.out.println("ALERT! DIFF NOISE");
                if(noise > THRESHOLD){
                    avrX += dx;
                    avrZ += dz;
                    cnt++;
                    list.add(new Pos(pos.blockX()+dx, pos.blockZ()+dz));
                }
            }
        }
        if(cnt > 0) {
            r = Math.sqrt(avrX*avrX + avrZ*avrZ);
            double val = height(r/cnt);
            System.out.println("Valid crater noise pos: " + noisePosString(pos) + ", cnt: " + cnt + ", r: " + r/cnt + ", v: "+val+", avrX: "+avrX/cnt+", avrZ: "+avrZ/cnt+", list: "+ list+", seed: "+seed);
            return val;
        }else{
            LOGGER.debug("No valid crater center found near " + noisePosString(pos) + ", seed: "+seed);
            return 0;
        }
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
