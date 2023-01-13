package com.brainsmash.broken_world.blocks.entity.electric.generator;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.JumboFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.BurnTimeRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.GeneratorGuiDescription;
import com.brainsmash.broken_world.util.ThermalFluidInv;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
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

public class ThermalGeneratorEntity extends PowerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, PropertyDelegateHolder {


    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index){
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

    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET.mul(16);

    public final ThermalFluidInv fluidInv;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public ThermalGeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.THERMAL_GENERATOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        setGenerate(10);
        fluidInv = new ThermalFluidInv(1, SINGLE_TANK_CAPACITY);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient) {
            if (!fluidInv.getInvFluid(0).isEmpty() && fluidInv.getInvFluid(0).amount().isPositive() && getEnergy() < getMaxCapacity()) {
                running = true;
                fluidInv.getInvFluid(0).split(FluidAmount.of1620(1));
                markDirty();
            } else {
                running = false;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        FluidVolume invFluid = fluidInv.getInvFluid(0);
        if (!invFluid.isEmpty()) {
            nbt.put("fluid", invFluid.toTag());
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        if (nbt.contains("fluid")) {
            FluidVolume fluid = FluidVolume.fromTag(nbt.getCompound("fluid"));
            fluidInv.setInvFluid(0, fluid, Simulation.ACTION);
        }
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
}
