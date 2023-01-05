package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerBlockEntity extends CableBlockEntity {

    protected boolean running = false;
    private int generatePower = 50;

    public PowerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.POWER_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    public PowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }

    @Override
    public int getMaxFlow() {
        return 32;
    }

    public void setGenerate(int p){
        generatePower = p;
    }
    public int getGenerate() {
        return running ? generatePower : 0;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient && world.isChunkLoaded(pos)) {
            increaseEnergy(getGenerate());
            increaseEnergy(deltaFlow);
            EnergyManager.processTick(this);
        }
    }
}
