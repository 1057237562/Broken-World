package com.brainsmash.broken_world.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConstantsMap {
    private static final Map<String, Object> MAP = new HashMap<>();

    public static Set<String> keySet() {
        return MAP.keySet();
    }

    public static void put(String key, Object value) {
        MAP.put(key, value);
    }

    public static Object get(String key) {
        return MAP.get(key);
    }

    public static boolean getBoolOrDefault(String key, boolean defaultValue) {
        Object v = MAP.get(key);
        return v instanceof Boolean b ? b : defaultValue;
    }

    public static long getLongOrDefault(String key, long defaultValue) {
        Object v = MAP.get(key);
        return v instanceof Long b ? b : defaultValue;
    }

    public static double getDoubleOrDefault(String key, double defaultValue) {
        Object v = MAP.get(key);
        return v instanceof Double b ? b : defaultValue;
    }

    public static String getStringOrDefault(String key, String defaultValue) {
        Object v = MAP.get(key);
        return v instanceof String b ? b : defaultValue;
    }

}
