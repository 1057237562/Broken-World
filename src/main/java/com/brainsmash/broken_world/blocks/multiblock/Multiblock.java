package com.brainsmash.broken_world.blocks.multiblock;

import net.minecraft.world.World;

public abstract class Multiblock {
    public Multiblock(World world, MatchResult match) {
    }

    public boolean onDisassemble(boolean forced) {
        return false;
    }
}
