package com.brainsmash.broken_world.util;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderCoilBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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
}
