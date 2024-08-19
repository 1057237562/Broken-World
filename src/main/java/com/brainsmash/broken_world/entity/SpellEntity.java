package com.brainsmash.broken_world.entity;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellEntity extends ProjectileEntity {

    private static final TrackedData<String> SEQ = DataTracker.registerData(SpellEntity.class,
            TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(SpellEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);
    public List<Integer> seq = new ArrayList<>();
    public Vec3d normal;
    public Vec2f rot;
    public float tick = 0f;
    public boolean active = false;
    public float scale;
    public Vec3d shift;

    private int age = 0;

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
        seq.add(-1);
    }

    public SpellEntity(EntityType<SpellEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SEQ, "-1");
        this.dataTracker.startTracking(ACTIVE, false);
    }

    private boolean isInRange(Vec3d pos) {
        Quaternion rotate = new Quaternion(0, 0, 0, 1);
        rotate.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rot.y));
        rotate.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(rot.x));
        rotate.conjugate();

        Vec3f projection = new Vec3f(pos.subtract(getPos().add(shift)));
        projection.rotate(rotate);
        return new Vec2f(projection.getX(), projection.getY()).length() < 1.25;
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            if (world instanceof ServerWorld serverWorld) {
                if (++age == 20) {
                    discard();
                }
                Quaternion rotate = new Quaternion(0, 0, 0, 1);
                rotate.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rot.y));
                rotate.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(rot.x));
                int zOffset = 64;
                Vec3d[] points = {
                        new Vec3d(1.25, 1.25, 0),
                        new Vec3d(1.25, -1.25, 0),
                        new Vec3d(-1.25, 1.25, 0),
                        new Vec3d(-1.25, -1.25, 0),
                        new Vec3d(1.25, 1.25, zOffset),
                        new Vec3d(1.25, -1.25, zOffset),
                        new Vec3d(-1.25, 1.25, zOffset),
                        new Vec3d(-1.25, -1.25, zOffset)
                };
                double[] start = {
                        Double.MAX_VALUE,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE
                };
                double[] end = {
                        Double.MIN_VALUE,
                        Double.MIN_VALUE,
                        Double.MIN_VALUE
                };
                for (Vec3d point : points) {
                    Vec3f vec3f = new Vec3f(point);
                    vec3f.rotate(rotate);
                    Vec3d pos = getPos().add(shift).add(new Vec3d(vec3f));
                    start[0] = Math.min(start[0], pos.x);
                    start[1] = Math.min(start[1], pos.y);
                    start[2] = Math.min(start[2], pos.z);
                    end[0] = Math.max(end[0], pos.x);
                    end[1] = Math.max(end[1], pos.y);
                    end[2] = Math.max(end[2], pos.z);
                }

                List<LivingEntity> entities = serverWorld.getNonSpectatingEntities(LivingEntity.class,
                        new Box(new Vec3d(start[0], start[1], start[2]), new Vec3d(end[0], end[1], end[2])));
                for (LivingEntity entity : entities) {
                    if (isInRange(entity.getEyePos()) || isInRange(entity.getPos())) {
                        System.out.println(entity);
                    }
                }
            }
            return;
        }
        if (getOwner() != null && normal != null && rot != null) {
            if (!world.isClient) {
                if (getOwner() instanceof PlayerDataExtension dataExtension) {
                    if (dataExtension.getSpellEntity() != this) {
                        discard();
                    }
                }
                shift = getOwner().getEyePos().subtract(normal.negate().add(getPos()));

                Quaternion rotation = new Quaternion(0, 0, 0, 1);
                rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rot.y));
                rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(rot.x));
                rotation.conjugate();

                double u = normal.negate().dotProduct(normal) / normal.negate().dotProduct(
                        getOwner().getRotationVector());
                Vec3f vec3f = new Vec3f(getOwner().getRotationVector().multiply(u).add(normal.negate()));
                vec3f.rotate(rotation);
                Vec2f coord = new Vec2f(vec3f.getX(), vec3f.getY());

                double degree = 180 + Math.signum(coord.y) * Math.toDegrees(
                        Math.acos(coord.normalize().dot(new Vec2f(1, 0))));
                int phase = ((Math.toIntExact(Math.round(degree + 22.5))) / 45) % 8;
                if (coord.length() > 0.87 && coord.length() < 1.2 && seq.get(seq.size() - 1) != phase) {
                    seq.add(phase);
                    dataTracker.set(SEQ, StringUtils.join(seq, ','));
                }
            }
            return;
        }
        discard();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (!world.isClient) {
            return;
        }
        if (data == SEQ) {
            seq = Arrays.stream(dataTracker.get(SEQ).split(",")).map(Integer::parseInt).toList();
        }
        if (data == ACTIVE) {
            active = dataTracker.get(ACTIVE);
            stopRiding();
        }
        super.onTrackedDataSet(data);
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
            seq.add(-1);
            setPosition(pos.x, pos.y, pos.z);
        }

    }

    public void releaseSpell() {
        // TODO : implement spell intepreter
        System.out.println(StringUtils.join(seq, ','));
        if (seq.size() > 1) {
            active = true;
            dataTracker.set(ACTIVE, true);
            stopRiding();
        } else {
            discard();
        }
    }
}
