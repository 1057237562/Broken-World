package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

import static com.brainsmash.broken_world.blocks.CutRubberLogBlock.RUBBER_LEVEL;

public class RubberLeaves extends LootLeavesBlock {
    public RubberLeaves(Settings settings) {
        super(settings);
    }

    @Override
    public Block getDrops() {
        return BlockRegister.blocks[BlockRegistry.RUBBER_SAPLING.ordinal()];
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (random.nextFloat() < 0.02f) updateRubberAge(state, world, pos);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !(Boolean) state.get(PERSISTENT);
    }

    private static void updateRubberAge(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState i = state;
        BlockPos p = pos;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (Direction direction : Direction.values()) {
            mutable.set(pos, direction);
            BlockState nptr = world.getBlockState(mutable);
            if (getDistanceFromLog(i) > getDistanceFromLog(nptr)) {
                i = world.getBlockState(mutable);
                p = mutable.toImmutable();
            }
        }
        if (p.equals(pos)) return;
        if (i.getBlock() instanceof LeavesBlock) {
            updateRubberAge(i, world, p);
        }
        if (i.isIn(BlockTags.LOGS)) {
            updateEntireTree(world, p);
        }
    }

    private static void updateEntireTree(WorldAccess world, BlockPos pos) {
        Direction[] directions = {
                Direction.DOWN,
                Direction.UP
        };
        for (Direction d : directions) {
            BlockPos.Mutable p = pos.mutableCopy();
            p.move(d);

            while (world.getBlockState(p).isIn(BlockTags.LOGS)) {
                BlockState blockState = world.getBlockState(p);
                if (blockState.getBlock() instanceof CutRubberLogBlock) {
                    world.setBlockState(p, blockState.with(RUBBER_LEVEL, Math.min(2, blockState.get(RUBBER_LEVEL) + 1)),
                            3);
                    break;
                }
                p.move(d);
            }
        }
    }

    private static int getDistanceFromLog(BlockState state) {
        if (state.isIn(BlockTags.LOGS)) {
            return 0;
        } else {
            return state.getBlock() instanceof LeavesBlock ? state.get(DISTANCE) : 7;
        }
    }
}
