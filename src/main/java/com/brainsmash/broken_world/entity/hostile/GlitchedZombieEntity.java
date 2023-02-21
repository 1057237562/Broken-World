package com.brainsmash.broken_world.entity.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class GlitchedZombieEntity extends ZombieEntity {

    public GlitchedZombieEntity(EntityType<? extends GlitchedZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createGlitchedZombieAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0).add(
                EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(
                EntityAttributes.GENERIC_ARMOR, 5.0).add(EntityAttributes.GENERIC_MAX_HEALTH, 40).add(
                EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
    }
}
