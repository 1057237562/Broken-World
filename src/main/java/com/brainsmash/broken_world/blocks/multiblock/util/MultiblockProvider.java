package com.brainsmash.broken_world.blocks.multiblock.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiblockProvider {

    MultiblockComponent get(World world, BlockPos pos);
}
