package com.brainsmash.broken_world.blocks.electric.generator;

import com.brainsmash.broken_world.blocks.electric.base.PowerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.generator.HydroGeneratorEntity;
import com.brainsmash.broken_world.registry.PointOfInterestRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.WorldAccess;
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
        return new HydroGeneratorEntity(pos,state);
    }

    private List<BlockPos> getNearbyWindTurbines(ServerWorld world, BlockPos pos){
        PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
        Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(
                poiType -> {
                    LOGGER.info(poiType.getKey().toString());
                    return poiType.matchesId(PointOfInterestRegister.WIND_TURBINE);
                },
                pos,
                20,
                PointOfInterestStorage.OccupationStatus.ANY
        );
        return stream.map(PointOfInterest::getPos).collect(Collectors.toList());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            LOGGER.info("Nearby wind turbines: " + getNearbyWindTurbines((ServerWorld) world, pos).toString());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
    }
}
