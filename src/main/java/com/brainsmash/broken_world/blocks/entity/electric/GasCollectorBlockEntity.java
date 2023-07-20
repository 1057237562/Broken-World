package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.GasCollectorRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
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

import java.util.List;
import java.util.Random;

public class GasCollectorBlockEntity extends ConsumerBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final Random random = new Random();
    private Item lastItem;
    int selectedGas = 0;

    public GasCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.GAS_COLLECTOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        maxProgression = 150;
        powerConsumption = 4;
    }

    public void selectGasOutput(int i) {
        selectedGas = i;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GasCollectorGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos), selectedGas);
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
        if (!world.isClient) {
            ItemStack rawMaterial = inventory.get(0);
            if (GasCollectorRecipe.recipes.containsKey(rawMaterial.getItem()) && canRun()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    List<Pair<Float, Item>> list = GasCollectorRecipe.recipes.get(rawMaterial.getItem());
                    for (Pair<Float, Item> output : list) {
                        if (random.nextFloat() < output.getLeft()) {
                            if (!insertItem(new ItemStack(output.getRight(), 1))) {
                                EntityHelper.spawnItem(world, new ItemStack(output.getRight(), 1), 1, Direction.UP,
                                        pos);
                            }
                        }
                    }
                    if (inventory.get(0).getRecipeRemainder().isEmpty()) inventory.get(0).decrement(1);
                    else inventory.set(0, inventory.get(0).getRecipeRemainder().copy());
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
        }
        super.tick(world, pos, state, blockEntity);
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
