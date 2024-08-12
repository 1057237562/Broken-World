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

public class Pistol extends GunItem {

    private float recoil = -1f;
    private float spread = 0.17f;

    private float spreadModifier = 4f;

    public Pistol(Settings settings) {
        super(settings);
    }

    @Override
    public void fire(World world, PlayerEntity user) {
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        if (user.getItemCooldownManager().isCoolingDown(this)) return;
        user.getItemCooldownManager().set(this, 3);

        if (hasAmmo(itemStack) || user.getAbilities().creativeMode) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.SHOOT_EVENT,
                    SoundCategory.NEUTRAL, 0.3f, 0.7f / (world.getRandom().nextFloat() * 0.2f + 0.4f));
            if (!world.isClient) {
                BulletEntity lightAmmoEntity = new BulletEntity(world, user, 0.75);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                lightAmmoEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(lightAmmoEntity);
            } else {
                ((PlayerDataExtension) user).addPitchSpeed(recoil);
                ((PlayerDataExtension) user).addYawSpeed((float) (user.getRandom().nextGaussian() * recoil));
            }
            if (!user.getAbilities().creativeMode) {
                reduceAmmo(itemStack);
                itemStack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
                //Util.getAmmo(user, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).decrement(1);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
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
}
