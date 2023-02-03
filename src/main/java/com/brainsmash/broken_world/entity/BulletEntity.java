package com.brainsmash.broken_world.entity;

import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BulletEntity extends ProjectileEntity {

    private double damage = 1.25f;

    private int life;

    protected BulletEntity(EntityType<? extends BulletEntity> type, double x, double y, double z, World world) {
        this((EntityType<BulletEntity>) type, world);
        this.setPosition(x, y, z);
    }

    protected BulletEntity(EntityType<? extends BulletEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - (double) 0.1f, owner.getZ(), world);
        this.setOwner(owner);
    }

    public BulletEntity(World world, LivingEntity owner) {
        this(EntityRegister.BULLET_ENTITY_ENTITY_TYPE, owner, world);
    }

    public BulletEntity(EntityType<BulletEntity> bulletEntityEntityType, World world) {
        super(bulletEntityEntityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!entity.canHit()) return;
        float f = (float) this.getVelocity().length();
        int i = MathHelper.ceil(MathHelper.clamp((double) f * this.damage, 0.0, 2.147483647E9));
        if (this.isHeadShot()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int) Math.min(l + (long) i, Integer.MAX_VALUE);
        }
        if (entity.damage(DamageSource.thrownProjectile(this, getOwner()), i)) {
            if (entity.getType() == EntityType.ENDERMAN) return;
        }
        discard();
    }

    @Override
    protected void initDataTracker() {

    }

    protected static float updateRotation(float prevRot, float newRot) {
        while (newRot - prevRot < -180.0f) {
            prevRot -= 360.0f;
        }
        while (newRot - prevRot >= 180.0f) {
            prevRot += 360.0f;
        }
        return MathHelper.lerp(0.2f, prevRot, newRot);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
        this.life = 0;
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition,
                this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    protected float getDragInWater() {
        return 0.1f;
    }

    @Override
    public void tick() {
        Vec3d vec3d2;
        VoxelShape voxelShape;
        BlockPos blockPos;
        BlockState blockState;
        super.tick();
        Vec3d vec3d = this.getVelocity();
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            double d = vec3d.horizontalLength();
            this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
        if (!((blockState = this.world.getBlockState(
                blockPos = this.getBlockPos())).isAir() || (voxelShape = blockState.getCollisionShape(this.world,
                blockPos)).isEmpty())) {
            vec3d2 = this.getPos();
            for (Box box : voxelShape.getBoundingBoxes()) {
                if (!box.offset(blockPos).contains(vec3d2)) continue;
                break;
            }
        }
        if (this.isTouchingWaterOrRain() || blockState.isOf(Blocks.POWDER_SNOW)) {
            this.extinguish();
        }
        Vec3d vec3d3 = this.getPos();
        HitResult hitResult = this.world.raycast(
                new RaycastContext(vec3d3, vec3d2 = vec3d3.add(vec3d), RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d2 = hitResult.getPos();
        }
        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d2);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer(
                        (PlayerEntity) entity)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }
            if (hitResult != null) {
                this.onCollision(hitResult);
                this.velocityDirty = true;
            }
            if (entityHitResult == null) break;
            hitResult = null;
        }
        vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        double l = vec3d.horizontalLength();
        this.setYaw((float) (MathHelper.atan2(e, g) * 57.2957763671875));
        this.setPitch((float) (MathHelper.atan2(f, l) * 57.2957763671875));
        this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
        this.setYaw(updateRotation(this.prevYaw, this.getYaw()));
        float m = 0.99f;
        float n = 0.05f;
        if (this.isTouchingWater()) {
            for (int o = 0; o < 4; ++o) {
                float p = 0.25f;
                this.world.addParticle(ParticleTypes.BUBBLE, h - e * 0.25, j - f * 0.25, k - g * 0.25, e, f, g);
            }
            m = this.getDragInWater();
        }
        this.setVelocity(vec3d.multiply(m));
        if (!this.hasNoGravity()) {
            Vec3d vec3d4 = this.getVelocity();
            this.setVelocity(vec3d4.x, vec3d4.y - (double) 0.05f, vec3d4.z);
        }
        this.setPosition(h, j, k);
        this.checkBlockCollision();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK,
                world.getBlockState(blockHitResult.getBlockPos()));
        for (int i = 0; i < 8; ++i) {
            this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
        discard();
    }

    //TODO: Make a headshot detector
    private boolean isHeadShot() {
        return false;
    }
}
