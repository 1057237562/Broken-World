package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.GasRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import com.brainsmash.broken_world.screenhandlers.descriptions.GasCollectorGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GasCollectorBlockEntity extends ConsumerBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private Item lastItem;
    private List<Pair<GasRegister.Gas, Integer>> gasList = null;
    int selectedGas = 0;

    public GasCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.GAS_COLLECTOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        maxProgression = 10000;
        powerConsumption = 4;
    }

    public void selectGasOutput(int i) {
        if (i == selectedGas)
            return;
        selectedGas = i;
        progression = 0;
        if (!gasList.isEmpty()) maxProgression = gasList.get(selectedGas).getRight();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GasCollectorGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos),
                selectedGas);
    }

    public boolean insertItem(ItemStack stack) {
        if (inventory.get(2).isEmpty()) {
            inventory.set(2, stack);
            return true;
        }
        if (inventory.get(2).getItem().equals(stack.getItem())) {
            int insertCount = Math.min(inventory.get(2).getMaxCount() - inventory.get(2).getCount(), stack.getCount());
            inventory.get(2).increment(insertCount);
            stack.decrement(insertCount);
        }
        return stack.getCount() == 0;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (gasList == null) {
            loadGasList();
        }
        if (!world.isClient) {
            if (canRun()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    Item product = gasList.get(selectedGas).getLeft().product();
                    if (!insertItem(new ItemStack(product, 1))) {
                        EntityHelper.spawnItem(world, new ItemStack(product, 1), 1, Direction.UP,
                                pos);
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

    protected void loadGasList() {
        if (world == null) {
            gasList = new ArrayList<>();
            return;
        }
        gasList = GasRegister.getBiomeGases(world, pos);
        if (!gasList.isEmpty()) {
            // If data pack is modified before loading this entity from save, gas list might be smaller, causing probable index out of bound.
            selectGasOutput(selectedGas % gasList.size());
            maxProgression = gasList.get(selectedGas).getRight();
        }
    }

    @Override
    public boolean canRun() {
        return super.canRun() && !gasList.isEmpty() && inventory.get(0).isOf(ItemRegister.items[ItemRegistry.GAS_TANK.ordinal()]);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(selectedGas);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        lastItem = inventory.get(0).getItem();
        selectedGas = nbt.getInt("selectedGas");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("selectedGas", selectedGas);
        super.writeNbt(nbt);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i % inventory.size();
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 1;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 2;
    }
}
