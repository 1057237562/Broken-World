package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.WeaponryGuiDescription;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
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

public class WeaponryBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(19, ItemStack.EMPTY);
    public ItemStack output = ItemStack.EMPTY;
    public Map<Item, Integer> requiredMaterial = new ConcurrentHashMap<>();

    protected boolean powered = false;

    public boolean isPowered() {
        return powered;
    }

    public WeaponryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WEAPONRY_ENTITY_TYPE, pos, state);
        maxProgression = 100;
        powerConsumption = 12;
        setMaxCapacity(1000);
        powered = true;
    }

    public WeaponryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setOutput(ItemStack output) {
        if (!this.output.equals(output)) {
            progression = 0;
            this.output = output;
            requiredMaterial.clear();
            for (int i = 0; i < 9; i++) {
                requiredMaterial.merge(inventory.get(i).getItem(), inventory.get(i).getCount(),
                        (integer, integer2) -> integer + integer2);
            }
        }
    }

    public boolean checkMaterial() {
        Map<Item, Integer> currentMaterial = new ConcurrentHashMap<>();
        for (int i = 9; i < 18; i++) {
            currentMaterial.merge(inventory.get(i).getItem(), inventory.get(i).getCount(),
                    ((integer, integer2) -> integer + integer2));
        }
        for (Map.Entry<Item, Integer> pair : requiredMaterial.entrySet()) {
            if (currentMaterial.getOrDefault(pair.getKey(), 0) < pair.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void consumeMaterial() {
        Map<Item, Integer> rest = new ConcurrentHashMap<>();
        rest.putAll(requiredMaterial);
        for (ItemStack stack : inventory.subList(9, 17)) {
            int request = rest.getOrDefault(stack.getItem(), 0);
            if (request > 0) {
                int managed = Math.min(request, stack.getCount());
                rest.compute(stack.getItem(), (item, integer) -> integer - managed);
                stack.decrement(managed);
            }
        }
    }

    public boolean insertItem(ItemStack stack) {
        for (int i = 9; i < 18; i++) {
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
        if (world instanceof ServerWorld) {
            if (!output.isEmpty() && isPowered() && canRun() && !output.isEmpty() && ((inventory.get(18).isOf(
                    output.getItem()) && inventory.get(
                    18).getCount() + output.getCount() <= output.getMaxCount()) || inventory.get(18).isEmpty())) {
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
                            inventory.set(18, output.copy());
                        } else {
                            inventory.get(18).increment(output.getCount());
                        }
                        super.tick(world, pos, state, blockEntity);
                        running = false;
                        return;
                    }
                }
            } else {
                running = false;
            }
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
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
        return new WeaponryGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        NbtCompound compound = new NbtCompound();
        output.writeNbt(compound);
        nbt.put("output", compound);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        setOutput(ItemStack.fromNbt(nbt.getCompound("output")));
    }
}
