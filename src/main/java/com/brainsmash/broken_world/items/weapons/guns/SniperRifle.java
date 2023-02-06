package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SniperRifle extends Item implements GunBase {

    private float recoil = -7.5f;
    private float spread = 0.01f;

    private float spreadModifier = 20f;

    public SniperRifle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void fire(World world, PlayerEntity user) {
        if (user.getItemCooldownManager().isCoolingDown(this)) return;
        user.getItemCooldownManager().set(this, 10);

        if (!Util.getAmmo(user,
                ItemRegister.items[ItemRegistry.SNIPER_AMMO.ordinal()]).isEmpty() || user.getAbilities().creativeMode) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!world.isClient) {
                BulletEntity sniperAmmo = new BulletEntity(world, user, 4.45f);

                float s = (float) (spread + user.getVelocity().length() * spreadModifier);
                sniperAmmo.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 5f, 1.0f);
                world.spawnEntity(sniperAmmo);
            }
            user.setPitch(user.getPitch() + recoil);
        }
        if (!user.getAbilities().creativeMode) {
            Util.getAmmo(user, ItemRegister.items[ItemRegistry.SNIPER_AMMO.ordinal()]).decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void fireTick(World world, PlayerEntity user) {

    }
}
