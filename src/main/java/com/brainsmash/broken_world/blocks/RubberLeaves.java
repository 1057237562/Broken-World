package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Block;

public class RubberLeaves extends LootLeavesBlock {
    public RubberLeaves(Settings settings) {
        super(settings);
    }

    @Override
    public Block getDrops() {
        return BlockRegister.blocks[BlockRegistry.RUBBER_SAPLING.ordinal()];
    }
}
