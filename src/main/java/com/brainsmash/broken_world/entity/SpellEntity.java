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
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;

public class SpellEntity extends ProjectileEntity {

    public Vec3d normal;
    public Vec2f rot;
    public float tick = 0f;
    public float scale;
    private List<Short> list;

    public SpellEntity(World world) {
        super(EntityRegister.SPELL_ENTITY_TYPE, world);
    }

    public SpellEntity(LivingEntity owner, World world) {
        super(EntityRegister.SPELL_ENTITY_TYPE, world);
        this.setOwner(owner);
        this.startRiding(owner);
        if (owner instanceof PlayerDataExtension dataExtension) {
            dataExtension.setSpellEntity(this);
        }
        normal = owner.getRotationVector();
        rot = new Vec2f(owner.getPitch(), owner.getYaw());
        Vec3d pos = owner.getPos().add(0, owner.getMountedHeightOffset(), 0);
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
        if (!world.isClient && getOwner() != null && rot != null) {
            if (getOwner() instanceof PlayerDataExtension dataExtension) {
                if (dataExtension.getSpellEntity() != this) {
                    discard();
                }
            }

            Quaternion rotation = new Quaternion(0, 0, 0, 1);
            rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rot.y));
            rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(rot.x));
            rotation.conjugate();

            double u = normal.negate().dotProduct(normal) / normal.negate().dotProduct(getOwner().getRotationVector());
            Vec3f vec3f = new Vec3f(getOwner().getRotationVector().multiply(u).add(normal.negate()));
            vec3f.rotate(rotation);
            Vec2f coord = new Vec2f(vec3f.getX(), vec3f.getY());

            double degree = Math.acos(coord.normalize().dot(new Vec2f(1, 0)));
            System.out.println(180 + Math.signum(coord.y) * Math.toDegrees(degree));
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
            rot = new Vec2f(getOwner().getPitch(), getOwner().getYaw());
            Vec3d pos = getOwner().getPos().add(0, getOwner().getMountedHeightOffset(), 0);
            setPosition(pos.x, pos.y, pos.z);
        }

    }
}
