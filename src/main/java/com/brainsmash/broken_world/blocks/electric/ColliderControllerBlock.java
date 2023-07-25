package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ColliderControllerBlock extends ConsumerBlock {
    public ColliderControllerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Optional<Multiblock> multiblock = MultiblockLib.INSTANCE.getMultiblock(world, pos);
        if (multiblock.isEmpty()) {
            MultiblockLib.INSTANCE.tryAssembleMultiblock(world, pos);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
