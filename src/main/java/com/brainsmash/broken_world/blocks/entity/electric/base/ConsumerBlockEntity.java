package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.BlockRegister;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConsumerBlockEntity extends CableBlockEntity implements PropertyDelegateHolder {

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

    private boolean running = false;

    public ConsumerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CONSUMER_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    public ConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient && world.isChunkLoaded(pos)) {
            increaseEnergy(deltaFlow);
            EnergyManager.processTick(this);
        }
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
