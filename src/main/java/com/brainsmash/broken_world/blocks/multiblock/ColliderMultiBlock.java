package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderCoilBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderControllerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import io.github.jamalam360.multiblocklib.api.pattern.MatchResult;
import io.github.jamalam360.multiblocklib.api.pattern.MultiblockPatternKeyBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColliderMultiBlock extends Multiblock {
    public static Identifier ID = new Identifier(Main.MODID, "collider");
    public static int DIAMETER = 15;

    public ColliderMultiBlock(World world, MatchResult match) {
        super(world, match);
    }

    @Override
    public boolean onDisassemble(boolean forced) {
        int d = ColliderMultiBlock.DIAMETER;
        BlockPos pos = getMatchResult().bottomLeftPos().add(d / 2, 0, -d / 2);
        if (getWorld().getBlockEntity(pos) instanceof ColliderControllerBlockEntity entity)
            entity.onCoilBreak();
        return super.onDisassemble(forced);
    }

    public static void register() {
        MultiblockLib.INSTANCE.registerMultiblock(
                ID,
                ColliderMultiBlock::new,
                MultiblockPatternKeyBuilder.start()
                        .where('A', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == BlockRegister.get(BlockRegistry.COLLIDER_COIL)))
                        .where('B', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == BlockRegister.get(BlockRegistry.COLLIDER_CONTROLLER)))
                        .where(' ', CachedBlockPosition.matchesBlockState(state -> state.isAir()))
                        .build()
        );
    }


}