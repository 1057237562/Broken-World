package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.fluid.storage.FluidWorldUtil;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.PumpGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PumpBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public SingleFluidStorage<FluidVariant> fluidStorage = new SingleFluidStorage<FluidVariant>() {

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return false;
        }
    };
    public BlockPos pointer = new BlockPos(3, -1, 3);

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.PUMP_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        maxProgression = 75;
        powerConsumption = 4;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            for (Direction direction : Direction.values()) {
                if (!fluidStorage.isEmpty() && direction != Direction.DOWN) {
                    Storage<FluidVariant> insertable = FluidStorage.SIDED.find(world, pos.offset(direction),
                            direction.getOpposite());
                    if (insertable == null) continue;
                    try (var transaction = Transaction.openOuter()) {
                        fluidStorage.amount -= insertable.insert(fluidStorage.variant, fluidStorage.amount,
                                transaction);
                        transaction.commit();
                    }
                    if (fluidStorage.isEmpty()) {
                        break;
                    }
                    markDirty();
                }
            }
            if (checkEnergy()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    progression = 0;
                    if (fluidStorage.isEmpty() || inventory.get(0).isOf(Items.BUCKET)) {
                        pointer = new BlockPos(3, -1, 3);
                        while (pointer.getY() >= -4) {
                            if (!world.isOutOfHeightLimit(pos.getY() + pointer.getY())) {
                                BlockPos pointPos = pos.add(pointer.getX(), pointer.getY(), pointer.getZ());
                                if (world.isChunkLoaded(pointPos)) {
                                    SingleFluidStorage<FluidVariant> drained = FluidWorldUtil.drain(getWorld(),
                                            pointPos);
                                    if (inventory.get(0).isOf(Items.BUCKET) && !fluidStorage.isEmpty()) {
                                        inventory.get(0).decrement(1);
                                        if (inventory.get(0).isEmpty()) {
                                            inventory.set(0,
                                                    new ItemStack(drained.variant.getFluid().getBucketItem(), 1));
                                        } else {
                                            EntityHelper.spawnItem(world,
                                                    new ItemStack(drained.variant.getFluid().getBucketItem(), 1), 1,
                                                    Direction.UP, pos);
                                        }
                                        drained.amount -= FluidConstants.BUCKET;
                                    }
                                    if (!drained.isEmpty()) {
                                        fluidStorage = drained;
                                        markDirty();
                                        break;
                                    }

                                    if (pointer.getX() > -3) {
                                        pointer = pointer.add(-1, 0, 0);
                                    } else if (pointer.getZ() > -3) {
                                        pointer = pointer.add(6, 0, -1);
                                    } else {
                                        pointer = pointer.add(6, -1, 6);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                running = false;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("pointer", pointer.asLong());
        fluidStorage.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        pointer = BlockPos.fromLong(nbt.getLong("pointer"));
        fluidStorage.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PumpGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
}
