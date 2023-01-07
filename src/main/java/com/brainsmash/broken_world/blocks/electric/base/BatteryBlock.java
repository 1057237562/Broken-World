package com.brainsmash.broken_world.blocks.electric.base;

import com.brainsmash.broken_world.blocks.entity.electric.GeneratorEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.EnergyManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BatteryBlock extends BlockWithEntity {

    public BatteryBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryBlockEntity(pos,state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((BatteryBlockEntity) blockEntity).tick(world1, pos, state1, (BatteryBlockEntity) blockEntity);
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BatteryBlockEntity) {
                if(world instanceof ServerWorld){
                    if(blockEntity instanceof Inventory)
                        ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                    EnergyManager.UpdateGraph(world,pos);
                }
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            System.out.println(((BatteryBlockEntity)world.getBlockEntity(pos)).deltaFlow +":"+((BatteryBlockEntity)world.getBlockEntity(pos)).edges.toString());
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

}
