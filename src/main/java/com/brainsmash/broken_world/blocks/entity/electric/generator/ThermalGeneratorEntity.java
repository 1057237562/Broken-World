package com.brainsmash.broken_world.blocks.entity.electric.generator;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.ThermalGeneratorGuiDescription;
import com.brainsmash.broken_world.util.ThermalFluidInv;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThermalGeneratorEntity extends PowerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, PropertyDelegateHolder {


    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET.mul(8);

    public final ThermalFluidInv fluidInv = new ThermalFluidInv(1, SINGLE_TANK_CAPACITY);

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return getEnergy();
                case 1:
                    return getMaxCapacity();
                case 2:
                    return getAmount();
                case 3:
                    return SINGLE_TANK_CAPACITY.as1620();
                default:
                    return -1;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    setEnergy(value);
                    break;
                case 1:
                    setMaxCapacity(value);
                    break;
                case 2:
                    fluidAmount = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    private int fluidAmount = 0;

    public int getAmount() {
        if (world.isClient) {
            return fluidAmount;
        } else {
            return fluidInv.getInvFluid(0).amount().as1620();
        }
    }

    public ThermalGeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.THERMAL_GENERATOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        setGenerate(12);
        fluidInv.setInvFluid(0, FluidKeys.LAVA.withAmount(FluidAmount.ZERO), Simulation.ACTION);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (!fluidInv.getInvFluid(0).isEmpty() && fluidInv.getInvFluid(0).amount().isGreaterThanOrEqual(FluidAmount.of1620(
                    6)) && getEnergy() < getMaxCapacity()) {
                running = true;
                fluidInv.getInvFluid(0).split(FluidAmount.of1620(6));
            } else {
                running = false;
            }
            if (inventory.get(1).isOf(Items.LAVA_BUCKET)) {
                if (fluidInv.getInvFluid(0).isEmpty()) {
                    if (fluidInv.setInvFluid(0, FluidKeys.LAVA.withAmount(FluidAmount.BUCKET), Simulation.ACTION)) {
                        inventory.set(1, new ItemStack(Items.BUCKET, 1));
                    }
                } else {
                    if (fluidInv.getInvFluid(0).amount().isLessThanOrEqual(SINGLE_TANK_CAPACITY.sub(FluidAmount.BUCKET)) && fluidInv.getInvFluid(
                            0).merge(FluidKeys.LAVA.withAmount(FluidAmount.BUCKET), Simulation.ACTION)) {
                        inventory.set(1, new ItemStack(Items.BUCKET, 1));
                    }
                }
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
        if (nbt.contains("fluid")) {
            FluidVolume fluid = FluidVolume.fromTag(nbt.getCompound("fluid"));
            fluidInv.setInvFluid(0, fluid, Simulation.ACTION);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        FluidVolume invFluid = fluidInv.getInvFluid(0);
        if (!invFluid.isEmpty()) {
            nbt.put("fluid", invFluid.toTag());
        }
        super.writeNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ThermalGeneratorGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
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
