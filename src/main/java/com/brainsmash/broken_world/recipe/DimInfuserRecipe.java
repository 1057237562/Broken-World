package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.Main;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class DimInfuserRecipe extends ShapelessRecipe {


    public DimInfuserRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, output, input);
    }

    public static class Type implements RecipeType<DimInfuserRecipe> {
        public static final Type INSTANCE = new Type();
        public static final Identifier ID = new Identifier(Main.MODID, "dim_infuser");

        private Type() {
        }

        public String toString() {
            return "dim_infuser";
        }
    }

    public static class Serializer extends ShapelessRecipe.Serializer {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(Main.MODID, "dim_infuser");

        @Override
        public DimInfuserRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            ShapelessRecipe father = super.read(identifier, packetByteBuf);
            return new DimInfuserRecipe(father.getId(), father.getGroup(), father.getOutput(), father.getIngredients());
        }

        @Override
        public DimInfuserRecipe read(Identifier identifier, JsonObject jsonObject) {
            ShapelessRecipe father = super.read(identifier, jsonObject);
            return new DimInfuserRecipe(father.getId(), father.getGroup(), father.getOutput(), father.getIngredients());
        }
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static void register() {
        Registry.register(Registry.RECIPE_TYPE, Type.ID, Type.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, Serializer.ID, Serializer.INSTANCE);
    }
}