package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.StoneBaseBlockEntity;
import com.brainsmash.broken_world.blocks.model.CustomModelBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StoneBaseBlock extends BlockWithEntity implements CustomModelBlock {
    private final boolean isBlack;

    public StoneBaseBlock(Settings settings, boolean isBlack) {
        super(settings);
        this.isBlack = isBlack;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        StoneBaseBlockEntity entity = (StoneBaseBlockEntity) world.getBlockEntity(pos);
        if (entity != null) {
            if (player.getStackInHand(hand).getCount() == 1) {
                ItemStack stack = entity.itemStack;
                entity.itemStack = player.getStackInHand(hand);
                player.setStackInHand(hand, stack);
                entity.markDirty();
                return ActionResult.SUCCESS;
            } else {
                if (entity.itemStack.isEmpty()) {
                    ItemStack stack = player.getStackInHand(hand).copy();
                    stack.setCount(1);
                    entity.itemStack = stack;
                    player.getStackInHand(hand).decrement(1);
                    entity.markDirty();
                    return ActionResult.SUCCESS;
                }
                return ActionResult.FAIL;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof StoneBaseBlockEntity entity) {
                entity.tick(world1, pos, state1, entity);
            }
        };
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.getBlockEntity(pos) instanceof StoneBaseBlockEntity entity && !entity.itemStack.isEmpty()) {
            dropStack(world, pos, entity.itemStack);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoneBaseBlockEntity(pos, state, isBlack);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(2f / 16f, 0, 2f / 16f, 14f / 16f, 1, 14f / 16f);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(2f / 16f, 0, 2f / 16f, 14f / 16f, 1, 14f / 16f);
    }
}
