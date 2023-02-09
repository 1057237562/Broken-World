package com.brainsmash.broken_world.items.weapons.guns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface GunBase {

    int maxMagazine = 8;

    void fire(World world, PlayerEntity user);

    default boolean fireTick(World world, PlayerEntity user) {
        return true;
    }
}
