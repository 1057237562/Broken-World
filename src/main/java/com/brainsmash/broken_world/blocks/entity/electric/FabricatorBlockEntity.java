package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.FabricatorGuiDescription;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricatorBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(19, ItemStack.EMPTY);
    public ItemStack output;
    public Map<Item, Integer> requiredMaterial = new ConcurrentHashMap<>();

    public FabricatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.FABRICATOR_ENTITY_TYPE, pos, state);
        maxProgression = 100;
    }

    public void setOutput(ItemStack output) {
        if (!this.output.equals(output)) {
            progression = 0;
            this.output = output;
            requiredMaterial.clear();
            for (int i = 0; i < 9; i++) {
                requiredMaterial.merge(inventory.get(i).getItem(), inventory.get(i).getCount(), (integer, integer2) -> integer + integer2);
            }
        }
    }

    public boolean checkMaterial() {
        Map<Item, Integer> currentMaterial = new ConcurrentHashMap<>();
        for (int i = 9; i < 18; i++) {
            currentMaterial.merge(inventory.get(i).getItem(), inventory.get(i).getCount(), ((integer, integer2) -> integer + integer2));
        }
        boolean result = true;
        for (Map.Entry<Item, Integer> pair : requiredMaterial.entrySet()) {
            if (currentMaterial.getOrDefault(pair.getKey(), 0) < pair.getValue()) {
                result = false;
            }
        }
        return result;
    }

    public void consumeMaterial() {
        Map<Item, Integer> rest = new ConcurrentHashMap<>();
        rest.putAll(requiredMaterial);
        for (ItemStack stack : inventory.subList(9, 17)) {
            int request = rest.getOrDefault(stack.getItem(), 0);
            if (request > 0) {
                int managed = Math.min(request, stack.getCount());
                stack.decrement(managed);
                rest.compute(stack.getItem(), (item, integer) -> integer - managed);
            }
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (world instanceof ServerWorld) {
            if (!output.isEmpty() && canRun() && (inventory.get(18).getItem().equals(output) || inventory.get(18).isEmpty())) {
                if (!isRunning()) {
                    if (checkMaterial()) running = true;
                    else running = false;
                } else {
                    if (progression < maxProgression) {
                        progression++;
                    } else {
                        progression = 0;
                        consumeMaterial();
                        if (inventory.get(18).isEmpty()) {
                            inventory.set(18, output);
                        } else {
                            inventory.get(18).increment(1);
                        }
                        super.tick(world, pos, state, blockEntity);
                        running = false;
                        return;
                    }
                }
            } else {
                running = false;
            }
        }
        super.tick(world, pos, state, blockEntity);
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
        return new FabricatorGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }
}
