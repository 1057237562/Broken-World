package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.SoundRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SniperRifle extends GunItem {

    private float recoil = -7.5f;
    private float spread = 0.01f;

    private float spreadModifier = 25f;

    public SniperRifle(Settings settings) {
        super(settings);
        maxMagazine = 10;
    }

    @Override
    public boolean fire(World world, PlayerEntity user, Hand hand) {
        if (!user.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).isEmpty()) return false;
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(this)) return false;
        user.getItemCooldownManager().set(this, 12);

        if (hasAmmo(itemStack) || user.getAbilities().creativeMode) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.SHOOT_EVENT,
                    SoundCategory.NEUTRAL, 1.2f, 0.1f / (world.getRandom().nextFloat() * 0.2f + 0.8f));
            if (!world.isClient) {
                BulletEntity sniperAmmo = new BulletEntity(world, user, 4.45f);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                sniperAmmo.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 6f, 1.0f);
                world.spawnEntity(sniperAmmo);
            } else {
                ((PlayerDataExtension) user).addPitchSpeed(recoil);
                ((PlayerDataExtension) user).addYawSpeed((float) (user.getRandom().nextGaussian() * recoil / 4f));
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
    public boolean fireTick(World world, PlayerEntity user, Hand hand) {
        return world.isClient;
    }

    @Override
    public int countAmmo(PlayerEntity entity, int maxAmmo) {
        int result = 0;
        ItemStack itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.SNIPER_AMMO.ordinal()]);
        while (!itemStack.isEmpty() && result < maxAmmo) {
            int count = Math.min(itemStack.getCount(), maxAmmo - result);
            if (count > 0) {
                itemStack.decrement(count);
                result += count;
            }
            itemStack = Util.getAmmo(entity, ItemRegister.items[ItemRegistry.SNIPER_AMMO.ordinal()]);
        }
        return result;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS,
                1.0F, 1.0F, false);
        return super.use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_SPYGLASS_STOP_USING,
                SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_SPYGLASS_STOP_USING,
                SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

}
