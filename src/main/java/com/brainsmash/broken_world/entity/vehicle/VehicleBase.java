package com.brainsmash.broken_world.entity.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VehicleBase extends LivingEntity {
    protected VehicleBase(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return null;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public void travel(Vec3d movementInput) {
        super.travel(movementInput);
        LivingEntity livingEntity = (LivingEntity) getFirstPassenger();
        if (livingEntity == null) {
            return;
        }
        this.setYaw(livingEntity.getYaw());
        this.prevYaw = this.getYaw();
        this.setPitch(livingEntity.getPitch() * 0.5f);
        this.setRotation(this.getYaw(), this.getPitch());

        float sidewaysSpeed = livingEntity.sidewaysSpeed * 0.5f;
        float forwardSpeed = livingEntity.forwardSpeed;

        super.travel(new Vec3d(sidewaysSpeed, 0, forwardSpeed));
    }
}
