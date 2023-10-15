package com.brainsmash.broken_world.items.magical;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class WandCore extends Item {
    public WandCore(Settings settings) {
        super(settings);
    }

    public void execute(World world, PlayerEntity player, ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getNbt();
    }
}
