package com.brainsmash.broken_world.util;

import java.util.ArrayList;

public class MathHelper extends net.minecraft.util.math.MathHelper {
    public static int[] digits(int i) {
        if (i == 0) {
            return new int[]{0};
        }
        int j = Math.abs(i);
        ArrayList<Integer> list = new ArrayList<>();
        while (j > 0) {
            list.add(j % 10);
            j /= 10;
        }
        int[] array = new int[list.size()];
        for (int k = 0; k < list.size(); k++) {
            array[k] = list.get(k);
        }
        return array;
    }

    /**
     * Converts to Roman numerals.
     *
     * @param i The integer to be converted.
     *          Due to the nature of Roman numerals, it's best that 0 < i <= 3999.
     *          Negative numbers will be treated as positive numbers.
     *          If i == 0, returns an empty string.
     * @return The Roman numerals in string form.
     */
    public static String roman(int i) {
        final String[] hundreds = {
                "",
                "C",
                "CC",
                "CCC",
                "CD",
                "D",
                "DC",
                "DCC",
                "DCCC",
                "CM"
        };
        final String[] tens = {
                "",
                "X",
                "XX",
                "XXX",
                "XL",
                "L",
                "LX",
                "LXX",
                "LXXX",
                "XC"
        };
        final String[] units = {
                "",
                "I",
                "II",
                "III",
                "IV",
                "V",
                "VI",
                "VII",
                "VIII",
                "IX"
        };

        if (i == 0) {
            return "";
        }
        if (i < 0) {
            i = -i;
        }

        StringBuilder s = new StringBuilder();
        s.append("M".repeat(Math.max(0, i / 1000)));
        i %= 1000;
        int[] digits = digits(i);
        if (digits.length >= 3) s.append(hundreds[digits[2]]);
        if (digits.length >= 2) s.append(tens[digits[1]]);
        s.append(units[digits[0]]);
        return s.toString();
    }
}
