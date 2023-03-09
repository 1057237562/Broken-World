package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.world.World;

public class DroneEntity extends FlyingEntity {
    public DroneEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }
    
}
