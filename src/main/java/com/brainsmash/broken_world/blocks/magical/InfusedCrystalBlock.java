package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.InfusedCrystalBlockEntity;
import com.brainsmash.broken_world.blocks.multiblock.Multiblock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Multiblock.convertToDummy(world, pos.add(-1, -1, -1), new Vec3i(3, 3, 3));
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
