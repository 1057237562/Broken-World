package com.brainsmash.broken_world.blocks.entity.cable;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CableBlockEntity extends BlockEntity implements BlockEntityTicker<CableBlockEntity> {

    private long energy = 0;
    public boolean tickMark = false;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(Main.CABLE_ENTITY_TYPE, pos, state);
    }
    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos,BlockState state){ super(type,pos,state);}

    public int getMaxCapacity(){
        return 10;
    }

    public int getMaxFlow(){
        return 5;
    }

    public int getEnergyLost(){
        return 1;
    }

    public long getEnergy(){
        return energy;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        EnergyManager.processTick(this);
    }

    BlockEntity getAdjacentBlockEntity(Direction direction) {
        return world.getBlockEntity(getPos().offset(direction));
    }

    public void setEnergy(long l) {
        energy = l;
    }

    public void increaseEnergy(long l){
        energy += l;
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        if (compound.contains("energy")) {
            energy = compound.getLong("energy");
        }
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        super.writeNbt(compound);
        compound.putLong("energy", energy);
    }
}
