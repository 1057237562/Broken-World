package com.brainsmash.broken_world.blocks.electric.base;

import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.EnergyManager;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ConsumerBlock extends BlockWithEntity {

    public ConsumerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConsumerBlockEntity(pos,state);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if(!world.isClient())
            EnergyManager.UpdateGraph(world,pos);
        super.onBroken(world, pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((ConsumerBlockEntity) blockEntity).tick(world1, pos, state1, (ConsumerBlockEntity) blockEntity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            System.out.println(((ConsumerBlockEntity)world.getBlockEntity(pos)).getEnergy()+":"+((ConsumerBlockEntity)world.getBlockEntity(pos)).deltaFlow +":"+((ConsumerBlockEntity)world.getBlockEntity(pos)).edges.toString());
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

}
