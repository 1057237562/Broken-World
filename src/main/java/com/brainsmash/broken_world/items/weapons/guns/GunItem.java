package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.items.CustomUsePoseItem;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GunItem extends Item implements CustomUsePoseItem {
    int maxMagazine = 20;
    int maxReloadTime = 50;

    public GunItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.getOffHandStack().isEmpty()) {
            ItemStack itemStack = user.getStackInHand(hand);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            ItemStack itemStack = user.getStackInHand(hand);
            return TypedActionResult.fail(itemStack);
        }
    }

    public boolean fire(World world, PlayerEntity user, Hand hand) {
        return false;
    }

    public boolean fireTick(World world, PlayerEntity user, Hand hand) {
        return false;
    }

    public boolean hasAmmo(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) return false;
        return nbtCompound.getInt("ammoCount") > 0 && !nbtCompound.getBoolean("reloading");
    }

    public void reduceAmmo(ItemStack stack) {
        if (hasAmmo(stack)) {
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound == null) nbtCompound = new NbtCompound();
            nbtCompound.putInt("ammoCount", nbtCompound.getInt("ammoCount") - 1);
            stack.setNbt(nbtCompound);
        }
    }

    public void reload(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("reloading", true);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) nbtCompound = new NbtCompound();
        if (!selected) {
            nbtCompound.putBoolean("reloading", false);
            nbtCompound.putInt("reloadTick", 0);
        } else {
            if (nbtCompound.getBoolean("reloading")) {
                int tick = nbtCompound.getInt("reloadTick");
                if (tick < maxReloadTime) {
                    tick++;
                    nbtCompound.putInt("reloadTick", tick);
                } else {
                    nbtCompound.putBoolean("reloading", false);
                    nbtCompound.putInt("reloadTick", 0);

                    nbtCompound.putInt("ammoCount", nbtCompound.getInt("ammoCount") + countAmmo((PlayerEntity) entity,
                            maxMagazine - nbtCompound.getInt("ammoCount")));
                }
            }
        }
        stack.setNbt(nbtCompound);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public int countAmmo(PlayerEntity entity, int maxAmmo) {
        return 0;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public BipedEntityModel.ArmPose getUsePose() {
        return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
