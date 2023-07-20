package com.brainsmash.broken_world;

import net.minecraft.item.Item;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GasManager {

    public record Gas(int color, Item product) {
    }
    private static Map<Identifier, List<Pair<Gas, Integer>>> gases = new HashMap<>();
    public static void load() {
        MinecraftServer
    }
}
