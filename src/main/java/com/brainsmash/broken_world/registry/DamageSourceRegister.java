package com.brainsmash.broken_world.registry;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DamageSourceRegister {
    public static final RegistryKey<DamageType> ELECTRIC_SHOCK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
            new Identifier("electricShock"));
    public static final DamageSource ELECTRIC = new DamageSources(DynamicRegistryManager.EMPTY).create(ELECTRIC_SHOCK);
}
