package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;

public class PhoenixEntity extends HostileEntity {

    private float eyeOffset = 0.5f;
    private int eyeOffsetCooldown;

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 0f;

    public PhoenixEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        moveControl = new PhoenixMoveControl(this);
    }

    public static DefaultAttributeContainer.Builder createPhoenixAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0).add(
                EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0).add(EntityAttributes.GENERIC_MAX_HEALTH, 400);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new ShootFireballGoal(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtTargetGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(7, new FlyRandomlyGoal(this));
        this.goalSelector.add(7, new WanderAroundFarGoal((PathAwareEntity) this, 1.0, 0.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, true));
    }

    @Override
    public void tickMovement() {
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.onGround ? -1.0f : 4.0f) * 0.3f;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f);
        if (!this.onGround && this.flapSpeed < 0.3f) {
            this.flapSpeed = 0.3f;
        }
        this.flapSpeed *= 0.9f;
        if (!this.onGround && this.getVelocity().y < 0.0) {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress += this.flapSpeed * 2.0f;
        if (this.world.isClient) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5,
                        SoundEvents.BLOCK_FIRE_AMBIENT, this.getSoundCategory(), 1.0f + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5), this.getRandomBodyY(),
                        this.getParticleZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        super.tickMovement();
    }

    @Override
    protected void mobTick() {
        LivingEntity livingEntity;
        --this.eyeOffsetCooldown;
        if (this.eyeOffsetCooldown <= 0) {
            this.eyeOffsetCooldown = 100;
            this.eyeOffset = (float) this.random.nextTriangular(0.5, 6.891);
        }
        if ((livingEntity = this.getTarget()) != null && livingEntity.getEyeY() > this.getEyeY() + (double) this.eyeOffset && this.canTarget(
                livingEntity)) {
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(this.getVelocity().add(0.0, ((double) 0.3f - vec3d.y) * (double) 0.3f, 0.0));
            this.velocityDirty = true;
        }
        super.mobTick();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            if (this.isTouchingWater()) {
                this.updateVelocity(0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.8f));
            } else if (this.isInLava()) {
                this.updateVelocity(0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.5));
            } else {
                float f = 0.91f;
                if (this.onGround) {
                    f = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0,
                            this.getZ())).getBlock().getSlipperiness() * 0.91f;
                }
                float g = 0.16277137f / (f * f * f);
                f = 0.91f;
                if (this.onGround) {
                    f = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0,
                            this.getZ())).getBlock().getSlipperiness() * 0.91f;
                }
                this.updateVelocity(this.onGround ? 0.1f * g : 0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(f));
            }
        }
        this.updateLimbs(this, false);
        //super.travel(movementInput);
    }

    static class PhoenixMoveControl extends MoveControl {
        private final PhoenixEntity phoenixEntity;
        private int collisionCheckCooldown;

        public PhoenixMoveControl(PhoenixEntity phoenix) {
            super(phoenix);
            this.phoenixEntity = phoenix;
        }

        @Override
        public void tick() {
            if (this.state != MoveControl.State.MOVE_TO) {
                return;
            }
            if (this.collisionCheckCooldown-- <= 0) {
                this.collisionCheckCooldown += this.phoenixEntity.getRandom().nextInt(5) + 2;
                Vec3d vec3d = new Vec3d(this.targetX - this.phoenixEntity.getX(),
                        this.targetY - this.phoenixEntity.getY(), this.targetZ - this.phoenixEntity.getZ());
                double d = vec3d.length();
                if (this.willCollide(vec3d = vec3d.normalize(), MathHelper.ceil(d))) {
                    this.phoenixEntity.setVelocity(this.phoenixEntity.getVelocity().add(vec3d.multiply(0.5)));
                } else {
                    this.state = MoveControl.State.WAIT;
                }
            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.phoenixEntity.getBoundingBox();
            for (int i = 1; i < steps; ++i) {
                if (this.phoenixEntity.world.isSpaceEmpty(this.phoenixEntity, box = box.offset(direction))) continue;
                return false;
            }
            return true;
        }
    }

    static class ShootFireballGoal extends Goal {
        private final PhoenixEntity phoenixEntity;
        private int fireballsFired;
        private int fireballCooldown;
        private int targetNotVisibleTicks;

        public ShootFireballGoal(PhoenixEntity phoenix) {
            this.phoenixEntity = phoenix;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.phoenixEntity.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.phoenixEntity.canTarget(livingEntity);
        }

        @Override
        public void start() {
            this.fireballsFired = 0;
        }

        @Override
        public void stop() {
            this.targetNotVisibleTicks = 0;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            --this.fireballCooldown;
            LivingEntity livingEntity = this.phoenixEntity.getTarget();
            if (livingEntity == null) {
                return;
            }
            boolean bl = this.phoenixEntity.getVisibilityCache().canSee(livingEntity);
            this.targetNotVisibleTicks = bl ? 0 : ++this.targetNotVisibleTicks;
            double d = this.phoenixEntity.squaredDistanceTo(livingEntity);
            if (d < 4.0) {
                if (!bl) {
                    return;
                }
                if (this.fireballCooldown <= 0) {
                    this.fireballCooldown = 3;
                    this.phoenixEntity.tryAttack(livingEntity);
                }
                this.phoenixEntity.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(),
                        livingEntity.getZ(), 1.0);
            } else if (d < this.getFollowRange() * this.getFollowRange() && bl) {
                double e = livingEntity.getX() - this.phoenixEntity.getX();
                double f = livingEntity.getBodyY(0.5) - this.phoenixEntity.getBodyY(0.5);
                double g = livingEntity.getZ() - this.phoenixEntity.getZ();
                if (this.fireballCooldown <= 0) {
                    ++this.fireballsFired;
                    if (this.fireballsFired == 1) {
                        this.fireballCooldown = 3;
                    } else if (this.fireballsFired <= 4) {
                        this.fireballCooldown = 7;
                    } else {
                        this.fireballCooldown = 10;
                        this.fireballsFired = 0;
                    }
                    if (this.fireballsFired > 1) {
                        double h = Math.sqrt(Math.sqrt(d)) * 0.5;
                        if (!this.phoenixEntity.isSilent()) {
                            this.phoenixEntity.world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS,
                                    this.phoenixEntity.getBlockPos(), 0);
                        }
                        SmallFireballEntity smallFireballEntity = new SmallFireballEntity(this.phoenixEntity.world,
                                this.phoenixEntity, this.phoenixEntity.getRandom().nextTriangular(e, 0.78f * h), f,
                                this.phoenixEntity.getRandom().nextTriangular(g, 0.78f * h));
                        smallFireballEntity.setPosition(smallFireballEntity.getX(),
                                this.phoenixEntity.getBodyY(0.5) + 0.5, smallFireballEntity.getZ());
                        this.phoenixEntity.world.spawnEntity(smallFireballEntity);
                    }
                }
                this.phoenixEntity.getLookControl().lookAt(livingEntity, 10.0f, 10.0f);
            } else if (this.targetNotVisibleTicks < 5) {
                this.phoenixEntity.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(),
                        livingEntity.getZ(), 1.0);
            }
            super.tick();
        }

        private double getFollowRange() {
            return this.phoenixEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
    }

    static class FlyRandomlyGoal extends Goal {
        private final PhoenixEntity phoenixEntity;

        public FlyRandomlyGoal(PhoenixEntity phoenix) {
            this.phoenixEntity = phoenix;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            double f;
            double e;
            MoveControl moveControl = this.phoenixEntity.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            }
            double d = moveControl.getTargetX() - this.phoenixEntity.getX();
            double g = d * d + (e = moveControl.getTargetY() - this.phoenixEntity.getY()) * e + (f = moveControl.getTargetZ() - this.phoenixEntity.getZ()) * f;
            return g < 1.0 || g > 3600.0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.phoenixEntity.getRandom();
            double d = this.phoenixEntity.getX() + (double) ((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double e = MathHelper.clamp(
                    this.phoenixEntity.getY() + (double) ((random.nextFloat() * 2.0f - 1.0f) * 16.0f), 70, 180);
            double f = this.phoenixEntity.getZ() + (double) ((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.phoenixEntity.getMoveControl().moveTo(d, e, f, 1.0);
        }
    }

    static class LookAtTargetGoal extends Goal {
        private final PhoenixEntity phoenixEntity;

        public LookAtTargetGoal(PhoenixEntity phoenix) {
            this.phoenixEntity = phoenix;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.phoenixEntity.getTarget() == null) {
                Vec3d vec3d = this.phoenixEntity.getVelocity();
                this.phoenixEntity.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776f);
                this.phoenixEntity.bodyYaw = this.phoenixEntity.getYaw();
            } else {
                LivingEntity livingEntity = this.phoenixEntity.getTarget();
                double d = 64.0;
                if (livingEntity.squaredDistanceTo(this.phoenixEntity) < 4096.0) {
                    double e = livingEntity.getX() - this.phoenixEntity.getX();
                    double f = livingEntity.getZ() - this.phoenixEntity.getZ();
                    this.phoenixEntity.setYaw(-((float) MathHelper.atan2(e, f)) * 57.295776f);
                    this.phoenixEntity.bodyYaw = this.phoenixEntity.getYaw();
                }
            }
        }
    }
}
