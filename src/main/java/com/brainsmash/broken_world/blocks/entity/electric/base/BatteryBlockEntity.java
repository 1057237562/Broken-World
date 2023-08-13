package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.items.electrical.BatteryItem;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.BatteryGuiDescription;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BatteryBlockEntity extends CableBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, PropertyDelegateHolder {

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return getEnergy();
                case 1:
                    return getMaxCapacity();
                default:
                    return -1;
            }
        }

        @Override
        public void set(int index, int value) {
            setEnergy(value);
        }

        @Override
        public int size() {
            return 2;
        }
    };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.BATTERY_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BatteryGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient && world.isChunkLoaded(pos)) {
            increaseEnergy(deltaFlow);
            EnergyManager.processTick(this);
            if (!inventory.get(0).isEmpty() && inventory.get(0).getItem() instanceof BatteryItem batteryItem) {
                setEnergy(getEnergy() - batteryItem.charge(inventory.get(0), Math.min(getEnergy(), getMaxFlow())));
            }
        }
    }
}
