package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.blocks.multiblock.ColliderMultiBlock;
import com.brainsmash.broken_world.recipe.ColliderRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.util.EntityHelper;
import com.brainsmash.broken_world.util.PosHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColliderControllerBlockEntity extends ConsumerBlockEntity implements ImplementedInventory, SidedInventory {

    protected List<ColliderCoilBlockEntity> coilBlockEntityList = null;
    protected final RecipeManager.MatchGetter<ImplementedInventory, ? extends ColliderRecipe> matchGetter;
    protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    public static final int[] TOP_SLOTS = new int[]{0};
    public static final int[] SIDE_SLOTS = new int[]{1};
    public static final int[] BOTTOM_SLOTS = new int[]{2};

    public ColliderControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_CONTROLLER_ENTITY_TYPE, pos, state);
        setMaxCapacity(200);
        maxProgression = 40;
        powerConsumption = 4;
        matchGetter = RecipeManager.createCachedMatchGetter(ColliderRecipe.Type.INSTANCE);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        Optional<? extends ColliderRecipe> recipeMatch = matchGetter.getFirstMatch(this, world);
        running = coilBlockEntityList != null && checkEnergy() && recipeMatch.isPresent();
        if (running) {
            startCoils();
            if (progression < maxProgression) progression++;
            else {
                if (areCoilsCharged()) {
                    dischargeCoils();
                    progression = 0;
                    ColliderRecipe recipe = recipeMatch.get();
                    shrinkInputStacks(recipe);
                    outputProduct(recipe);
                }
            }
        } else {
            stopCoils();
        }
        super.tick(world, pos, state, blockEntity);
    }

    protected void shrinkInputStacks(ColliderRecipe recipe) {
        ItemStack stack1 = getStack(0);
        ItemStack stack2 = getStack(1);
        if (recipe.a().test(stack1) && stack1.getCount() >= recipe.amountA() && recipe.b().test(
                stack2) && stack2.getCount() >= recipe.amountB()) {
            stack1.decrement(recipe.amountA());
            stack2.decrement(recipe.amountB());
        } else {
            stack2.decrement(recipe.amountA());
            stack1.decrement(recipe.amountB());
        }
    }

    protected void outputProduct(ColliderRecipe recipe) {
        ItemStack outputSlot = getStack(3);
        ItemStack productStack = recipe.craft(this);
        int insertAmount;
        if (outputSlot.isEmpty()) {
            insertAmount = Math.min(productStack.getMaxCount(), productStack.getCount());
            setStack(3, new ItemStack(productStack.getItem(), insertAmount));
        } else if (outputSlot.isOf(productStack.getItem())) {
            insertAmount = Math.min(outputSlot.getMaxCount() - outputSlot.getCount(), productStack.getCount());
            outputSlot.increment(insertAmount);
        } else {
            insertAmount = 0;
        }
        EntityHelper.spawnItem(world, new ItemStack(productStack.getItem(), productStack.getCount() - insertAmount), 1,
                Direction.UP, pos);
    }

    public boolean areCoilsCharged() {
        if (coilBlockEntityList == null) return false;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            if (!coil.isCharged()) return false;
        }
        return true;
    }

    protected void dischargeCoils() {
        if (coilBlockEntityList == null) return;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.discharge();
        }
    }

    protected void startCoils() {
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.start();
        }
    }

    protected void stopCoils() {
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.stop();
        }
    }

    public void onMultiBlockAssembled() {
        if (coilBlockEntityList != null || world == null) return;
        coilBlockEntityList = new ArrayList<>();
        PosHelper.squareAround(pos, ColliderMultiBlock.DIAMETER / 2).forEach(p -> {
            BlockEntity entity = world.getBlockEntity(p);
            if (entity instanceof ColliderCoilBlockEntity) {
                coilBlockEntityList.add((ColliderCoilBlockEntity) entity);
            } else {
                Main.LOGGER.error("{} at {} is not instance of ColliderCoilBlockEntity. ", entity, p);
            }
        });
        if (coilBlockEntityList.size() != ColliderMultiBlock.DIAMETER * 4 - 4) {
            Main.LOGGER.error("Couldn't complete collider coil construction! ");
            coilBlockEntityList = null;
        }
    }

    public void onCoilBreak() {
        stopCoils();
        coilBlockEntityList = null;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) return TOP_SLOTS;
        if (side.getAxis().isHorizontal()) return SIDE_SLOTS;
        return BOTTOM_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return switch (slot) {
            case 0 -> dir == Direction.UP;
            case 1 -> dir != null && dir.getAxis().isHorizontal();
            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 2;
    }
}