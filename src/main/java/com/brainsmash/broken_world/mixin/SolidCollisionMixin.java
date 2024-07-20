package com.brainsmash.broken_world.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class SolidCollisionMixin extends Entity {

    public SolidCollisionMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    public boolean collidesWith(Entity other) {
        if (other instanceof LivingEntity) {
            return (this.isCollidable() && other.isCollidable());
        }
        return other.isCollidable() && !this.isConnectedThroughVehicle(other);
    }

    @Unique
    public boolean isCollidable() {
        return isAlive();
    }
}
