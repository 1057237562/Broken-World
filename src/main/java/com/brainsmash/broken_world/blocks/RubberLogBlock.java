package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RubberLogBlock extends PillarBlock {
    public static final DirectionProperty CUT_FACING = Properties.FACING;

    public RubberLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y).with(CUT_FACING, Direction.NORTH));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock()))
            return;
        consumeEntireTree(world, pos);
    }

    void consumeEntireTree(World world, BlockPos pos) {
        Direction[] directions = {Direction.DOWN, Direction.UP};
        for (Direction d : directions) {
            BlockPos.Mutable p = pos.mutableCopy();
            p.move(d);
            while (world.getBlockState(p).getBlock() instanceof RubberLogBlock) {
                BlockState blockState = world.getBlockState(p);
                world.setBlockState(p, Registry.BLOCK.get(new Identifier(Main.MODID, "rubber_log")).getDefaultState().with(AXIS, blockState.get(AXIS)));
                p.move(d);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (isBowl(stack)) {
            if (!state.isOf(Registry.BLOCK.get(new Identifier(Main.MODID, "cut_rubber_log"))))
                return ActionResult.PASS;
            world.setBlockState(pos, Registry.BLOCK.get(new Identifier(Main.MODID, "collected_rubber_log")).getDefaultState()
                    .with(AXIS, state.get(AXIS))
                    .with(CUT_FACING, state.get(CUT_FACING))
            );
            return ActionResult.SUCCESS;
        } else if(isKnife(stack)) {
            if (!state.isOf(Registry.BLOCK.get(new Identifier(Main.MODID, "natural_rubber_log"))))
                return ActionResult.PASS;
            world.setBlockState(pos, state.with(CUT_FACING, hit.getSide()).with(COLLECT_STATE, State.CUT));
            consumeEntireTree(world, pos);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    boolean isBowl(ItemStack stack) {
        return true;
    }

    boolean isKnife(ItemStack stack) {
        return true;
    }
}
