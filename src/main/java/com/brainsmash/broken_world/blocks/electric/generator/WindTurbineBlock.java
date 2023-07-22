package com.brainsmash.broken_world.blocks.electric.generator;

import com.brainsmash.broken_world.blocks.electric.base.PowerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.generator.WindTurbineEntity;
import com.brainsmash.broken_world.registry.PointOfInterestRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WindTurbineBlock extends PowerBlock {
    public WindTurbineBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WindTurbineEntity(pos, state);
    }

    protected List<BlockPos> getNearbyWindTurbines(ServerWorld world, BlockPos pos) {
        PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
        Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(
                poiType -> poiType.matchesId(PointOfInterestRegister.WIND_TURBINE), pos, 20,
                PointOfInterestStorage.OccupationStatus.ANY).filter(poi -> !poi.getPos().equals(pos));
        return stream.map(PointOfInterest::getPos).collect(Collectors.toList());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (world.isClient || state.isOf(newState.getBlock())) return;

        List<BlockPos> list = getNearbyWindTurbines((ServerWorld) world, pos);
        for (BlockPos blockPos : list) {
            if (world.getBlockEntity(blockPos) instanceof WindTurbineEntity windTurbineEntity) {
                windTurbineEntity.lessCrowded();
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        ((WindTurbineEntity) world.getBlockEntity(pos)).updateGen(random);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient) return;
        List<BlockPos> list = getNearbyWindTurbines((ServerWorld) world, pos);
        for (BlockPos blockPos : list) {
            if (world.getBlockEntity(blockPos) instanceof WindTurbineEntity windTurbineEntity) {
                windTurbineEntity.moreCrowded();
            }
        }
        if (world.getBlockEntity(pos) instanceof WindTurbineEntity windTurbineEntity) {
            windTurbineEntity.setCrowdedness(list.size());
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(Text.literal("0-20 IU/t").formatted(Formatting.GRAY));
    }
}