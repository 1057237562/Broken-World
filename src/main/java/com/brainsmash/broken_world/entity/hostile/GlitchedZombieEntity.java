package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class GlitchedZombieEntity extends ZombieEntity {

    public GlitchedZombieEntity(EntityType<? extends GlitchedZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }
}
