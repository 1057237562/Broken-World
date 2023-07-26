package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.InfusedCrystalBlockEntity;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InfusedCrystalBlock extends BlockWithEntity {
    public InfusedCrystalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfusedCrystalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((InfusedCrystalBlockEntity) blockEntity).tick(world1, pos,
                    state1, (InfusedCrystalBlockEntity) blockEntity);
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Optional<Multiblock> multiblock = MultiblockLib.INSTANCE.getMultiblock(world, pos);
        if (!multiblock.isPresent()) {
            if (MultiblockLib.INSTANCE.tryAssembleMultiblock(world, Direction.NORTH,
                    pos.offset(Direction.DOWN, 1).offset(Direction.SOUTH, 1).offset(Direction.WEST, 1))) {
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
