package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.SoundRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class SMG extends GunItem {

    private float recoil = -0.8f;
    private float spread = 1f;

    private float spreadModifier = 3f;

    public SMG(Settings settings) {
        super(settings);
        maxMagazine = 15;
    }

    @Override
    public boolean fire(World world, PlayerEntity user, Hand hand) {
        Item item = user.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).getItem();
        return item instanceof SMG || !(item instanceof GunItem);
    }

    @Override
    public boolean fireTick(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = Optional.ofNullable(itemStack.getNbt()).orElse(new NbtCompound());
        if (nbtCompound.getInt("cooldown") > 0) return world.isClient;
        nbtCompound.putInt("cooldown", 1);
        itemStack.setNbt(nbtCompound);

        if (hasAmmo(itemStack) || user.getAbilities().creativeMode) {

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.SHOOT_EVENT,
                    SoundCategory.NEUTRAL, 0.5f, 0.7f / (world.getRandom().nextFloat() * 0.2f + 0.4f));
            if (!world.isClient) {
                BulletEntity bulletEntity = new BulletEntity(world, user, 0.3f);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                bulletEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(bulletEntity);
            } else {
                ((PlayerDataExtension) user).addPitchSpeed(recoil);
                ((PlayerDataExtension) user).addYawSpeed((float) (user.getRandom().nextGaussian() * recoil));
            }
            if (!user.getAbilities().creativeMode) {
                reduceAmmo(itemStack);
                itemStack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return true;
    }

    @Override
    public int countAmmo(PlayerEntity entity, int maxAmmo) {
        int result = 0;
        ItemStack itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]);
        while (!itemStack.isEmpty() && result < maxAmmo) {
            int count = Math.min(itemStack.getCount(), maxAmmo - result);
            if (count > 0) {
                itemStack.decrement(count);
                result += count;
            }
            itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]);
        }
        return result;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) nbtCompound = new NbtCompound();
        if (nbtCompound.getInt("cooldown") > 0) {
            nbtCompound.putInt("cooldown", nbtCompound.getInt("cooldown") - 1);
        }
    }
}
