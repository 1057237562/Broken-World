package com.brainsmash.broken_world.entity;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SpellEntity extends ProjectileEntity {

    private List<Short> list;
    private LivingEntity owner;
    public Vec3d normal;

    public SpellEntity(World world) {
        super(EntityRegister.SPELL_ENTITY_ENTITY_TYPE, world);
    }

    public SpellEntity(LivingEntity owner, World world, Vec3d initialRot) {
        super(EntityRegister.SPELL_ENTITY_ENTITY_TYPE, world);
        this.owner = owner;
        if (owner instanceof PlayerDataExtension dataExtension) {
            dataExtension.setSpellEntity(this);
        }
        normal = initialRot;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean saveNbt(NbtCompound nbt) {
        return super.saveNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
