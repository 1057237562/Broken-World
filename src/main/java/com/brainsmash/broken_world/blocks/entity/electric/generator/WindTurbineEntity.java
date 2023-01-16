package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class WindTurbineEntity extends PowerBlockEntity {

    protected int nearbyTurbineCount = 0;
    protected final boolean bellowSeaLvl;
    public WindTurbineEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WIND_TURBINE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        bellowSeaLvl = pos.getY() < this.pos
        setGenerate(calculateGenerate());
        running = !bellowSeaLvl;
    }

    protected int calculateGenerate(){
        return 114514;
    }

    public void moreCrowded(){
        running = false;
        nearbyTurbineCount++;
    }

    public void lessCrowded(){
        nearbyTurbineCount--;
        if(nearbyTurbineCount == 0)
            running = !bellowSeaLvl;
    }
}
