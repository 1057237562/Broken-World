package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ForestGuardianEntity extends HostileEntity {
    public ForestGuardianEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createForestGuardianAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 500.0).add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.42).add(
                EntityAttributes.GENERIC_MAX_HEALTH, 40).add(EntityAttributes.GENERIC_ARMOR, 3.0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.add(2, new AttackGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new TargetGoal(this));
        this.goalSelector.add(4, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f, 1.0f));
    }

    public static DefaultAttributeContainer createForestGuardianAttribute() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 300).add(
                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                0.25f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0).build();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.initEquipment(random, difficulty);
        return data;
    }

    public int getAttackTicksLeft() {
        return 0;
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(ForestGuardianEntity guardianEntity) {
            super(guardianEntity, 1.0, false);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return this.mob.getWidth() * 6.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
        }
    }

    static class TargetGoal extends ActiveTargetGoal<LivingEntity> {
        public TargetGoal(ForestGuardianEntity guardianEntity) {
            super(guardianEntity, LivingEntity.class, 0, true, true, LivingEntity::isMobOrPlayer);
        }

        @Override
        public void start() {
            super.start();
            this.mob.setDespawnCounter(0);
        }
    }
}
