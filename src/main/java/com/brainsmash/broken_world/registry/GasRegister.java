package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GasRegister implements SimpleSynchronousResourceReloadListener {
    public static final GasRegister INSTANCE = new GasRegister();
    public static final Identifier ID = new Identifier(Main.MODID, "gas_registry");
    private static final Map<Identifier, Gas> gases = new HashMap<>();
    private static final Map<Identifier, List<Pair<Gas, Integer>>> biomeGasProduction = new HashMap<>();
    public static final int BIOME_GAS_LIMIT = 5;
    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        gases.clear();
        biomeGasProduction.clear();

        manager.findResources("gas/", path -> path.getPath().equals("gas/gases.json")).forEach((id, resource) -> {
            try(InputStream stream = resource.getInputStream()) {
                JsonObject root = (JsonObject) JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonArray gasList = root.getAsJsonArray("gases");
                for (JsonElement element : gasList) {
                    JsonObject gas = (JsonObject) element;
                    Identifier productID = new Identifier(gas.get("product").getAsString());
                    Optional<Item> optional = Registry.ITEM.getOrEmpty(productID);
                    if (optional.isEmpty()) {
                        Main.LOGGER.error("No gas product item found for " + productID);
                        continue;
                    }
                    gases.put(
                            new Identifier(id.getNamespace(), gas.get("name").getAsString()),
                            new Gas(gas.get("color").getAsInt(), optional.get())
                    );
                }
            } catch (Exception e) {
                Main.LOGGER.error("Error loading gas registry for {}: {}", id, e);
            }
        });

        manager.findResources("gas/", path -> path.getPath().equals("gas/biomes.json")).forEach((id, resource) -> {
            try(InputStream stream = resource.getInputStream()) {
                JsonObject root = (JsonObject) JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonArray biomeList = root.getAsJsonArray("biomes");
                biomeList.forEach(element -> {
                    JsonObject biomeObj = (JsonObject) element;
                    Identifier biomeID = new Identifier(biomeObj.get("biome").getAsString());

                    List<Pair<Gas, Integer>> gasList = new ArrayList<>();
                    int cnt = 0;
                    for (JsonElement gasElement : biomeObj.getAsJsonArray("gases")) {
                        if (cnt++ >= BIOME_GAS_LIMIT) {
                            Main.LOGGER.warn("Too many gas types for biome {} (max {}), skipping. ", biomeID, BIOME_GAS_LIMIT);
                            break;
                        }
                        JsonObject gasObj = (JsonObject) gasElement;
                        Identifier gasID = new Identifier(gasObj.get("gas").getAsString());
                        Gas gas = gases.get(gasID);
                        if (gas == null) {
                            Main.LOGGER.error("Gas {} not found for biome {} in {}", gasID, biomeID, id);
                            continue;
                        }
                        int ticks = gasObj.get("ticks_per_product").getAsInt();
                        if (ticks <= 0) {
                            Main.LOGGER.error(
                                    "Error adding gas {} for biome {} in {} ticks_per_product must be positive",
                                    gasID,
                                    biomeID,
                                    id
                            );
                            break;
                        }
                        gasList.add(new Pair<>(gas, ticks));
                    }

                    biomeGasProduction.put(biomeID, gasList);
                });
            } catch (Exception e) {
                LogUtils.getLogger().error("Error loading biome gas production registry for " + id + ": " + e, e);
            }
        });
    }

    public record Gas(int color, Item product) {
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GasRegister.INSTANCE);
    }

    public static List<Pair<Gas, Integer>> getBiomeGases(Identifier biome) {
        return biomeGasProduction.getOrDefault(biome, new ArrayList<>());
    }

    public static List<Pair<Gas, Integer>> getBiomeGases(World world, BlockPos pos) {
        Optional<RegistryKey<Biome>> optional = world.getBiome(pos).getKey();
        if (optional.isEmpty())
            return new ArrayList<>();
        return getBiomeGases(optional.get().getValue());
    }
}
