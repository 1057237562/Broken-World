package com.brainsmash.broken_world.entity.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class MagicBroom extends VehicleBase {
    protected MagicBroom(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }


}
