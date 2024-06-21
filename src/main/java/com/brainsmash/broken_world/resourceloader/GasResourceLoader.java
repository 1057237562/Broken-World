package com.brainsmash.broken_world.resourceloader;

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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GasResourceLoader implements SimpleSynchronousResourceReloadListener {
    public static final GasResourceLoader INSTANCE = new GasResourceLoader();
    public static final Identifier ID = new Identifier(Main.MODID, "gas_registry");
    private static final Map<Identifier, Gas> gases = new HashMap<>();
    private static final Map<Identifier, Map<Gas, Integer>> gasesByBiomes = new HashMap<>();
    private static final Map<TagKey<Biome>, Map<Gas, Pair<Integer, Integer>>> gasesByBiomeTags = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        gases.clear();
        gasesByBiomes.clear();

        manager.findResources("gases/", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try (InputStream stream = resource.getInputStream()) {
                JsonElement element = JsonParser.parseReader(new InputStreamReader(stream));
                Gas gas = Gas.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, element)).getOrThrow(false,
                        Main.LOGGER::error);

                // There might be duplicated gases, fow now ignore it, it will be fixed in the future
                gases.put(id, gas);
                for (BiomeProduction production : gas.biomes) {
                    Identifier biomeID = production.biome;

                    Map<Gas, Integer> map = gasesByBiomes.computeIfAbsent(biomeID, arg -> new HashMap<>());
                    map.put(gas, production.ticksPerProduction);
                }
                for (BiomeTagProduction production : gas.biomeTags) {
                    TagKey<Biome> tag = production.tag;

                    Map<Gas, Pair<Integer, Integer>> map = gasesByBiomeTags.computeIfAbsent(tag,
                            arg -> new HashMap<>());
                    map.put(gas, new Pair<>(production.ticksPerProduction, production.priority));
                }
            } catch (Exception e) {
                Main.LOGGER.error("Error loading gas registry for {}: {}", id, e);
            }
        });
    }

    public record Gas(int color, RegistryEntry<Item> product, List<BiomeProduction> biomes,
                      List<BiomeTagProduction> biomeTags) {
        public static final Codec<Gas> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(Codec.INT.fieldOf("color").forGetter(Gas::color), Identifier.CODEC.flatXmap(
                                id -> Registries.ITEM.getOrEmpty(id).map(
                                        item -> DataResult.success(item.getDefaultStack().getRegistryEntry())).orElse(
                                        DataResult.error(() -> "Cannot find gas product " + id)), entry -> entry.getKey().map(
                                        itemRegistryKey -> DataResult.success(itemRegistryKey.getValue())).orElseGet(
                                        () -> DataResult.error(
                                                () -> "Cannot get registry key for direct item entry. "))).fieldOf(
                                "product").forGetter(Gas::product),
                        BiomeProduction.CODEC.listOf().fieldOf("biomes").forGetter(Gas::biomes),
                        BiomeTagProduction.CODEC.listOf().fieldOf("biome_tags").forGetter(Gas::biomeTags)).apply(
                        instance, Gas::new));
    }

    public record BiomeProduction(Identifier biome, int ticksPerProduction) {
        public static final Codec<BiomeProduction> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(Identifier.CODEC.fieldOf("biome").forGetter(BiomeProduction::biome),
                        Codecs.POSITIVE_INT.fieldOf("ticks_per_production").forGetter(
                                BiomeProduction::ticksPerProduction)).apply(instance, BiomeProduction::new));
    }

    public record BiomeTagProduction(TagKey<Biome> tag, int ticksPerProduction, int priority) {
        public static final Codec<BiomeTagProduction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagKey.codec(RegistryKeys.BIOME).fieldOf("tag").forGetter(BiomeTagProduction::tag),
                Codecs.POSITIVE_INT.fieldOf("ticks_per_production").forGetter(BiomeTagProduction::ticksPerProduction),
                Codec.INT.optionalFieldOf("priority", 50).forGetter(BiomeTagProduction::priority)).apply(instance,
                BiomeTagProduction::new));
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GasResourceLoader.INSTANCE);
    }

    public static List<Pair<Gas, Integer>> getBiomeGases(World world, BlockPos pos) {
        RegistryEntry<Biome> entry = world.getBiome(pos);

        Optional<RegistryKey<Biome>> optional = entry.getKey();
        if (optional.isPresent()) {
            Identifier biomeID = optional.get().getValue();
            Map<Gas, Integer> map = gasesByBiomes.get(biomeID);
            if (map != null) {
                List<Pair<Gas, Integer>> list = new ArrayList<>();
                map.forEach((gas, ticks) -> list.add(new Pair<>(gas, ticks)));
                return list;
            }
        }

        Map<Gas, Pair<Integer, Integer>> resultMap = new HashMap<>();
        for (TagKey<Biome> tag : gasesByBiomeTags.keySet()) {
            if (entry.isIn(TagKey.of(RegistryKeys.BIOME, tag.id()))) {
                gasesByBiomeTags.get(tag).forEach((k, v) -> {
                    Pair<Integer, Integer> existedV = resultMap.get(k);
                    if (existedV != null && existedV.getRight() > v.getRight()) return;
                    resultMap.put(k, v);
                });
            }
        }
        List<Pair<Gas, Integer>> result = new ArrayList<>();
        resultMap.forEach((gas, pair) -> result.add(new Pair<>(gas, pair.getLeft())));
        return result;
    }
}
