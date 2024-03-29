package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.ElectrolyzerRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.ElectrolyzerGuiDescription;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ElectrolyzerBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private Item lastItem;
    public float tick = 0;

    public ElectrolyzerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.ELECTROLYZER_ENTITY_TYPE, pos, state);
        maxProgression = 100;
        setMaxCapacity(1000);
        powerConsumption = 16;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (ElectrolyzerRecipe.recipes.containsKey(inventory.get(0).getItem()) && checkCount() && checkEnergy()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    ItemStack output = ElectrolyzerRecipe.recipes.get(inventory.get(0).getItem()).copy();
                    if (!insertItem(output)) {
                        EntityHelper.spawnItem(world, output, 1, Direction.UP, pos);
                    }
                    inventory.get(0).decrement(ElectrolyzerRecipe.counts.get(inventory.get(0).getItem()));
                    progression = 0;
                }
                if (!inventory.get(0).getItem().equals(lastItem) || !checkCount()) {
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

    private boolean checkCount() {
        return inventory.get(0).getCount() >= ElectrolyzerRecipe.counts.get(inventory.get(0).getItem());
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
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ElectrolyzerGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        running = nbt.getBoolean("running");
        lastItem = inventory.get(0).getItem();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putBoolean("running", running);
        super.writeNbt(nbt);
    }
}
