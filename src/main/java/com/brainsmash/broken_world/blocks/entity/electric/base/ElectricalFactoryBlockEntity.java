package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.blocks.entity.FactoryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.container.ItemInterface;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.util.EntityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ElectricalFactoryBlockEntity extends ConsumerBlockEntity implements ItemInterface, ImplementedInventory, FactoryBlockEntity<ImplementedInventory, ItemStack> {

    protected final DefaultedList<ItemStack> inventory;
    private String lastItem;

    public ElectricalFactoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, DefaultedList<ItemStack> inventory) {
        super(type, pos, state);
        this.inventory = inventory;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (matches(this) && checkEnergy()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    craft(this).forEach(itemStack -> {
                        ItemStack reminder = insertItemToOutput(itemStack);
                        EntityHelper.spawnItem(world, reminder, 1, Direction.UP, pos);
                    });
                    progression = 0;
                }
                if (!getInputHashCode().equals(lastItem)) {
                    lastItem = getInputHashCode();
                    progression = 0;
                }
            } else {
                running = false;
                progression = 0;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            chargeUseItem(getBatterySlot());
        }
        super.tick(world, pos, state, blockEntity);
    }

    private String getInputHashCode() {
        return getInputSlots().stream().map(ItemStack::getItem).sorted(
                Comparator.comparingInt(Object::hashCode)).collect(StringBuilder::new, StringBuilder::append,
                StringBuilder::append).toString();
    }

    protected List<ItemStack> getInputSlots() {
        return List.of(inventory.get(0));
    }

    protected ItemStack getBatterySlot() {
        return inventory.get(1);
    }

    protected List<ItemStack> getOutputSlots() {
        return inventory.subList(2, inventory.size());
    }

    @Override
    public ItemStack insertItemToOutput(ItemStack stack) {
        for (int i = 0; i < getOutputSlots().size(); i++) {
            if (getOutputSlots().get(i).isEmpty()) {
                getOutputSlots().set(i, stack);
                return ItemStack.EMPTY;
            }
            if (getOutputSlots().get(i).isOf(stack.getItem())) {
                int count = Math.min(stack.getCount(), stack.getMaxCount() - getOutputSlots().get(i).getCount());
                if (count > 0) {
                    getOutputSlots().get(i).increment(count);
                    stack.decrement(count);
                }
                if (stack.getCount() == 0) return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public ItemStack insertItemToInput(ItemStack stack) {
        for (int i = 0; i < getInputSlots().size(); i++) {
            if (getInputSlots().get(i).isEmpty()) {
                getInputSlots().set(i, stack);
                return ItemStack.EMPTY;
            }
            if (getInputSlots().get(i).isOf(stack.getItem())) {
                int count = Math.min(stack.getCount(), stack.getMaxCount() - getInputSlots().get(i).getCount());
                if (count > 0) {
                    getInputSlots().get(i).increment(count);
                    stack.decrement(count);
                }
                if (stack.getCount() == 0) return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        lastItem = getInputHashCode();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    @Override
    public boolean matches(ImplementedInventory itemList) {
        return false;
    }

    @Override
    public List<ItemStack> craft(ImplementedInventory itemList) {
        return List.of();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
