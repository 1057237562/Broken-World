package com.brainsmash.broken_world.items.weapons.guns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface GunBase {
    void fire(World world, PlayerEntity user);

    default boolean fireTick(World world, PlayerEntity user) {
        return false;
    }
}
