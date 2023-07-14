package com.brainsmash.broken_world.blocks.electric.base;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.EnergyManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CableBlock extends BlockWithEntity {

    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new IdentityHashMap<>();
    public static final Map<Direction, BooleanProperty> PROPERTY_MAP = Util.make(new HashMap<>(), map -> {
        map.put(Direction.EAST, EAST);
        map.put(Direction.WEST, WEST);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    });

    final double cableThickness; //0.25 for thicker
    final boolean covered;
    final int maxFlow;

    public CableBlock(Settings settings, double thickness, boolean cover, int capa) {
        super(settings);
        setDefaultState(
                this.getStateManager().getDefaultState().with(EAST, false).with(WEST, false).with(NORTH, false).with(
                        SOUTH, false).with(UP, false).with(DOWN, false));
        cableThickness = thickness;
        covered = cover;
        maxFlow = capa;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntity(pos, state, maxFlow);
    }

    public boolean canConnect(BlockState state) {
        return state.isIn(TagKey.of(Registry.BLOCK_KEY, new Identifier("broken_world:electrical")));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
        super.appendProperties(builder);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /*@Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((CableBlockEntity) blockEntity).tick(world1, pos, state1, (CableBlockEntity) blockEntity);
    }*/

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext shapeContext) {
        return getShape(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getShape(state);
    }

    public static VoxelShape getShape(BlockState state) {
        return SHAPE_CACHE.computeIfAbsent(state, CableBlock::getStateShape);
    }

    private static VoxelShape getStateShape(BlockState state) {
        CableBlock cableBlock = (CableBlock) state.getBlock();

        final double size = cableBlock.cableThickness;
        final VoxelShape baseShape = VoxelShapes.cuboid(size, size, size, 1 - size, 1 - size, 1 - size);
        List<VoxelShape> connections = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (state.get(CableBlock.PROPERTY_MAP.get(dir))) {
                double[] mins = new double[]{
                        size,
                        size,
                        size
                };
                double[] maxs = new double[]{
                        1 - size,
                        1 - size,
                        1 - size
                };
                int axis = dir.getAxis().ordinal();
                if (dir.getDirection() == Direction.AxisDirection.POSITIVE) {
                    maxs[axis] = 1;
                } else {
                    mins[axis] = 0;
                }
                connections.add(VoxelShapes.cuboid(mins[0], mins[1], mins[2], maxs[0], maxs[1], maxs[2]));
            }
        }
        return VoxelShapes.union(baseShape, connections.toArray(new VoxelShape[]{}));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos1 = blockPos.north();
        BlockPos blockPos2 = blockPos.east();
        BlockPos blockPos3 = blockPos.south();
        BlockPos blockPos4 = blockPos.west();
        BlockPos blockPos5 = blockPos.up();
        BlockPos blockPos6 = blockPos.down();
        BlockState blockState1 = blockView.getBlockState(blockPos1);
        BlockState blockState2 = blockView.getBlockState(blockPos2);
        BlockState blockState3 = blockView.getBlockState(blockPos3);
        BlockState blockState4 = blockView.getBlockState(blockPos4);
        BlockState blockState5 = blockView.getBlockState(blockPos5);
        BlockState blockState6 = blockView.getBlockState(blockPos6);
        return super.getPlacementState(ctx).with(NORTH, canConnect(blockState1)).with(EAST,
                canConnect(blockState2)).with(SOUTH, canConnect(blockState3)).with(WEST, canConnect(blockState4)).with(
                UP, canConnect(blockState5)).with(DOWN, canConnect(blockState6));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state.with(PROPERTY_MAP.get(direction), this.canConnect(neighborState));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()) EnergyManager.UpdateGraph(world, pos);
        super.onBroken(world, pos, state);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) EnergyManager.UpdateGraph(world, pos);
        super.onDestroyedByExplosion(world, pos, explosion);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            System.out.println(((CableBlockEntity) world.getBlockEntity(
                    pos)).deltaFlow + ":" + ((CableBlockEntity) world.getBlockEntity(pos)).edges.toString());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
