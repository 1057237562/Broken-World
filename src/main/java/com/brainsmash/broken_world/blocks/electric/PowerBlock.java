package com.brainsmash.broken_world.blocks.electric;

import com.brainsmash.broken_world.blocks.entity.electric.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.EnergyManager;
import com.brainsmash.broken_world.blocks.entity.electric.PowerBlockEntity;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PowerBlock extends BlockWithEntity {

    public PowerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PowerBlockEntity(pos,state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((PowerBlockEntity) blockEntity).tick(world1, pos, state1, (PowerBlockEntity) blockEntity);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if(!world.isClient())
            EnergyManager.UpdateGraph(world,pos);
        super.onBroken(world, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            System.out.println(((PowerBlockEntity)world.getBlockEntity(pos)).getEnergy()+":"+((PowerBlockEntity)world.getBlockEntity(pos)).deltaFlow +":"+((PowerBlockEntity)world.getBlockEntity(pos)).edges.toString());
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

}