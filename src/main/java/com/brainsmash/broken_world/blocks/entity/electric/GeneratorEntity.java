package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.BurnTimeRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.GeneratorGuiDescription;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GeneratorEntity extends PowerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, PropertyDelegateHolder, SidedInventory {

    public int fuelTime = 0;
    public int maxFuelTime = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index){
                case 0:
                    return getEnergy();
                case 1:
                    return getMaxCapacity();
                case 2:
                    return fuelTime;
                case 3:
                    return maxFuelTime;
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
            return 4;
        }
    };


    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public GeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.GENERATOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        setGenerate(4);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(fuelTime > 0){
            running = true;
            fuelTime--;
            markDirty();
        }else{
            ItemStack fuel = inventory.get(0);
            if(!fuel.isEmpty() && getEnergy() < getMaxCapacity()){
                BurnTimeRegister.getGeneratorMap();
                maxFuelTime = fuelTime = BurnTimeRegister.generator_fuel.getOrDefault(fuel.getItem(),0);
                if(fuelTime > 0) {
                    running = true;
                    fuel.decrement(1);
                    markDirty();
                }
            }else{
                running = false;
            }
        }
        state = state.with(Properties.LIT, isRunning());
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        fuelTime = nbt.getInt("fuelTime");
        maxFuelTime = nbt.getInt("maxFuelTime");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("fuelTime", fuelTime);
        nbt.putInt("maxFuelTime", maxFuelTime);
        super.writeNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        //return new TeleporterControllerScreenHandler(syncId, playerInventory, this);
        return new GeneratorGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world,pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0};
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
