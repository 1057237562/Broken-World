package com.brainsmash.broken_world.entity.hostile;

import com.brainsmash.broken_world.entity.GelobGelEntity;
import com.brainsmash.broken_world.registry.EntityRegister;
import com.brainsmash.broken_world.registry.ParticleRegister;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GelobEntity extends SpiderEntity {
    public static final int TOUCH_DOWN_STRETCH_TICKS = 30;
    private static final TrackedData<Integer> GELOB_SIZE = DataTracker.registerData(SlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected Random random = Random.create();
    public float targetJumpStretch = 0;
    public float jumpStretch = 0;
    public float lastJumpStretch = 0;
    public float moveStretch = 0;
    public int onGroundTicks = 0;
    private boolean onGroundLastTick = false;

    public GelobEntity(EntityType<? extends GelobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        int i = random.nextInt(3);
        if (i < 2 && random.nextFloat() < 0.5f * difficulty.getClampedLocalDifficulty()) {
            ++i;
        }
        int j = 1 << i;
        this.setSize(j, true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @VisibleForTesting
    public void setSize(int size, boolean heal) {
        int i = MathHelper.clamp(size, 1, 127);
        this.dataTracker.set(GELOB_SIZE, i);
        this.refreshPosition();
        this.calculateDimensions();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(i * i);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * (float)i);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(i);
        if (heal) {
            this.setHealth(this.getMaxHealth());
        }
        this.experiencePoints = i;
    }

    @Override
    public void tick() {
        this.jumpStretch += (this.targetJumpStretch - this.jumpStretch) * 0.5f;
        this.lastJumpStretch = this.jumpStretch;
        super.tick();
        if (this.onGround && !this.onGroundLastTick) {
            int i = this.getSize();
            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2);
                float g = this.random.nextFloat() * 0.5f + 0.5f;
                float h = MathHelper.sin(f) * (float)i * 0.5f * g;
                float k = MathHelper.cos(f) * (float)i * 0.5f * g;
                this.world.addParticle(this.getParticles(), this.getX() + (double)h, this.getY(), this.getZ() + (double)k, 0.0, 0.0, 0.0);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.targetJumpStretch = -0.5f;
        } else if (!this.onGround && this.onGroundLastTick) {
            this.targetJumpStretch = 1.0f;
        }
        this.onGroundLastTick = this.onGround;
        this.updateStretch();

        onGroundTicks = onGround ? Math.min(TOUCH_DOWN_STRETCH_TICKS, onGroundTicks + 1) : 0;

        if (onGround) {
            if (random.nextFloat() < 0.1f) {
                float theta = random.nextFloat() * (float) Math.PI * 2;
                float r = 0.1f + 0.2f * random.nextFloat();
                GelobGelEntity gel = EntityRegister.GELOB_GEL_ENTITY_TYPE.create(world);
                assert gel != null;
                gel.refreshPositionAndAngles(getX() + Math.cos(theta) * r, getY(), getZ() + Math.sin(theta) * r, 0, 0);
                world.spawnEntity(gel);
            }
        } else if (isClimbingWall()) {
            if (random.nextFloat() < 0.1f) {

            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(GELOB_SIZE, 1);
    }

    protected void updateStretch() {
        this.targetJumpStretch *= 0.6f;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (GELOB_SIZE.equals(data)) {
            this.calculateDimensions();
            this.setYaw(this.headYaw);
            this.bodyYaw = this.headYaw;
            if (this.isTouchingWater() && this.random.nextInt(20) == 0) {
                this.onSwimmingStart();
            }
        }
        super.onTrackedDataSet(data);
    }


    protected ParticleEffect getParticles() {
        return ParticleRegister.GELOB_TYPE;
    }

    public boolean isSmall() {
        return this.getSize() <= 1;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.isSmall()) {
            return SoundEvents.ENTITY_SLIME_HURT_SMALL;
        }
        return SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.isSmall()) {
            return SoundEvents.ENTITY_SLIME_DEATH_SMALL;
        }
        return SoundEvents.ENTITY_SLIME_DEATH;
    }


    protected SoundEvent getSquishSound() {
        if (this.isSmall()) {
            return SoundEvents.ENTITY_SLIME_SQUISH_SMALL;
        }
        return SoundEvents.ENTITY_SLIME_SQUISH;
    }

    public int getSize() {
        return this.dataTracker.get(GELOB_SIZE);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (state.getMaterial().isLiquid()) {
            return;
        }
        BlockState blockState = this.world.getBlockState(pos.up());
        BlockSoundGroup blockSoundGroup = blockState.isIn(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? blockState.getSoundGroup() : state.getSoundGroup();
        this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
    }
}
