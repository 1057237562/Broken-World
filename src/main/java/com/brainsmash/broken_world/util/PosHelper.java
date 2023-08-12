package com.brainsmash.broken_world.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PosHelper {
    public static Stream<BlockPos> squareAround(BlockPos center, final int r) {
        if (r < 0)
            throw new IllegalArgumentException("Radius cannot be negative. ");
        List<BlockPos> result = new ArrayList<>();
        if (r == 0) {
            result.add(new BlockPos(center));
            return result.stream();
        }
        final int steps = 2 * r;
        BlockPos.Mutable p = center.mutableCopy().move(-r, 0, -r);
        Direction d = Direction.EAST;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < steps; j++) {
                p.move(d);
                result.add(new BlockPos(p));
            }
            d = d.rotateClockwise(Direction.Axis.Y);
        }
        return result.stream();
    }

//    Converts from Cartesian Coordinate System to Polar Coordinate System.
//    Returning a Vec2f might be strange, but I don't want to make a new class for polar coordinates.
    public static Vec2f cartesianToPolar(Vec2f p) {
        float r = p.length();
        if (r == 0)
            return new Vec2f(0, 0);
        float theta = (float) Math.acos(p.x / r);
        return p.y < 0 ? new Vec2f(r, 2 * MathHelper.PI - theta) : new Vec2f(r, theta);
    }
}
