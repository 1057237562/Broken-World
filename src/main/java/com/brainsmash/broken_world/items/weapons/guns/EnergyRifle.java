package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.SoundRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EnergyRifle extends GunItem {

    private float recoil = -1.50f;
    private float spread = 0.15f;
    private float spreadModifier = 15f;

    public EnergyRifle(Settings settings) {
        super(settings);
        maxMagazine = 30;
    }

    public EnergyRifle(Settings settings, int maxMagazine) {
        super(settings);
        this.maxMagazine = maxMagazine;
    }

    @Override
    public boolean fire(World world, PlayerEntity user, Hand hand) {
        return true;
    }

    @Override
    public boolean fireTick(World world, PlayerEntity user, Hand hand) {
        if (hand != Hand.MAIN_HAND) return false;
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        if (user.getItemCooldownManager().isCoolingDown(this)) return world.isClient;
        user.getItemCooldownManager().set(this, 2);

        if (hasAmmo(itemStack) || user.getAbilities().creativeMode) {

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.SHOOT_EVENT,
                    SoundCategory.NEUTRAL, 0.4f, 0.6f / (world.getRandom().nextFloat() * 0.2f + 0.6f));
            if (!world.isClient) {
                BulletEntity heavyAmmoEntity = new BulletEntity(world, user, 1.35f);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                heavyAmmoEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(heavyAmmoEntity);
            } else {
                ((PlayerDataExtension) user).addPitchSpeed(recoil);
                ((PlayerDataExtension) user).addYawSpeed((float) (user.getRandom().nextGaussian() * recoil / 2f));
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
        ItemStack itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.ENERGY_AMMO.ordinal()]);
        while (!itemStack.isEmpty() && result < maxAmmo) {
            int count = Math.min(itemStack.getCount(), maxAmmo - result);
            if (count > 0) {
                itemStack.decrement(count);
                result += count;
            }
            itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.ENERGY_AMMO.ordinal()]);
        }
        return result;
    }
}
