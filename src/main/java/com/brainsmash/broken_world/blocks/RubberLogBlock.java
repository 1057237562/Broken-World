package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static com.brainsmash.broken_world.blocks.CutRubberLogBlock.RUBBER_LEVEL;

public class RubberLogBlock extends LogBlock {
    public static final DirectionProperty CUT_FACING = Properties.FACING;

    public RubberLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y).with(CUT_FACING, Direction.NORTH));
    }

    @Override
    public Block getParent() {
        return BlockRegister.blocks[BlockRegistry.RUBBER_LOG.ordinal()];
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) return;
        consumeEntireTree(world, pos);
    }

    void consumeEntireTree(World world, BlockPos pos) {
        Direction[] directions = {
                Direction.DOWN,
                Direction.UP
        };
        for (Direction d : directions) {
            BlockPos.Mutable p = pos.mutableCopy();
            p.move(d);
            while (world.getBlockState(p).getBlock() instanceof RubberLogBlock) {
                BlockState blockState = world.getBlockState(p);
                world.setBlockState(p,
                        BlockRegister.blocks[BlockRegistry.RUBBER_LOG.ordinal()].getDefaultState().with(AXIS,
                                blockState.get(AXIS)));
                p.move(d);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof SwordItem) {
            if (!state.isOf(BlockRegister.blocks[BlockRegistry.NATURAL_RUBBER_LOG.ordinal()])) return ActionResult.PASS;
            if (hit.getSide().getAxis() == state.get(AXIS)) return ActionResult.PASS;
            world.setBlockState(pos,
                    BlockRegister.blocks[BlockRegistry.CUT_RUBBER_LOG.ordinal()].getDefaultState().with(AXIS,
                            state.get(AXIS)).with(CUT_FACING, hit.getSide()).with(RUBBER_LEVEL, 2));
            consumeEntireTree(world, pos);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CUT_FACING);
    }
}
