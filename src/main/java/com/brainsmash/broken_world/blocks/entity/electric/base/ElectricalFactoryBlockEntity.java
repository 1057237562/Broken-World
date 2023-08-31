package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.blocks.entity.FactoryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.container.ItemInsertable;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ElectricalFactoryBlockEntity extends ConsumerBlockEntity implements ItemInsertable, ImplementedInventory, FactoryBlockEntity<ImplementedInventory, ItemStack> {

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
                    craft(this).forEach(itemStack -> insertItem(itemStack));
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
        return getInputSlots().stream().sorted(
                Comparator.comparingInt(itemStack -> itemStack.getItem().hashCode())).collect(StringBuilder::new,
                StringBuilder::append, StringBuilder::append).toString();
    }

    protected List<ItemStack> getInputSlots() {
        return List.of(inventory.get(0));
    }

    protected ItemStack getBatterySlot() {
        return inventory.get(1);
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        for (int i = 2; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return ItemStack.EMPTY;
            }
            if (inventory.get(i).isOf(stack.getItem())) {
                int count = Math.min(stack.getCount(), stack.getMaxCount() - inventory.get(i).getCount());
                if (count > 0) {
                    inventory.get(i).increment(count);
                    stack.decrement(count);
                }
                if (stack.getCount() == 0) return ItemStack.EMPTY;
            }
        }
        return stack;
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
        return null;
    }
}
