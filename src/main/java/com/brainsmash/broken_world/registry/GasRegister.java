package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GasRegister implements SimpleSynchronousResourceReloadListener {
    public static final GasRegister INSTANCE = new GasRegister();
    public static final Identifier ID = new Identifier(Main.MODID, "gas_registry");
    private static final Map<Identifier, Gas> gases = new HashMap<>();
    private static final Map<Codecs.TagEntryId, Set<Pair<Gas, Integer>>> gasesByBiomes = new HashMap<>();
    private static final Map<Codecs.TagEntryId, Set<Pair<Gas, Integer>>> gasesByBiomeTags = new HashMap<>();
    public static final int BIOME_GAS_LIMIT = 5;
    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        gases.clear();
        gasesByBiomes.clear();

        manager.findResources("gas/", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try(InputStream stream = resource.getInputStream()) {
                JsonElement element = JsonParser.parseReader(new InputStreamReader(stream));
                Gas gas = Gas.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, element)).getOrThrow(false, Main.LOGGER::error);
                
                gases.put(id, gas);
                loop: for (Environment environment : gas.environments) {
                    Codecs.TagEntryId biome = environment.biome;

                    Map<Codecs.TagEntryId, Set<Pair<Gas, Integer>>> map = biome.tag() ? gasesByBiomeTags : gasesByBiomes;
                    Set<Pair<Gas, Integer>> set = map.computeIfAbsent(biome, arg -> new HashSet<>());
                    if (set.size() >= 5) {
                        Main.LOGGER.error("Cannot add {} to {} because a biome can have up to {} types of gases. ", id, biome, BIOME_GAS_LIMIT);
                        continue;
                    }
                    for (Pair<Gas, Integer> pair : set) {
                        if (pair.getLeft() == gas)
                            Main.LOGGER.error("Duplicate gas {} in {}. ", id, biome);
                        continue loop;
                    }
                    set.add(new Pair<>(gas, environment.ticksPerProduction()));
                }
            } catch (Exception e) {
                Main.LOGGER.error("Error loading gas registry for {}: {}", id, e);
            }
        });
    }

    public record Gas(int color, RegistryEntry<Item> product, List<Environment> environments) {
        public static final Codec<Gas> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("color").forGetter(Gas::color),
                        Identifier.CODEC.flatXmap(
                                id -> Registry.ITEM.getOrEmpty(id).map(item -> DataResult.success(item.getDefaultStack().getRegistryEntry())).orElse(DataResult.error("Cannot find gas product " + id)),
                                entry -> entry.getKey().map(itemRegistryKey -> DataResult.success(itemRegistryKey.getValue())).orElseGet(() -> DataResult.error("Cannot get registry key for direct item entry. "))
                        ).fieldOf("product").forGetter(Gas::product),
                        Environment.CODEC.listOf().fieldOf("environments").forGetter(Gas::environments)
                ).apply(instance, Gas::new)
        );
    }
    
    public record Environment(Codecs.TagEntryId biome, int ticksPerProduction) {
        public static final Codec<Environment> CODEC = RecordCodecBuilder.create(instance -> 
                instance.group(
                        Codecs.TAG_ENTRY_ID.fieldOf("biome").forGetter(Environment::biome),
                        Codecs.POSITIVE_INT.fieldOf("ticks_per_production").forGetter(Environment::ticksPerProduction)
                ).apply(instance, Environment::new)
        );
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GasRegister.INSTANCE);
    }

    public static List<Pair<Gas, Integer>> getBiomeGases(World world, BlockPos pos) {
        RegistryEntry<Biome> entry = world.getBiome(pos);

        Optional<RegistryKey<Biome>> optional = entry.getKey();
        if (optional.isPresent()) {
            Identifier biomeID = optional.get().getValue();
            Set<Pair<Gas, Integer>> set = gasesByBiomes.get(new Codecs.TagEntryId(biomeID, false));
            if (set != null)
                return List.copyOf(set);
        }

        Set<Pair<Gas, Integer>> set = new HashSet<>();
        for (Codecs.TagEntryId tag : gasesByBiomeTags.keySet()) {
            if (entry.isIn(TagKey.of(Registry.BIOME_KEY, tag.id())))
                set.addAll(gasesByBiomeTags.get(tag));
        }
        return List.copyOf(set);
    }
}
