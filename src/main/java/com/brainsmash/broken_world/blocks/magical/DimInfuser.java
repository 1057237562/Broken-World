package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.DimInfuserEntity;
import com.brainsmash.broken_world.blocks.entity.magical.StoneBaseBlockEntity;
import com.brainsmash.broken_world.blocks.model.CustomModelBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DimInfuser extends BlockWithEntity implements CustomModelBlock {
    public DimInfuser(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getChunkManager().getLightingProvider().getLight(pos, 0) > 4) return ActionResult.FAIL;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (world.getBlockEntity(pos.add(i * 2, 2,
                        j * 2)) instanceof StoneBaseBlockEntity entity && !entity.itemStack.isEmpty() && entity.isBlack && !entity.crafting) {
                    entity.startCrafting(pos);
                }
            }
        }
        if (world.getBlockEntity(pos) instanceof DimInfuserEntity entity) {
            entity.crafting = true;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof DimInfuserEntity entity) {
                entity.tick(world1, pos, state1, entity);
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DimInfuserEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world instanceof ServerWorld serverWorld) {
            if (serverWorld.getBlockEntity(pos) instanceof DimInfuserEntity entity) {
                for (ItemStack itemStack : entity.itemStacks) {
                    dropStack(world, pos, itemStack);
                }
                entity.itemStacks.clear();
            }
        }
    }
}
