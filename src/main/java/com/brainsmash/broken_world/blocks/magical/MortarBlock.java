package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.MortarBlockEntity;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MortarBlock extends BlockWithEntity {
    public MortarBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (entity instanceof ItemEntity itemEntity) {
            if (world.getBlockEntity(pos) instanceof MortarBlockEntity mortarBlockEntity) {
                mortarBlockEntity.setGrindItem(itemEntity.getStack());
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(3f / 16f, 0, 3f / 16f, 13f / 16f, 4f / 16f, 13f / 16f);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(3f / 16f, 0, 3f / 16f, 13f / 16f, 4f / 16f, 13f / 16f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isOf(ItemRegister.get(ItemRegistry.PESTLE))) {
            if (!world.isClient) {
                if (world.getBlockEntity(pos) instanceof MortarBlockEntity mortarBlockEntity) {
                    mortarBlockEntity.grind(player, hand);
                }
            }
            return ActionResult.SUCCESS;
        }
        if (player.getStackInHand(hand).isEmpty()) {
            if (!world.isClient) {
                if (world.getBlockEntity(pos) instanceof MortarBlockEntity mortarBlockEntity) {
                    player.setStackInHand(hand, mortarBlockEntity.takeGrindItem());
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
