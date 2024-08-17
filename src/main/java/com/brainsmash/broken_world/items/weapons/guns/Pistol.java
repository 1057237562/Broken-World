package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.SoundRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class Pistol extends GunItem {

    private float recoil = -1f;
    private float spread = 0.17f;

    private float spreadModifier = 4f;

    public Pistol(Settings settings) {
        super(settings);
    }

    @Override
    public boolean fire(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = Optional.ofNullable(itemStack.getNbt()).orElse(new NbtCompound());
        if (nbtCompound.getInt("cooldown") > 0) return false;
        nbtCompound.putInt("cooldown", 3);
        itemStack.setNbt(nbtCompound);
        user.getItemCooldownManager().set(this, 3);

        if (hasAmmo(itemStack) || user.getAbilities().creativeMode) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.SHOOT_EVENT,
                    SoundCategory.NEUTRAL, 0.3f, 0.7f / (world.getRandom().nextFloat() * 0.2f + 0.4f));
            if (!world.isClient) {
                BulletEntity lightAmmoEntity = new BulletEntity(world, user, 0.45f);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                lightAmmoEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(lightAmmoEntity);
            } else {
                ((PlayerDataExtension) user).addPitchSpeed(recoil);
                ((PlayerDataExtension) user).addYawSpeed((float) (user.getRandom().nextGaussian() * recoil / 4f));
            }
            if (!user.getAbilities().creativeMode) {
                reduceAmmo(itemStack);
                itemStack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
                //Util.getAmmo(user, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).decrement(1);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return true;
    }

    @Override
    public boolean fireTick(World world, PlayerEntity user, Hand hand) {
        return world.isClient;
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
