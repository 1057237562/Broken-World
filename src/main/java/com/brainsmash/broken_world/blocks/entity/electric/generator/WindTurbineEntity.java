package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WindTurbineEntity extends PowerBlockEntity {

    protected int nearbyTurbineCount = 0;
    public WindTurbineEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WIND_TURBINE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
    }

    @Override
    public void setWorld(World world){
        setGenerate(calculateGenerate());
        running = getGenerate() != 0;
    }

    protected int calculateGenerate(){
        return Math.max(0,pos.getY() - world.getSeaLevel());
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
