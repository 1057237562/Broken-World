package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.AdvancedFurnaceRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.AdvancedFurnaceGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class AdvancedFurnaceBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(23, ItemStack.EMPTY);
    public final Random random = new Random();
    private Item lastItem;

    public AdvancedFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.ADVANCED_FURNACE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        maxProgression = 150;
        powerConsumption = 10;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AdvancedFurnaceGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }

    public boolean insertItem(ItemStack stack) {
        for (int i = 2; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return true;
            }
            if (inventory.get(i).getItem().equals(stack.getItem())) {
                int insertCount = Math.min(inventory.get(i).getMaxCount() - inventory.get(i).getCount(),
                        stack.getCount());
                inventory.get(i).increment(insertCount);
                stack.decrement(insertCount);
            }
            if (stack.getCount() == 0) return true;
        }
        if (stack.getCount() == 0) return true;
        return false;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (AdvancedFurnaceRecipe.recipes.containsKey(inventory.get(0).getItem()) && checkEnergy()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    List<Pair<Float, Item>> output = AdvancedFurnaceRecipe.recipes.get(inventory.get(0).getItem());
                    for (Pair<Float, Item> pair : output) {
                        if (random.nextDouble() < pair.getLeft()) {
                            if (!insertItem(new ItemStack(pair.getRight(), 1))) {
                                EntityHelper.spawnItem(world, new ItemStack(pair.getRight(), 1), 1, Direction.UP, pos);
                            }
                        }
                    }
                    inventory.get(0).decrement(1);
                    progression = 0;
                }
                if (!inventory.get(0).getItem().equals(lastItem)) {
                    lastItem = inventory.get(0).getItem();
                    progression = 0;
                }
            } else {
                running = false;
                progression = 0;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            chargeUseItem(inventory.get(1));
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        lastItem = inventory.get(0).getItem();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

}
