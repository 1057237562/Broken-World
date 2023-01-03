package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerBlockEntity extends CableBlockEntity{

    private boolean running = false;

    public PowerBlockEntity(BlockPos pos, BlockState state) {
        super(Main.POWER_ENTITY_TYPE, pos, state);
        setMaxCapacity(10000);
    }

    @Override
    public int getMaxFlow() {
        return 32;
    }

    public int getGenerate() {
        return 50;
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
        if(!world.isClient) {
            increaseEnergy(getGenerate());
            increaseEnergy(deltaFlow);
            EnergyManager.processTick(this);
        }
    }
}
