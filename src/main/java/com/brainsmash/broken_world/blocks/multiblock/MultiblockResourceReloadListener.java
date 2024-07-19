package com.brainsmash.broken_world.blocks.multiblock;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

import static com.brainsmash.broken_world.Main.MODID;

public class MultiblockResourceReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {

    public MultiblockResourceReloadListener() {
        super(new Gson(), "multiblocks");
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(MODID, "multiblocks");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((id, element) -> {
            if (element.isJsonObject()) {
                MultiblockUtil.patternMap.put(id, new MultiblockPattern(element.getAsJsonObject()));
            } else {
                MultiblockUtil.LOGGER.warn("Failed to load pattern {}", id);
                MultiblockUtil.LOGGER.warn("MultiblockPattern {} is not a JSON object", id);
            }
        });
    }
}

