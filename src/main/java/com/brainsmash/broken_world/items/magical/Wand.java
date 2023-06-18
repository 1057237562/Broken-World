package com.brainsmash.broken_world.items.magical;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item {
    public Wand(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        NbtCompound nbtCompound = user.getStackInHand(hand).getNbt();
        if (user.isSneaking()) {

        } else {

        }
        return super.use(world, user, hand);
    }
}
