package com.brainsmash.broken_world.blocks.entity;

import com.brainsmash.broken_world.entity.VatPlayerEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.math.BlockPos;

public class CloneVatBlockEntity extends BlockEntity {


    private VatPlayerEntity playerEntity;

    public CloneVatBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CLONE_VAT_ENTITY_TYPE, pos, state);
    }

    protected NbtList toNbtList(float... values) {
        NbtList nbtList = new NbtList();
        for (float f : values) {
            nbtList.add(NbtFloat.of(f));
        }
        return nbtList;
    }

    public VatPlayerEntity getPlayerEntity() {
        if (playerEntity == null) {
            assert MinecraftClient.getInstance().interactionManager != null;
            playerEntity = new VatPlayerEntity(MinecraftClient.getInstance(), (ClientWorld) world,
                    MinecraftClient.getInstance().getNetworkHandler(), new StatHandler(), new ClientRecipeBook(), false,
                    false);
            if (playerEntity != null) {
                NbtCompound nbt = MinecraftClient.getInstance().player.writeNbt(new NbtCompound());
                nbt.put("Rotation", toNbtList(0.0f, 0.0f));
                playerEntity.readNbt(nbt);
                playerEntity.setPose(EntityPose.STANDING);
                playerEntity.getInventory().clear();
            }
        }
        return playerEntity;
    }
}
