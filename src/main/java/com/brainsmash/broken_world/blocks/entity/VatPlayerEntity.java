package com.brainsmash.broken_world.blocks.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;

public class VatPlayerEntity extends ClientPlayerEntity {
    public VatPlayerEntity(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting) {
        super(client, world, networkHandler, stats, recipeBook, lastSneaking, lastSprinting);
    }

    @Override
    public boolean isSneaky() {
        return true;
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }
}
