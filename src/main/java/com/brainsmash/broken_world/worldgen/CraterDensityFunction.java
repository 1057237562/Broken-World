package com.brainsmash.broken_world.worldgen;

import com.brainsmash.broken_world.Main;
import com.mojang.datafixers.kinds.K1;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.slf4j.Logger;


public record CraterDensityFunction(DensityFunction input, double threshold, int searchRadius, DensityFunction radius) implements DensityFunction.Base {
    public static final Codec<Double> CONSTANT_DOUBLE_RANGE = Codec.doubleRange(-1000000.0, 1000000.0);
    public static final Codec<Integer> CONSTANT_INT_RANGE = Codec.intRange(-1000000, 1000000);

    public static final MapCodec<CraterDensityFunction> CRATER_CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(CraterDensityFunction::input),
                CONSTANT_DOUBLE_RANGE.fieldOf("threshold").forGetter(CraterDensityFunction::threshold),
                CONSTANT_INT_RANGE.fieldOf("search_radius").forGetter(CraterDensityFunction::searchRadius),
                DensityFunction.FUNCTION_CODEC.fieldOf("radius").forGetter(CraterDensityFunction::radius)
        ).apply(instance, CraterDensityFunction::new);
    });
    public static final CodecHolder<CraterDensityFunction> CODEC_HOLDER;

    static{
        CODEC_HOLDER = CodecHolder.of(CRATER_CODEC);
    }
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Identifier ID = new Identifier(Main.MODID, "crater");

    private static double height(double r, double R){
        // This is a math function that returns the height corresponding given radius (r) to the centre of the crater.
        // Parameter R means the total radius of the crater.
        // That is: height(r, R) = f(r * EFFECTIVE_RANGE / R)
        // f(x) = [Mathematical Expression]

        // The effective range means when x >= EFFECTIVE_RANGE, f(x) approximately equals to 0
        final double EFFECTIVE_RANGE = 20;
        r *= EFFECTIVE_RANGE / R;
        return (1- Math.pow(r/2, 4))*Math.exp(-25*r*r/49)
                + 5*Math.exp(-MathHelper.square((r-15)/3))
                - 2*Math.exp(-MathHelper.square((r-5)/5));
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

    public double sample(NoisePos pos) {
        double r;
        int cnt = 0;
        int maxX = 0, maxZ = 0;
        double max = Double.NEGATIVE_INFINITY;
        boolean flag = true;
        for(int dz = -searchRadius; dz <= searchRadius; dz += 4){
            int i = (int) Math.round(Math.sqrt(searchRadius * searchRadius - dz*dz));
            for(int dx = -i; dx <= i; dx += 4){
                double noise = input.sample(new UnblendedNoisePos(pos.blockX()+dx, 0, pos.blockZ()+dz));
                if(noise != 0)
                    flag = false;
//                LOGGER.info("noise: " + noise + " at: x: " + (pos.blockX()+dx) + ", z: " + (pos.blockZ()+dz));
                if(noise > threshold && noise > max){
                    max = noise;
                    maxX = dx;
                    maxZ = dz;
                }
            }
        }
        if(max > Double.NEGATIVE_INFINITY) {
            double R = radius.sample(new UnblendedNoisePos(pos.blockX()+maxX, 0, pos.blockZ()+maxZ));
            r = Math.sqrt(maxX*maxX + maxZ*maxZ);
            double val = height(r, R);
            System.out.println(
                    "Valid crater noise pos: " + noisePosString(pos.blockX(), pos.blockZ()) +
                    ", cnt: " + cnt +
                    ", r: " + r +
                    ", R: " + R +
                    ", v: "+val+
                    ", center x: "+pos.blockX()+maxX+
                    ", center z: "+pos.blockZ()+maxZ
            );
            return val;
        }
//        LOGGER.debug("No valid crater center found near " + noisePosString(pos.blockX(), pos.blockZ()) + ", max: " + max);
        return 0;
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new CraterDensityFunction(input.apply(visitor), threshold, searchRadius, radius.apply(visitor)));
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
