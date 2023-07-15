package com.brainsmash.broken_world.registry;

import net.minecraft.entity.damage.DamageSource;

public class DamageSourceRegister {
    public static final DamageSource ELECTRIC = new DamageSource("electricShock").setBypassesArmor().setUnblockable();
}
