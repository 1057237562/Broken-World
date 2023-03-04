package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ApocalyptorEntity extends HostileEntity {
    public ApocalyptorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(new Class[0]));
        this.goalSelector.add(2, new AttackGoal(this));
        this.targetSelector.add(3, new TargetGoal(this));
        this.goalSelector.add(4, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 3.0f, 1.0f));
        this.goalSelector.add(6, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
    }

    public static DefaultAttributeContainer createApocalyptorAttributes() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 300).add(
                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                0.21f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0).build();
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.15), this.getBodyY(1.1),
                        this.getParticleZ(0.15), 0.0, 0.0, 0.0);
            }
        }
        super.tickMovement();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.initEquipment(random, difficulty);
        return data;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND,
                new ItemStack(random.nextDouble() < 0.75f ? Items.STONE_AXE : Items.GOLDEN_AXE));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
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
