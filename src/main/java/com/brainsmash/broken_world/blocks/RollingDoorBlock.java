package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.blocks.entity.RollingDoorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RollingDoorBlock extends BlockWithEntity {
    public RollingDoorBlock(Settings settings) {
        super(settings);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RollingDoorBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        RollingDoorBlockEntity rollingDoorBlockEntity = (RollingDoorBlockEntity) world.getBlockEntity(pos);
        if (!rollingDoorBlockEntity.extract()) {
            rollingDoorBlockEntity.contract();
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }

        if (world.getBlockEntity(pos) instanceof RollingDoorBlockEntity entity) {
            if (world.isReceivingRedstonePower(pos)) {
                entity.extract();
            } else {
                if (entity.extended) {
                    entity.contract();
                }
            }
        }
    }
}
