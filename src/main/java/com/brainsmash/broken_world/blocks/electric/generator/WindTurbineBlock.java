package com.brainsmash.broken_world.blocks.electric.generator;

import com.brainsmash.broken_world.blocks.electric.base.PowerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.generator.ThermalGeneratorEntity;
import com.brainsmash.broken_world.blocks.entity.electric.generator.WindTurbineEntity;
import com.brainsmash.broken_world.registry.PointOfInterestRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WindTurbineBlock extends PowerBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public WindTurbineBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WindTurbineEntity(pos,state);
    }

    private List<BlockPos> getNearbyWindTurbines(ServerWorld world, BlockPos pos){
        PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
        Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(
                poiType -> poiType.matchesId(PointOfInterestRegister.WIND_TURBINE),
                pos,
                20,
                PointOfInterestStorage.OccupationStatus.ANY
        ).filter(poi -> !poi.getPos().equals(pos));
        return stream.map(PointOfInterest::getPos).collect(Collectors.toList());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if(world.isClient || state.isOf(newState.getBlock()))
            return;

        List<BlockPos> list = getNearbyWindTurbines((ServerWorld) world, pos);
        for(BlockPos blockPos : list){
            BlockEntity entity = world.getBlockEntity(blockPos);
            if(!(entity instanceof WindTurbineEntity))
                continue;
            ((WindTurbineEntity) entity).lessCrowded();
            BlockState blockState = world.getBlockState(blockPos).with(Properties.LIT, ((WindTurbineEntity) entity).isRunning());
            world.setBlockState(blockPos, blockState);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(world.isClient)
            return;
        LOGGER.info("Wind Turbine onPlace called. ");
        List<BlockPos> list = getNearbyWindTurbines((ServerWorld) world, pos);
        for(BlockPos blockPos : list){
            BlockEntity entity = world.getBlockEntity(blockPos);
            if(!(entity instanceof WindTurbineEntity))
                continue;
            ((WindTurbineEntity) entity).moreCrowded();
            BlockState blockState = world.getBlockState(blockPos).with(Properties.LIT, ((WindTurbineEntity)entity).isRunning());
            world.setBlockState(blockPos, blockState);
        }
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof WindTurbineEntity && !list.isEmpty()){
            ((WindTurbineEntity) entity).moreCrowded();
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);
            LOGGER.info("Running: " + (!(entity instanceof WindTurbineEntity) ?
                                      "BlockEntity not instance of WindTurbineEntity. " :
                                      ((WindTurbineEntity) entity).isRunning())
            );
            List<BlockPos> list = getNearbyWindTurbines((ServerWorld) world, pos);
            LOGGER.info("Nearby wind turbines: ");
            for(BlockPos blockPos : list){
                LOGGER.info(blockPos.toString() + ", distance: " + pos.getSquaredDistance(blockPos));
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockEntity entity = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
        return super.getPlacementState(ctx)
                .with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite())
                .with(Properties.LIT, entity instanceof WindTurbineEntity && ((WindTurbineEntity) entity).isRunning());
        // I know it won't work, but PJ says he will handle it
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
    }
}