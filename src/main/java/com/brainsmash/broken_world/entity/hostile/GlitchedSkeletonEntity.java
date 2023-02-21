package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.World;

public class GlitchedSkeletonEntity extends SkeletonEntity {
    public GlitchedSkeletonEntity(EntityType<? extends GlitchedSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createGlitchedSkeletonAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0).add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0).add(EntityAttributes.GENERIC_MAX_HEALTH, 40).add(
                EntityAttributes.GENERIC_ARMOR, 3.0);
    }
}
