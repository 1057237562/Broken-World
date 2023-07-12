package com.brainsmash.broken_world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public class LogBlock extends PillarBlock {
    public LogBlock(Settings settings) {
        super(settings);
    }

    public Block getParent() {
        return this;
    }
}
