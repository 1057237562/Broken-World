package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.Main;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class FabricatorRecipe extends ShapedRecipe {

    public FabricatorRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, width, height, input, output);
    }

    public static class Type implements RecipeType<FabricatorRecipe> {
        public static final Type INSTANCE = new Type();
        public static final Identifier ID = new Identifier(Main.MODID, "fabricating");

        private Type() {
        }

        public String toString() {
            return "fabricating";
        }
    }

    public static class Serializer extends ShapedRecipe.Serializer {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(Main.MODID, "fabricating");

        @Override
        public FabricatorRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            ShapedRecipe father = super.read(identifier, packetByteBuf);
            return new FabricatorRecipe(father.getId(),
                    father.getGroup(),
                    father.getWidth(),
                    father.getHeight(),
                    father.getIngredients(),
                    father.getOutput());
        }

        @Override
        public FabricatorRecipe read(Identifier identifier, JsonObject jsonObject) {
            ShapedRecipe father = super.read(identifier, jsonObject);
            return new FabricatorRecipe(father.getId(),
                    father.getGroup(),
                    father.getWidth(),
                    father.getHeight(),
                    father.getIngredients(),
                    father.getOutput());
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