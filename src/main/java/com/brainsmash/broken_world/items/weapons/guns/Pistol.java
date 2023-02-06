package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Pistol extends Item implements GunBase {

    private float recoil = -1f;
    private float spread = 0.17f;

    private float spreadModifier = 4f;

    public Pistol(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void fire(World world, PlayerEntity user) {
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        if (user.getItemCooldownManager().isCoolingDown(this)) return;
        user.getItemCooldownManager().set(this, 3);

        if (!Util.getAmmo(user,
                ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).isEmpty() || user.getAbilities().creativeMode) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!world.isClient) {
                BulletEntity lightAmmoEntity = new BulletEntity(world, user, 0.75);

                float s = spread + ((user.isUsingItem() && user.getActiveItem() == itemStack) ? 0f : spreadModifier);
                lightAmmoEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(lightAmmoEntity);
            }
            user.setPitch(user.getPitch() + recoil);
        }
        if (!user.getAbilities().creativeMode) {
            Util.getAmmo(user, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}
