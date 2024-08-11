package com.brainsmash.broken_world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class GelobGelEntity extends Entity {
    protected float scale = 1.0f;
    public GelobGelEntity(EntityType<? extends GelobGelEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        scale -= 0.05f;
        if (scale <= 0) {
            discard();
        }
    }

    public float getScale() {
        return scale;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        scale = nbt.getFloat("scale");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("scale", scale);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, 0);
    }
}
