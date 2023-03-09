package com.brainsmash.broken_world.items;

import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CoordinateCardBase extends Item {

    private String dimensionName;

    public CoordinateCardBase(Settings settings, String dimensionName) {
        super(settings);
        this.dimensionName = dimensionName;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        NbtCompound nbtCompound = (NbtCompound) ((EntityDataExtension) user).getData();

        NbtList nbtList = nbtCompound.getList("dimension", NbtElement.COMPOUND_TYPE);
        NbtCompound keyValue = new NbtCompound();
        keyValue.putString("key", dimensionName);
        nbtList.add(keyValue);

        ((EntityDataExtension) user).setData(nbtCompound);

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
