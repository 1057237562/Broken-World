package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CableBlockEntity extends BlockEntity implements BlockEntityTicker<CableBlockEntity> {

    public Map<Direction, Integer> edges = new ConcurrentHashMap<>();
    public Direction parent = null;
    public int deltaFlow = 0;
    public int minFlow = 0;
    private int energy = 0;
    private int maxCapacity = 0;
    public boolean tickMark = false;
    public boolean visMark = false;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(Main.CABLE_ENTITY_TYPE, pos, state);
    }
    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos,BlockState state){ super(type,pos,state);}

    public int getMaxCapacity(){
        return maxCapacity;
    }

    public void setMaxCapacity(int cap){
        maxCapacity = cap;
    }

    public int getMaxFlow(){
        return 16;
    }

    /*public int getEnergyLost(){
        return 1;
    }*/

    public int getEnergy(){
        return energy;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {}

    BlockEntity getAdjacentBlockEntity(Direction direction) {
        return world.getBlockEntity(getPos().offset(direction));
    }

    public void setEnergy(int i) {
        energy = i;
    }

    public void increaseEnergy(int i){
        energy += i;
        energy = Math.min(energy,getMaxCapacity());
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        energy = compound.getInt("energy");
        maxCapacity = compound.getInt("maxCapacity");
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        super.writeNbt(compound);
        compound.putInt("energy", energy);
        compound.putInt("maxCapacity",maxCapacity);
    }

    public void ComputeDeltaFlow() {
        deltaFlow = 0;
        for(Direction direction : Direction.values()){
            if(getAdjacentBlockEntity(direction) instanceof CableBlockEntity neighbour){
                deltaFlow += (edges.getOrDefault(direction,0) - neighbour.edges.getOrDefault(direction.getOpposite(),0))/2;
            }
        }
    }
}
