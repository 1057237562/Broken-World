package com.brainsmash.broken_world.items.magical.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ImplementedRune {

    void execute(World world, PlayerEntity player, BlockPos at, ImplementedRune last);
}
