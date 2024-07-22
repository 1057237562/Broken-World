package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.enchantment.ExperiencedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;

public class EnchantmentRegister {
    public static Enchantment EXPERIENCED = new ExperiencedEnchantment();

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(MODID, "experienced"), EXPERIENCED);
    }
}
