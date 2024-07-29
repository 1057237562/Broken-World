package com.brainsmash.broken_world.entity.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicBroomEntity extends VehicleEntity {

    public MagicBroomEntity(EntityType<? extends VehicleEntity> entityType, World world) {
        super(entityType, world);
        airStrafingSpeed = 0.05f;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void travel(Vec3d movementInput) {
        LivingEntity livingEntity = (LivingEntity) getFirstPassenger();
        if (livingEntity == null) {
            return;
        }
        this.prevYaw = this.getYaw();
        this.setHeadYaw(livingEntity.getHeadYaw());
        this.setRotation(livingEntity.getHeadYaw(), 0);

        float sidewaysSpeed = livingEntity.sidewaysSpeed * 0.5f;
        float forwardSpeed = livingEntity.forwardSpeed;

        super.travel(new Vec3d(sidewaysSpeed, upStrafingSpeed, forwardSpeed));
    }
}
