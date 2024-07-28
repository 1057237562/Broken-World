package com.brainsmash.broken_world.util;

import java.util.ArrayList;
import java.util.List;

public class MathHelper {
    public static int[] getDigits(int i) {
        if (i == 0) {
            return new int[] {0};
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
}
