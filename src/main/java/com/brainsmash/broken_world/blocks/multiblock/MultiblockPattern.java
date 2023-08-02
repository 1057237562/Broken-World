package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.blocks.multiblock.util.StructureMaterial;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cottonmc.cotton.gui.widget.data.Vec2i;
import net.minecraft.tag.TagKey;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class MultiblockPattern {

    public Layer[] layers;
    public Map<Character, StructureMaterial> keypairs = Maps.newHashMap();
    public Vec3i size;

    public MultiblockPattern(JsonObject object) {
        JsonArray layersObj = object.get("layers").getAsJsonArray();
        layers = new Layer[layersObj.size()];

        size = new Vec3i(0, layersObj.size(), 0);

        for (int i = 0; i < layersObj.size(); i++) {
            layers[i] = new Layer(layersObj.get(i));
            size = new Vec3i(Math.max(size.getX(), layers[i].size.x()), size.getY(),
                    Math.max(size.getZ(), layers[i].size.y()));
        }

        JsonObject key = object.get("key").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : key.entrySet()) {
            keypairs.put(entry.getKey().charAt(0), getBlockPredicate(entry.getValue()));
        }
    }

    public boolean test(WorldAccess worldAccess, BlockPos startPos, BlockRotation rotation) {
        return test(worldAccess, startPos, rotation, BlockPos.ORIGIN);
    }

    public boolean test(WorldAccess worldAccess, BlockPos startPos, BlockRotation rotation, BlockPos anchor) {
        for (int y = 0; y < layers.length; y++) {
            Layer layer = layers[y];
            for (int z = 0; z < layer.rows.length; z++) {
                String row = layer.rows[z];
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c != ' ') {
                        BlockPos pos = new BlockPos(x, y, z).subtract(anchor).rotate(rotation);
                        if (!keypairs.get(c).test(worldAccess.getBlockState(startPos.add(pos)))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private StructureMaterial getBlockPredicate(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("tag")) {
                Identifier id = Identifier.tryParse(obj.get("tag").getAsString());
                return StructureMaterial.fromTag(TagKey.of(Registry.BLOCK_KEY, id));
            }
            if (obj.has("block")) {
                Identifier id = Identifier.tryParse(obj.get("block").getAsString());
                return StructureMaterial.ofBlocks(Registry.BLOCK.get(id));
            }
        } else if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            StructureMaterial predicates = StructureMaterial.empty();
            for (int i = 0; i < array.size(); i++) {
                predicates.concat(getBlockPredicate(array.get(i)));
            }
            return predicates;
        } else {
            throw new IllegalArgumentException("Invalid block predicate");
        }
        return StructureMaterial.empty();
    }

    public class Layer {

        int min = 1;
        int max = 1;
        String[] rows;

        public final Vec2i size;

        public Layer(JsonElement json) {
            JsonObject obj = json.getAsJsonObject();
            JsonElement minObj = obj.get("min");
            JsonElement maxObj = obj.get("max");

            if (minObj != null) {
                min = minObj.getAsInt();
            }

            if (maxObj != null) {
                max = maxObj.getAsInt();
            }

            if (min > max) {
                throw new IllegalArgumentException("min > max");
            } else if (min < 1) {
                throw new IllegalArgumentException("min < 1");
            }

            JsonArray rowsObj = obj.get("rows").getAsJsonArray();
            rows = new String[rowsObj.size()];
            int length = 0;
            for (int i = 0; i < rowsObj.size(); i++) {
                rows[i] = rowsObj.get(i).getAsString();
                length = Math.max(length, rows[i].length());
            }
            size = new Vec2i(length, rowsObj.size());
        }
    }
}
