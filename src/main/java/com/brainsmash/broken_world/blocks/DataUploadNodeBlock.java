package com.brainsmash.broken_world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DataUploadNodeBlock extends Block {

    public DataUploadNodeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.invalid_packet"));
            }
        }
        super.onBlockBreakStart(state, world, pos, player);
    }
}
