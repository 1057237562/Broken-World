package com.brainsmash.broken_world.worldgen;

import com.brainsmash.broken_world.Main;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import static com.brainsmash.broken_world.worldgen.BWDensityFunctionTypes.Crater.CONSTANT_DOUBLE_RANGE;

public class BWDensityFunctionTypes {
    
    public static void register(){
        Crater.register();
        CraterCenter.register();
    }

    public record CraterCenter(DensityFunction input, double threshold) implements DensityFunction.Base{

        public static final MapCodec<CraterCenter> CRATER_CENTER_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(CraterCenter::input),
            CONSTANT_DOUBLE_RANGE.fieldOf("threshold").forGetter(CraterCenter::threshold)
        ).apply(instance, CraterCenter::new));

        public static final CodecHolder<CraterCenter> CODEC_HOLDER = CodecHolder.of(CRATER_CENTER_CODEC);
        public static final Identifier ID = new Identifier(Main.MODID, "crater_center");

        @Override
        public DensityFunction apply(DensityFunctionVisitor visitor) {
            return visitor.apply(new CraterCenter(input.apply(visitor), threshold));
        }

        @Override
        public double sample(NoisePos pos) {
            double val = input.sample(pos);
            Pos p = new Pos(pos);
            if(     val < threshold ||
                    input.sample(p.offsetX(Crater.STEP)) >= val ||
                    input.sample((p.offsetX(-Crater.STEP))) >= val ||
                    input.sample((p.offsetZ(Crater.STEP))) >= val ||
                    input.sample((p.offsetZ(-Crater.STEP))) >= val
            )
                return -10;
            return val;
        }

        @Override
        public double minValue() {
            return -10;
        }

        @Override
        public double maxValue() {
            return input.maxValue();
        }

        @Override
        public CodecHolder<? extends DensityFunction> getCodecHolder() {
            return CODEC_HOLDER;
        }

        public static void register(){
            Registry.register(Registry.DENSITY_FUNCTION_TYPE, ID, CODEC_HOLDER.codec());
        }
    }

    public record Crater(DensityFunction center, double threshold, int searchRadius, DensityFunction radius) implements DensityFunction.Base {
        public static final Codec<Double> CONSTANT_DOUBLE_RANGE = Codec.doubleRange(-1000000.0, 1000000.0);
        public static final Codec<Integer> CONSTANT_INT_RANGE = Codec.intRange(-1000000, 1000000);

        public static final int STEP = 4;

        public static final MapCodec<Crater> CRATER_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                DensityFunction.FUNCTION_CODEC.fieldOf("center").forGetter(Crater::center),
                CONSTANT_DOUBLE_RANGE.fieldOf("threshold").forGetter(Crater::threshold),
                CONSTANT_INT_RANGE.fieldOf("search_radius").forGetter(Crater::searchRadius),
                DensityFunction.FUNCTION_CODEC.fieldOf("radius").forGetter(Crater::radius)
        ).apply(instance, Crater::new));
        public static final CodecHolder<Crater> CODEC_HOLDER = CodecHolder.of(CRATER_CODEC);

        public static final Identifier ID = new Identifier(Main.MODID, "crater");

        private static final double TRANSITION_BORDER = 15;
        // The effective range means when x >= EFFECTIVE_RANGE, f(x) approximately equals to 0
        private static final double EFFECTIVE_RANGE = 20;
        private static double height(double r, double R){
            // This is a math function that returns the height corresponding given radius (r) to the centre of the crater.
            // Parameter R means the total radius of the crater.
            // That is: height(r, R) = f(r * EFFECTIVE_RANGE / R)
            // f(x) = [Mathematical Expression]

            r *= EFFECTIVE_RANGE / R;
            return (1- Math.pow(r/2, 4))*Math.exp(-25*r*r/49)
                    + 5*Math.exp(-MathHelper.square((r-15)/3))
                    - 2*Math.exp(-MathHelper.square((r-5)/5));
        }

        private static double weight(double r, double R){
            r *= EFFECTIVE_RANGE / R;
            if(r <= TRANSITION_BORDER)
                return 1;
            r -= TRANSITION_BORDER;
            r /= 3;
            return Math.exp(-r*r);
        }

        public double sample(NoisePos pos) {
            Pos p = new Pos(pos);
            double sum = 0;
            double primary = 0;
            double primaryHeight = 0;
            double primaryWeight = 0;
            double secTotWeight = 0;
            for(int dz = -searchRadius; dz <= searchRadius; dz += STEP){
                int i = (int) Math.round(Math.sqrt(searchRadius * searchRadius - dz*dz));
                for(int dx = -i; dx <= i; dx += STEP){
                    Pos pp = p.offsetZ(dz).offsetX(dx);
                    double val = center.sample(pp);
                    if(val > threshold) {
                        double r = Math.sqrt(dx * dx + dz * dz);
                        double R = radius.sample((new Pos(pos)).offsetX(dx).offsetZ(dz));
                        if (val > primary) {
                            sum += primaryHeight;
                            secTotWeight += primaryWeight;
                            primary = val;
                            primaryWeight = weight(r, R);
                            primaryHeight = height(r, R) * primaryWeight;
                        } else {
                            double weight = weight(r, R);
                            sum += height(r, R) * weight;
                            secTotWeight += weight;
                        }
                    }
                }
            }

            return primaryHeight + sum/(secTotWeight>0 ? secTotWeight : 1)*(1-primaryWeight);
        }

        @Override
        public DensityFunction apply(DensityFunctionVisitor visitor) {
            return visitor.apply(new Crater(center.apply(visitor), threshold, searchRadius, radius.apply(visitor)));
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
}
