package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class ApocalyptorEntity extends HostileEntity {
    public ApocalyptorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer createApocalyptorAttributes() {
        return createHostileAttributes().build();
    }
}
