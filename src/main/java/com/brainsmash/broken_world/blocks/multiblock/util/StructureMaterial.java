package com.brainsmash.broken_world.blocks.multiblock.util;

import com.google.common.collect.Lists;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StructureMaterial implements Predicate<BlockState> {
    public static final StructureMaterial EMPTY = new StructureMaterial(Stream.empty());
    private final Entry[] entries;
    @Nullable
    private Block[] matchingBlocks;
    @Nullable
    private IntList ids;

    private StructureMaterial(Stream<? extends Entry> entries) {
        this.entries = entries.toArray((i) -> new Entry[i]);
    }

    public Block[] getMatchingBlocks() {
        this.cacheMatchingBlocks();
        return this.matchingBlocks;
    }

    private void cacheMatchingBlocks() {
        if (this.matchingBlocks == null) {
            this.matchingBlocks = Arrays.stream(this.entries).flatMap(
                    (entry) -> entry.getBlocks().stream()).distinct().toArray((i) -> new Block[i]);
        }

    }

    public StructureMaterial concat(StructureMaterial structureMaterial) {
        return ofEntries(Stream.concat(Arrays.stream(this.entries), Arrays.stream(structureMaterial.entries)));
    }

    public boolean test(@Nullable BlockState blockstate) {
        if (blockstate == null) {
            return false;
        } else {
            this.cacheMatchingBlocks();
            if (this.matchingBlocks.length == 0) {
                return blockstate.isAir();
            } else {
                Block[] var2 = this.matchingBlocks;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    Block block = var2[var4];
                    if (blockstate.isOf(block)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public JsonElement toJson() {
        if (this.entries.length == 1) {
            return this.entries[0].toJson();
        } else {
            JsonArray jsonArray = new JsonArray();
            Entry[] var2 = this.entries;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Entry entry = var2[var4];
                jsonArray.add((JsonElement) entry.toJson());
            }

            return jsonArray;
        }
    }

    public boolean isEmpty() {
        return this.entries.length == 0 && (this.matchingBlocks == null || this.matchingBlocks.length == 0) && (this.ids == null || this.ids.isEmpty());
    }

    private static StructureMaterial ofEntries(Stream<? extends Entry> entries) {
        StructureMaterial structureMaterial = new StructureMaterial(entries);
        return structureMaterial.entries.length == 0 ? EMPTY : structureMaterial;
    }

    public static StructureMaterial empty() {
        return EMPTY;
    }

    public static StructureMaterial fromTag(TagKey<Block> tag) {
        return ofEntries(Stream.of(new TagEntry(tag)));
    }

    public static StructureMaterial ofBlocks(Block... blocks) {
        return ofEntries(Arrays.stream(blocks).map(BlockEntry::new));
    }

    public static StructureMaterial ofBlocks(Stream<Block> blocks) {
        return ofEntries(blocks.map(BlockEntry::new));
    }

    public static StructureMaterial fromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                return ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
            } else if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    throw new JsonSyntaxException("Block array cannot be empty, at least one block must be defined");
                } else {
                    return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> {
                        return entryFromJson(JsonHelper.asObject(jsonElement, "block"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected block to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Block cannot be null");
        }
    }

    private static Entry entryFromJson(JsonObject json) {
        if (json.has("block") && json.has("tag")) {
            throw new JsonParseException("An Structure-Material entry is either a tag or an block, not both");
        } else if (json.has("block")) {
            Block block = getBlock(json);
            return new BlockEntry(block);
        } else if (json.has("tag")) {
            Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));
            TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, identifier);
            return new TagEntry(tagKey);
        } else {
            throw new JsonParseException("An Structure-Material entry needs either a tag or an block");
        }
    }

    private static Block getBlock(JsonObject json) {
        String string = JsonHelper.getString(json, "block");
        Identifier identifier = new Identifier(string);
        return Registries.BLOCK.getOrEmpty(identifier).orElseThrow(
                () -> new JsonSyntaxException("Unknown block '" + identifier + "'"));
    }

    private interface Entry {
        Collection<Block> getBlocks();

        JsonObject toJson();
    }

    private static class TagEntry implements Entry {
        private final TagKey<Block> tag;

        TagEntry(TagKey<Block> tag) {
            this.tag = tag;
        }

        public Collection<Block> getBlocks() {
            List<Block> list = Lists.newArrayList();
            Iterator var2 = Registries.BLOCK.iterateEntries(this.tag).iterator();

            while (var2.hasNext()) {
                RegistryEntry<Block> registryEntry = (RegistryEntry) var2.next();
                list.add(registryEntry.value());
            }

            return list;
        }

        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tag", this.tag.id().toString());
            return jsonObject;
        }
    }

    static class BlockEntry implements Entry {
        private final Block block;

        BlockEntry(Block block) {
            this.block = block;
        }

        public Collection<Block> getBlocks() {
            return Collections.singleton(this.block);
        }

        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", Registries.BLOCK.getId(this.block).toString());
            return jsonObject;
        }
    }
}
