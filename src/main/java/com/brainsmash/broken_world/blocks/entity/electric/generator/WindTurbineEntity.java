package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.world.World;

public class WindTurbineEntity extends PowerBlockEntity {

    protected int nearbyTurbineCount = 0;
    public Quaternion rotationMatrix;
    public WindTurbineEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WIND_TURBINE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
    }

    @Override
    public void setWorld(World world) {
        rotationMatrix = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING).getRotationQuaternion();
        super.setWorld(world);
    }

    private int sigmoid(int x, int s, int e){
        double f = 2.0*x/(e-s)-1;
        return (int) (20*(1/(1+Math.exp(f))));
    }

    protected int calculateGenerate(World world){
        return sigmoid(Math.max(0,pos.getY() - world.getSeaLevel()),0,world.getDimension().height()-world.getSeaLevel());
    }

    public void moreCrowded(){
        running = false;
        nearbyTurbineCount++;
        markDirty();
    }

    public void lessCrowded(){
        nearbyTurbineCount--;
        markDirty();
        if(nearbyTurbineCount == 0)
            running = getGenerate() != 0;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("nearby",nearbyTurbineCount);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        nearbyTurbineCount = nbt.getInt("nearby");
    }
}
