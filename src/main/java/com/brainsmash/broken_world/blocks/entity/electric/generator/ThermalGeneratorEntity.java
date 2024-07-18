package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.ThermalGeneratorGuiDescription;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
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

    public final SingleFluidStorage<FluidVariant> fluidStorage = new SingleFluidStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return 8 * FluidConstants.BUCKET;
        }
    };

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
                    return (int) fluidStorage.getCapacity();
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
                    fluidStorage.amount = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public int getAmount() {
        return (int) fluidStorage.amount;
    }

    public ThermalGeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.THERMAL_GENERATOR_ENTITY_TYPE, pos, state);
        setMaxCapacity(4000);
        setGenerate(16);
        fluidStorage.variant = FluidVariant.of(Fluids.LAVA);
        fluidStorage.amount = 0;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (!fluidStorage.isEmpty() && fluidStorage.amount >= 300 && getEnergy() < getMaxCapacity()) {
                running = true;
                fluidStorage.amount -= 300;
            } else {
                running = false;
            }
            if (inventory.get(1).isOf(Items.LAVA_BUCKET)) {
                if (fluidStorage.isEmpty()) {
                    fluidStorage.amount += FluidConstants.BUCKET;
                    inventory.set(1, new ItemStack(Items.BUCKET, 1));
                } else {
                    if (fluidStorage.amount <= fluidStorage.getCapacity() - FluidConstants.BUCKET) {
                        fluidStorage.amount += FluidConstants.BUCKET;
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
        fluidStorage.amount = nbt.getLong("fluidAmount");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putLong("fluidAmount", fluidStorage.amount);
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
