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

import java.lang.Math;
import java.util.Random;
import java.util.stream.IntStream;

public class BWDensityFunctionTypes {
    
    public static void register(){
        Crater.register();
        ClampedGradient.register();
        DiscretePoints.register();
    }

    public record Crater(DensityFunction center, double threshold, int searchRadius, DensityFunction radius) implements DensityFunction.Base {
        public Crater(DensityFunction center, double threshold, int searchRadius, DensityFunction radius){
            this.center = center;
            this.threshold = threshold;
            this.searchRadius = searchRadius - searchRadius%4;
            this.radius = radius;
        }
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
                i -= i%4;
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

    public record ClampedGradient(DensityFunction input, double fromInput, double toInput, double fromValue, double toValue) implements DensityFunction.Base {
        public ClampedGradient(DensityFunction input, double fromInput, double toInput, double fromValue, double toValue) {
            this.input = input;
            this.fromInput = fromInput;
            this.toInput = toInput;
            this.fromValue = fromValue;
            this.toValue = toValue;
        }

        public static final Codec<Double> CONSTANT_DOUBLE_RANGE = Codec.doubleRange(-1000000.0, 1000000.0);

        public static final MapCodec<ClampedGradient> CLAMPED_GRADIENT_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(ClampedGradient::input),
                CONSTANT_DOUBLE_RANGE.fieldOf("from_input").forGetter(ClampedGradient::fromInput),
                CONSTANT_DOUBLE_RANGE.fieldOf("to_input").forGetter(ClampedGradient::toInput),
                CONSTANT_DOUBLE_RANGE.fieldOf("from_value").forGetter(ClampedGradient::fromValue),
                CONSTANT_DOUBLE_RANGE.fieldOf("to_value").forGetter(ClampedGradient::toValue)
        ).apply(instance, ClampedGradient::new));
        public static final CodecHolder<ClampedGradient> CODEC_HOLDER = CodecHolder.of(CLAMPED_GRADIENT_CODEC);

        public static final Identifier ID = new Identifier(Main.MODID, "clamped_gradient");

        @Override
        public DensityFunction apply(DensityFunctionVisitor visitor) {
            return visitor.apply(new ClampedGradient(input.apply(visitor), fromInput, toInput, fromValue, toValue));
        }

        @Override
        public double minValue() {
            return fromValue < toValue ? fromValue : toValue;
        }

        @Override
        public double maxValue() {
            return fromValue > toValue ? fromValue : toValue;
        }

        @Override
        public CodecHolder<? extends DensityFunction> getCodecHolder() {
            return CODEC_HOLDER;
        }

        public double sample(NoisePos pos) {
            double in = input.sample(pos);
            if (in <= fromInput)
                return fromValue;
            if (in >= toInput)
                return toValue;
            double k = (toValue - fromValue) / (toInput - fromInput);
            double res = (in - fromInput) * k + fromValue;
            return res;
        }

        public static void register(){
            Registry.register(Registry.DENSITY_FUNCTION_TYPE, ID, CODEC_HOLDER.codec());
        }
    }

    public record DiscretePoints(int gridWidth, int gridPadding, double minRadius, double maxRadius, DensityFunction whenPositive) implements DensityFunction.Base {
        public DiscretePoints(int gridWidth, int gridPadding, double minRadius, double maxRadius, DensityFunction whenPositive) {
            this.gridWidth = gridWidth;
            this.gridPadding = gridPadding;
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
            this.whenPositive = whenPositive;
        }

        public static final Codec<Double> CONSTANT_DOUBLE_RANGE = Codec.doubleRange(-1000000.0, 1000000.0);
        public static final Codec<Integer> CONSTANT_INT_RANGE = Codec.intRange(-1000000, 1000000);

        public static final MapCodec<DiscretePoints> VOLCANO_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                CONSTANT_INT_RANGE.fieldOf("grid_width").forGetter(DiscretePoints::gridWidth),
                CONSTANT_INT_RANGE.fieldOf("grid_padding").forGetter(DiscretePoints::gridPadding),
                CONSTANT_DOUBLE_RANGE.fieldOf("min_radius").forGetter(DiscretePoints::minRadius),
                CONSTANT_DOUBLE_RANGE.fieldOf("max_radius").forGetter(DiscretePoints::maxRadius),
                DensityFunction.FUNCTION_CODEC.fieldOf("when_positive").forGetter(DiscretePoints::whenPositive)
        ).apply(instance, DiscretePoints::new));
        public static final CodecHolder<DiscretePoints> CODEC_HOLDER = CodecHolder.of(VOLCANO_CODEC);

        public static final Identifier ID = new Identifier(Main.MODID, "discrete_points");

        @Override
        public DensityFunction apply(DensityFunctionVisitor visitor) {
            return visitor.apply(new DiscretePoints(gridWidth, gridPadding, minRadius, maxRadius, whenPositive.apply(visitor)));
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

        public double sample(NoisePos pos) {
            int blockX = pos.blockX();
            int blockZ = pos.blockZ();
            int gridX = blockX >= 0 ? blockX / gridWidth : (blockX-gridWidth) / gridWidth;
            int gridZ = blockZ >= 0 ? blockZ / gridWidth : (blockZ-gridWidth) / gridWidth;
            // Because Random uses 48-bit seed.
            long seed = Math.abs(gridX) * 16777216; // 16,777,216 = 2^24
            seed += Math.abs(gridZ) % 16777216;
            Random random = new Random(seed);
            if (blockX < 0)
                random.nextInt();
            if (blockZ < 0) {
                random.nextInt();
                random.nextInt();
            }
            int x = gridX * gridWidth + gridPadding + Math.abs(random.nextInt()) % (gridWidth - 2*gridPadding);
            int z = gridZ * gridWidth + gridPadding + Math.abs(random.nextInt()) % (gridWidth - 2*gridPadding);

            if (whenPositive.sample(new Pos(x, 0, z)) <= 0)
                return -1;
            double radius = minRadius + Math.abs(random.nextInt()) % (maxRadius + 1 - minRadius);
            return Math.sqrt((x - blockX) * (x - blockX) + (z - blockZ) * (z - blockZ)) / radius;
        }

        public static void register(){
            Registry.register(Registry.DENSITY_FUNCTION_TYPE, ID, CODEC_HOLDER.codec());
        }
    }
}
