package com.brainsmash.broken_world.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class EmergencyTeleporter extends Item {
    public EmergencyTeleporter(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.getDimensionKey() == DimensionTypes.OVERWORLD) {
            return TypedActionResult.fail(itemStack);
        } else {
            if (!world.isClient) {
                ServerWorld destination = ((ServerWorld) world).getServer().getWorld(
                        RegistryKey.of(RegistryKeys.WORLD, new Identifier("overworld")));
                if (destination != null) {
                    ServerPlayerEntity player = (ServerPlayerEntity) user;
                    BlockPos blockPos = SpawnLocating.findServerSpawnPoint(destination,
                            new ChunkPos(destination.getSpawnPos()));
                    player.teleport(destination, blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.getYaw(),
                            player.getPitch());
                }
            }
            user.setCurrentHand(hand);
            itemStack.decrement(1);
            user.setStackInHand(hand, itemStack);
            return TypedActionResult.success(itemStack, true);
        }
    }
}
