package com.brainsmash.broken_world.recipe;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public record ColliderRecipe(Ingredient a, Ingredient b, int amountA, int amountB, ItemStack output, Identifier id) implements Recipe<ImplementedInventory> {
    @Override
    public boolean matches(ImplementedInventory inventory, World world) {
        if (inventory.size() < 2)
            return false;
        ItemStack stack1 = inventory.getStack(0);
        ItemStack stack2 = inventory.getStack(1);
        return a.test(stack1) && stack1.getCount() >= amountA && b.test(stack2) && stack2.getCount() >= amountB ||
                a.test(stack2) && stack2.getCount() >= amountA && b.test(stack1) && stack1.getCount() >= amountB;
    }

    @Override
    public ItemStack craft(ImplementedInventory inventory) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ColliderRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(Main.MODID, "colliding");

        @Override
        public ColliderRecipe read(Identifier id, JsonObject json) {
            int amount = json.get("amount_output").getAsInt();
            if (amount <= 0)
                throw new JsonSyntaxException("Collider recipe output amount must be positive");
            return new ColliderRecipe(
                    Ingredient.fromJson(json.get("a")),
                    Ingredient.fromJson(json.get("b")),
                    json.get("amount_a").getAsInt(),
                    json.get("amount_b").getAsInt(),
                    new ItemStack(Registry.ITEM.getOrEmpty(new Identifier(json.get("output").getAsString())).orElseThrow(() -> new JsonSyntaxException("No item found for output. ")), amount),
                    id
            );
        }

        @Override
        public ColliderRecipe read(Identifier id, PacketByteBuf buf) {
            return new ColliderRecipe(Ingredient.fromPacket(buf), Ingredient.fromPacket(buf), buf.readInt(), buf.readInt(), buf.readItemStack(), id);
        }

        @Override
        public void write(PacketByteBuf buf, ColliderRecipe recipe) {
            recipe.a.write(buf);
            recipe.b.write(buf);
            buf.writeInt(recipe.amountA);
            buf.writeInt(recipe.amountB);
            buf.writeItemStack(recipe.output);
        }
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ColliderRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final Identifier ID = new Identifier(Main.MODID, "collider_recipe");
    }

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, Serializer.ID, Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, Type.ID, Type.INSTANCE);
    }
}
