package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.electric.base.ConsumerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderCoilBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderControllerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.CrusherBlockEntity;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
            if (world.getBlockEntity(pos) instanceof ColliderControllerBlockEntity entity) {
                if (MultiblockLib.INSTANCE.tryAssembleMultiblock(world, Direction.NORTH, pos))
                    entity.onMultiBlockAssembled();
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient())
            return null;
        return (world1, pos, state1, blockEntity) -> ((ColliderControllerBlockEntity) blockEntity).tick(world1, pos, state1, (ColliderControllerBlockEntity) blockEntity);
    }
}
