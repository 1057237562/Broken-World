package com.brainsmash.broken_world.items.weapons.guns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface GunBase {
    public void fire(World world, PlayerEntity user);

    public void fireTick(World world, PlayerEntity user);
}
