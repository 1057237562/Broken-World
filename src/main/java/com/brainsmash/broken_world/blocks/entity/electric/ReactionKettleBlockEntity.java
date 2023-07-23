package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.ReactionRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.ReactionKettleGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import com.brainsmash.broken_world.util.ItemHelper;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class ReactionKettleBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);

    private Item[] lastItem = new Item[2];

    public ReactionKettleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.REACTION_KETTLE_ENTITY_TYPE, pos, state);
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
        return new ReactionKettleGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            String key = ItemHelper.makePair(inventory.get(0).getItem(), inventory.get(1).getItem(),
                    inventory.get(2).getItem());
            if (ReactionRecipe.recipes.containsKey(key) && checkCount(key) && canRun()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    ItemStack output = ReactionRecipe.recipes.get(key);
                    if (inventory.get(4).isEmpty()) {
                        inventory.set(4, output.copy());
                    } else if (inventory.get(4).getItem().equals(output.getItem()) && inventory.get(
                            4).getCount() + output.getCount() < inventory.get(4).getMaxCount()) {
                        inventory.get(4).increment(output.getCount());
                    } else {
                        EntityHelper.spawnItem(world, output, 1, Direction.UP, pos);
                    }
                    List<Integer> count = ReactionRecipe.counts.get(key);
                    List<ItemStack> stacks = List.of(inventory.get(0), inventory.get(1), inventory.get(2));
                    stacks.sort(Comparator.comparingInt(itemStack -> itemStack.getItem().hashCode()));
                    stacks.get(0).decrement(count.get(0));
                    stacks.get(1).decrement(count.get(1));
                    stacks.get(2).decrement(count.get(2));
                    progression = 0;
                }
                if (!inventory.get(0).getItem().equals(lastItem[0]) || !inventory.get(1).getItem().equals(
                        lastItem[1]) || !inventory.get(2).getItem().equals(lastItem[2])) {
                    lastItem = new Item[]{
                            inventory.get(0).getItem(),
                            inventory.get(1).getItem(),
                            inventory.get(2).getItem()
                    };
                    progression = 0;
                }
            } else {
                running = false;
                progression = 0;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            chargeUseItem(inventory.get(3));
        }
        super.tick(world, pos, state, blockEntity);
    }

    public boolean checkCount(String key) {
        List<Integer> count = ReactionRecipe.counts.get(key);
        List<Integer> inv = ItemHelper.makePair(inventory.get(0), inventory.get(1), inventory.get(2));
        return inv.get(0) >= count.get(0) && inv.get(1) >= count.get(1) && inv.get(2) >= count.get(2);
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
                inventory.get(1).getItem(),
                inventory.get(2).getItem()
        };
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

}
