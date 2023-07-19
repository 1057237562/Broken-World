package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GasRegister {
    public static HashMap<Identifier, List<Pair<Item, Integer>>> registry = new HashMap<>();

    public static void register() {
        registry.put(new Identifier("plains"), new ArrayList<>());
        registry.get(new Identifier(("plains"))).add(new Pair<>(ItemRegister.items[ItemRegistry.OXYGEN_UNIT.ordinal()], 20));
    }

    @NotNull
    public static List<Pair<Item, Integer>> getEnvironmentGas(World world, BlockPos pos) {
        Optional<RegistryKey<Biome>> key = world.getBiome(pos).getKey();
        if (key.isEmpty())
            return new ArrayList<>();
        Identifier biome = key.get().getValue();
        return registry.getOrDefault(biome, new ArrayList<>());
    }
}
