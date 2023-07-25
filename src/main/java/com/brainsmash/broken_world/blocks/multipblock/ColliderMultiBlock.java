package com.brainsmash.broken_world.blocks.multipblock;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.BlockRegister;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import io.github.jamalam360.multiblocklib.api.pattern.MatchResult;
import io.github.jamalam360.multiblocklib.api.pattern.MultiblockPatternKeyBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ColliderMultiBlock extends Multiblock {

    public ColliderMultiBlock(World world, MatchResult match) {
        super(world, match);
    }

    public static void register() {
        MultiblockLib.INSTANCE.registerMultiblock(
                new Identifier(Main.MODID, "collider.json"),
                ColliderMultiBlock::new,
                MultiblockPatternKeyBuilder.start()
                        .where('A', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == BlockRegister.get(BlockRegistry.)))
                        .where('B', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == Blocks.IRON_BLOCK))
                        .build();
);
    }
}
