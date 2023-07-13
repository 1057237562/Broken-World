package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CutRubberLogBlock extends LogBlock {
    public static final DirectionProperty CUT_FACING = Properties.FACING;
    public static final IntProperty RUBBER_LEVEL = Properties.AGE_3;

    public CutRubberLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                getDefaultState().with(AXIS, Direction.Axis.Y).with(CUT_FACING, Direction.NORTH).with(RUBBER_LEVEL, 0));
    }

    @Override
    public Block getParent() {
        return BlockRegister.blocks[BlockRegistry.RUBBER_LOG.ordinal()];
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem().equals(Items.BOWL)) {
            if (state.get(RUBBER_LEVEL) >= 2) {
                world.setBlockState(pos,
                        getDefaultState().with(AXIS, state.get(AXIS)).with(CUT_FACING, state.get(CUT_FACING)).with(
                                RUBBER_LEVEL, 0));
                stack.decrement(1);
                player.giveItemStack(new ItemStack(ItemRegister.items[ItemRegistry.BOWL_OF_LATEX.ordinal()]));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CUT_FACING).add(RUBBER_LEVEL);
    }
}
