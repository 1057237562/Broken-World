package com.brainsmash.broken_world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnPointerBlock extends Block {

    public SpawnPointerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.setSpawnPoint(world.getRegistryKey(), pos.add(0, 1, 0), 0.0f, false, true);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
