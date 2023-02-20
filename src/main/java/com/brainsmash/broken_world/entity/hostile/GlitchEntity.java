package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class GlitchEntity extends HostileEntity {
    protected GlitchEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
}
