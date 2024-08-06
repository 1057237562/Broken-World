package com.brainsmash.broken_world.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MiscHelper {
    public static Text getEnchantmentName(Enchantment enchantment) {
        MutableText mutableText = Text.translatable(enchantment.getTranslationKey());
        if (enchantment.isCursed()) {
            mutableText.formatted(Formatting.RED);
        } else {
            mutableText.formatted(Formatting.GRAY);
        }
        return mutableText;
    }
}
