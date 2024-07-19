package com.brainsmash.broken_world.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;

public class SoundRegister {

    public static final SoundEvent SHOOT_EVENT = Registry.register(Registry.SOUND_EVENT, new Identifier(MODID, "shoot"),
            new SoundEvent(new Identifier(MODID, "shoot")));

    public static final SoundEvent BULLET_EVENT = Registry.register(Registry.SOUND_EVENT,
            new Identifier(MODID, "bullet"), new SoundEvent(new Identifier(MODID, "bullet")));
}
