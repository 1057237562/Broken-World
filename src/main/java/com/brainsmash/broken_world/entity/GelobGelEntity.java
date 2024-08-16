package com.brainsmash.broken_world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class GelobGelEntity extends Entity {
    protected static final TrackedData<Float> GEL_SCALE = DataTracker.registerData(GelobGelEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected float scale = 1.0f;
    protected int tickCnt = 0;
    public GelobGelEntity(EntityType<? extends GelobGelEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        tickCnt++;
        if (tickCnt < 60)
            return;
        scale -= 0.05f;
        if (scale <= 0) {
            discard();
        }
    }

    public void setScale(float scale) {
        dataTracker.set(GEL_SCALE, scale);
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (data == GEL_SCALE) {
            scale = dataTracker.get(GEL_SCALE);
            tickCnt = 0;
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(scale);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(GEL_SCALE, 1.0f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        scale = nbt.getFloat("scale");
        tickCnt = nbt.getInt("tick_count");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("scale", scale);
        nbt.putInt("tick_count", tickCnt);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, 0);
    }
}
