package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.RefineryRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.RefineryGuiDescription;
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
import org.jetbrains.annotations.Nullable;

public class RefineryBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private Item[] lastItem = new Item[2];

    public RefineryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.REFINERY_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        maxProgression = 150;
        powerConsumption = 10;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RefineryGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            String key = RefineryRecipe.makePair(inventory.get(0).getItem(), inventory.get(1).getItem());
            if (RefineryRecipe.recipes.containsKey(key) && checkCount(key) && checkEnergy()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    ItemStack output = RefineryRecipe.recipes.get(key);
                    if (inventory.get(3).isEmpty()) {
                        inventory.set(3, output.copy());
                    } else if (inventory.get(3).getItem().equals(output.getItem()) && inventory.get(
                            3).getCount() + output.getCount() < inventory.get(3).getMaxCount()) {
                        inventory.get(3).increment(output.getCount());
                    } else {
                        EntityHelper.spawnItem(world, output, 1, Direction.UP, pos);
                    }
                    Pair<Integer, Integer> count = RefineryRecipe.counts.get(key);
                    if (inventory.get(0).getItem().hashCode() > inventory.get(1).getItem().hashCode()) {
                        inventory.get(0).decrement(count.getLeft());
                        inventory.get(1).decrement(count.getRight());
                    } else {
                        inventory.get(1).decrement(count.getLeft());
                        inventory.get(0).decrement(count.getRight());
                    }
                    progression = 0;
                }
                if (!inventory.get(0).getItem().equals(lastItem[0]) || !inventory.get(1).getItem().equals(
                        lastItem[1])) {
                    lastItem = new Item[]{
                            inventory.get(0).getItem(),
                            inventory.get(1).getItem()
                    };
                    progression = 0;
                }
            } else {
                running = false;
                progression = 0;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            chargeUseItem(inventory.get(2));
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
        lastItem = new Item[]{
                inventory.get(0).getItem(),
                inventory.get(1).getItem()
        };
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    /**
     * Check whether the input items are enough to run the recipe by comparing the count of the items with the same key from counts map.
     */
    private boolean checkCount(String key) {
        Pair<Integer, Integer> counts = RefineryRecipe.counts.get(key);
        ItemStack a = inventory.get(0);
        ItemStack b = inventory.get(1);
        if (a.getItem().hashCode() > b.getItem().hashCode()) {
            return a.getCount() >= counts.getLeft() && b.getCount() >= counts.getRight();
        } else {
            return a.getCount() >= counts.getRight() && b.getCount() >= counts.getLeft();
        }
    }

}
