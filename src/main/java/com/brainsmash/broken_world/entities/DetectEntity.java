package com.brainsmash.broken_world.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

public class DetectEntity extends MobEntity {
    public DetectEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
    }
}
