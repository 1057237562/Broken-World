package com.brainsmash.broken_world.entity.vehicle;

import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicBroomEntity extends VehicleEntity {

    public MagicBroomEntity(EntityType<? extends VehicleEntity> entityType, World world) {
        super(entityType, world);
        airStrafingSpeed = 0.05f;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity && !source.isProjectile()) {
            dropItems(source);
            discard();
        }
        return false;
    }

    protected void dropItems(DamageSource source) {
        this.dropItem(ItemRegister.get(ItemRegistry.MAGICAL_BROOM));
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
    public void tick() {
        super.tick();
        LivingEntity livingEntity = (LivingEntity) getFirstPassenger();
        if (livingEntity == null) {
            return;
        }
        this.prevYaw = this.getYaw();
        this.setHeadYaw(livingEntity.getBodyYaw());
        this.setRotation(livingEntity.getHeadYaw(), 0);
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float g = (float) ((this.isRemoved() ? 0.009999999776482582 : this.getMountedHeightOffset()) + passenger.getHeightOffset());
            Vec3d vec3d = (new Vec3d(0.0, -0.125, 0.0)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.getX() + vec3d.x, this.getY() + (double) g, this.getZ() + vec3d.z);
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        return getPos().add(0, 1f, 0);
    }

    @Override
    public void travel(Vec3d movementInput) {
        LivingEntity livingEntity = (LivingEntity) getFirstPassenger();
        if (livingEntity == null) {
            return;
        }

        float sidewaysSpeed = livingEntity.sidewaysSpeed * 0.5f;
        float forwardSpeed = livingEntity.forwardSpeed;

        super.travel(new Vec3d(sidewaysSpeed, upStrafingSpeed, forwardSpeed));
    }
}
