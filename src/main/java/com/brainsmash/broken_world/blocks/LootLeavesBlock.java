package com.brainsmash.broken_world.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;

public class LootLeavesBlock extends LeavesBlock {

    public LootLeavesBlock(Settings settings) {
        super(settings);
    }

    public Block getDrops() {
        return null;
    }
}
