package com.brainsmash.broken_world.entity;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SpellEntity extends ProjectileEntity {

    private List<Short> list;
    public Vec3d normal;

    public SpellEntity(World world) {
        super(EntityRegister.SPELL_ENTITY_TYPE, world);
    }

    public SpellEntity(LivingEntity owner, World world) {
        super(EntityRegister.SPELL_ENTITY_TYPE, world);
        this.setOwner(owner);
        if (owner instanceof PlayerDataExtension dataExtension) {
            dataExtension.setSpellEntity(this);
        }
        normal = owner.getRotationVector();
        Vec3d pos = owner.getEyePos().add(normal);
        setPosition(pos.x, pos.y, pos.z);
    }

    public SpellEntity(EntityType<SpellEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() != null && normal != null) {
            Vec3d pos = getOwner().getEyePos().add(normal);
            setPosition(pos.x, pos.y, pos.z);
        }
    }

    @Override
    public boolean saveNbt(NbtCompound nbt) {
        return super.saveNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 4096;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        Entity entity = this.getOwner();
        return new EntitySpawnS2CPacket(this, entity == null ? this.getId() : entity.getId());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (this.getOwner() == null) {
            this.discard();
        } else {
            normal = getOwner().getRotationVector();
            Vec3d pos = getOwner().getEyePos().add(normal);
            setPosition(pos.x, pos.y, pos.z);
        }

    }
}
