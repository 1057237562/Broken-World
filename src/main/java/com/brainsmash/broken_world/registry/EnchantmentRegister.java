package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.enchantment.SoulLeechingEnchantment;
import com.brainsmash.broken_world.enchantment.SoulPullyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;

public class EnchantmentRegister {
    public static Enchantment SOUL_LEECHING = new SoulLeechingEnchantment();
    public static Enchantment SOUL_PULLY = new SoulPullyEnchantment();

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(MODID, "soul_leeching"), SOUL_LEECHING);
        Registry.register(Registry.ENCHANTMENT, new Identifier(MODID, "soul_pully"), SOUL_PULLY);
    }
}
