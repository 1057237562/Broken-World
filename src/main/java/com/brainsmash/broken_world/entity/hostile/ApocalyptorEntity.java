package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;

public class ApocalyptorEntity extends HostileEntity {
    public ApocalyptorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new AttackGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, RaiderEntity.class).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<MerchantEntity>((MobEntity) this, MerchantEntity.class, true));
        this.targetSelector.add(4, new TargetGoal(this));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0f, 1.0f));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
    }

    public static DefaultAttributeContainer createApocalyptorAttributes() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 400).build();
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(ApocalyptorEntity apocalyptor) {
            super(apocalyptor, 1.0, false);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return this.mob.getWidth() * 6.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
        }
    }

    static class TargetGoal extends ActiveTargetGoal<LivingEntity> {
        public TargetGoal(ApocalyptorEntity apocalyptor) {
            super(apocalyptor, LivingEntity.class, 0, true, true, LivingEntity::isMobOrPlayer);
        }

        @Override
        public void start() {
            super.start();
            this.mob.setDespawnCounter(0);
        }
    }
}
