package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.CrucibleBlockEntity;
import com.brainsmash.broken_world.blocks.model.CustomModelBlock;
import com.brainsmash.broken_world.util.EntityHelper;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CrucibleBlock extends BlockWithEntity implements CustomModelBlock {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    private static final int BASE_FLUID_HEIGHT = 6;
    private static final double FLUID_HEIGHT_PER_LEVEL = 3.0;

    private static final VoxelShape RAYCAST_SHAPE;
    protected static final VoxelShape OUTLINE_SHAPE;

    private final Map<Item, CauldronBehavior> behaviorMap;

    /**
     * Constructs a cauldron block.
     *
     * <p>The behavior map must match {@link CauldronBehavior#createMap} by providing
     * a nonnull value for <em>all</em> items.
     *
     * @param settings
     * @param behaviorMap the map containing cauldron behaviors for each item
     */
    public CrucibleBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 1));
        this.behaviorMap = behaviorMap;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
        super.appendProperties(builder);
    }

    protected double getFluidHeight(BlockState state) {
        return (BASE_FLUID_HEIGHT + (double) state.get(LEVEL) * FLUID_HEIGHT_PER_LEVEL) / 16.0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL) - 1;
        BlockState blockState = i == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, i);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
    }

    protected boolean isEntityTouchingFluid(BlockState state, BlockPos pos, Entity entity) {
        return entity.getY() < (double) pos.getY() + this.getFluidHeight(
                state) && entity.getBoundingBox().maxY > (double) pos.getY() + 0.25;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (isEntityTouchingFluid(state, pos, entity)) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack itemStack = itemEntity.getStack();
                if (CrucibleBehavior.BREW.containsKey(itemStack.getItem())) {
                    EntityHelper.spawnItem(world,
                            CrucibleBehavior.BREW.get(itemStack.getItem()).interact(state, world, pos, itemStack), 1,
                            Direction.UP, pos);
                } else {
                    if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                        itemEntity.setStack(crucibleBlockEntity.insertItem(itemStack));
                    }
                }
            }
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            CauldronBehavior cauldronBehavior = this.behaviorMap.get(itemStack.getItem());
            return cauldronBehavior.interact(state, world, pos, player, hand, itemStack);
        } else {
            if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                if (!crucibleBlockEntity.getFirstItem().isEmpty()) {
                    if (world instanceof ServerWorld serverWorld) {
                        player.setStackInHand(hand, crucibleBlockEntity.getFirstItem());
                        crucibleBlockEntity.removeFirstItem();
                        crucibleBlockEntity.markDirty();
                        serverWorld.getChunkManager().markForUpdate(pos);
                    }
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.PASS;
                }
            } else {
                return ActionResult.PASS;
            }
        }
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return RAYCAST_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == 3;
    }

    static {
        RAYCAST_SHAPE = createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
        OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(),
                VoxelShapes.union(createCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
                        createCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
                        createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), RAYCAST_SHAPE),
                BooleanBiFunction.ONLY_FIRST);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient)
            return (world1, pos, state1, blockEntity) -> ((CrucibleBlockEntity) blockEntity).tick(world1, pos, state1,
                    (CrucibleBlockEntity) blockEntity);
        return null;
    }
}
