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

public class CoordinateCard extends Item {

    private String dimensionName;

    public CoordinateCard(Settings settings, String dimensionName) {
        super(settings);
        this.dimensionName = dimensionName;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        NbtCompound nbtCompound = (NbtCompound) ((EntityDataExtension) user).brokenWorld$getData();

        boolean flag = false;

        NbtList nbtList = nbtCompound.getList("dimension", NbtElement.COMPOUND_TYPE);
        if (nbtList != null) {
            for (NbtElement element : nbtList) {
                if (element instanceof NbtCompound compound) {
                    if (compound.getString("key").equals(dimensionName)) flag = true;
                }
            }
        }
        if (!flag) {
            NbtCompound keyValue = new NbtCompound();
            keyValue.putString("key", dimensionName);
            nbtList.add(keyValue);
            nbtCompound.put("dimension", nbtList);

            ((EntityDataExtension) user).brokenWorld$setData(nbtCompound);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
