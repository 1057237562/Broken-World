package com.brainsmash.broken_world.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Optional;

public class SerializationHelper {

    public static BlockState loadBlockState(NbtCompound nbtCompound) {
        Block block = Registry.BLOCK.getOrEmpty(new Identifier(nbtCompound.getString("blockId"))).orElse(null);
        if (block == null) return null;

        BlockState state = block.getDefaultState();
        NbtCompound stateObject = (NbtCompound) nbtCompound.get("BlockStateTag");
        if (stateObject == null) stateObject = nbtCompound;

        Collection<Property<?>> properties = state.getProperties();
        for (String key : stateObject.getKeys()) {
            if (stateObject == nbtCompound && (key.equals("BlockStateTag") || key.equals("block"))) continue;

            for (Property<?> property : properties) {
                if (property.getName().equals(key)) {
                    String val = stateObject.getString(key);
                    state = withProperty(state, property, val);
                    break;
                }
            }
        }

        return state;
    }

    public static NbtCompound saveBlockState(BlockState state) {
        BlockState defaultState = state.getBlock().getDefaultState();

        NbtCompound result = new NbtCompound();
        result.putString("blockId", Registry.BLOCK.getId(state.getBlock()).toString());
        NbtCompound stateObject = result;
        for (Property<?> property : state.getProperties()) {
            String key = property.getName();
            if (key.equals("block") || key.equals("BlockStateTag")) {
                stateObject = new NbtCompound();
                result.put("BlockStateTag", stateObject);
                break;
            }
        }

        for (Property<?> property : state.getProperties()) {
            if (state.get(property).equals(defaultState.get(property))) continue;
            String key = property.getName();
            String val = getProperty(state, property);
            stateObject.putString(key, val);
        }

        return result;

    }

    public static <T extends Comparable<T>> BlockState withProperty(BlockState state, Property<T> property, String stringValue) {
        Optional<T> val = property.parse(stringValue);
        if (val.isPresent()) {
            return state.with(property, val.get());
        } else {
            return state;
        }
    }

    public static <T extends Comparable<T>> String getProperty(BlockState state, Property<T> property) {
        return property.name(state.get(property));
    }
}
