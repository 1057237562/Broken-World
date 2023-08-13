package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.items.electrical.BatteryItem;
import com.brainsmash.broken_world.registry.BlockRegister;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConsumerBlockEntity extends CableBlockEntity implements PropertyDelegateHolder {

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return getEnergy();
                case 1:
                    return getMaxCapacity();
                case 2:
                    return progression;
                case 3:
                    return maxProgression;
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
                    progression = value;
                    break;
                case 3:
                    maxProgression = value;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    protected boolean running = false;
    protected int powerConsumption = 0;
    protected int progression = 0;
    protected int maxProgression = 0;

    public ConsumerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CONSUMER_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    public boolean isRunning() {
        return running;
    }

    public ConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progression = nbt.getInt("progression");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("progression", progression);
        super.writeNbt(nbt);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient && world.isChunkLoaded(pos)) {
            increaseEnergy(deltaFlow);
            if (running) {
                increaseEnergy(-powerConsumption);
            }
            EnergyManager.processTick(this);
        }
    }

    public boolean checkEnergy() {
        if (powerConsumption <= getEnergy()) {
            return true;
        }
        return false;
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    public void chargeUseItem(ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof BatteryItem batteryItem) {
            setEnergy(getEnergy() + batteryItem.discharge(itemStack,
                    Math.min(getMaxCapacity() - getEnergy(), getMaxFlow())));
        }
    }
}
